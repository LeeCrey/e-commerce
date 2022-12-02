package org.ethio.gpro.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.helpers.ResponseCode;
import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.SessionResponse;
import org.ethio.gpro.repositories.api.account.SessionsApi;
import org.ethio.gpro.repositories.retrofit.RetrofitConnectionUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionsRepository {
    private static final String TAG = "SessionsRepository";
    private MutableLiveData<SessionResponse> mSessionResult;
    private MutableLiveData<SessionResponse> mLogoutResult;
    private Call<SessionResponse> sessionResultCall;
    private SessionsApi api;

    public SessionsRepository(@NonNull Application application) {
        if (mSessionResult != null) {
            return;
        }

        mSessionResult = new MutableLiveData<>();
        mLogoutResult = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(SessionsApi.class);
    }

    public LiveData<SessionResponse> getLogoutResult() {
        return mLogoutResult;
    }

    public LiveData<SessionResponse> getSessionResult() {
        return mSessionResult;
    }

    public void login(Customer customer) {
        cancelConnection();

        sessionResultCall = api.login(customer);
        sessionResultCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (ResponseCode.isUnAuthorized(response.code())) {
                    mSessionResult.postValue(getErrorMessage("Incorrect email or password"));
                } else {
                    SessionResponse sessionResponse = response.body();
                    if (sessionResponse != null) {
                        sessionResponse.setAuthToken(response.headers().get("Authorization"));
                    }
                    mSessionResult.postValue(sessionResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                SessionResponse resp = null;
                if (t instanceof SocketTimeoutException) {
                    resp = getErrorMessage("Server timeout.");
                } else if (t instanceof UnknownHostException) {
                    resp = getErrorMessage("Unknown host.");
                } else if (call.isCanceled()) {
                    resp = getErrorMessage("Cancelled connection.");
                } else {
                    resp = getErrorMessage(t.getMessage());
                }
                mSessionResult.postValue(resp);
            }
        });
    }

    public void logout(String authorizationToken) {
        cancelConnection();

        sessionResultCall = api.logout(authorizationToken);
        sessionResultCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    mLogoutResult.postValue(response.body());
                } else {
                    mLogoutResult.postValue(getErrorMessage("Un-authorized"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                String a = String.valueOf(t instanceof IOException);
                mLogoutResult.postValue(getErrorMessage(a));
            }
        });
    }

    private SessionResponse getErrorMessage(String msg) {
        SessionResponse result = new SessionResponse();
        result.setOkay(false);
        result.setError(msg);

        return result;
    }

    public void cancelConnection() {
        if (null != sessionResultCall) {
            if (!(sessionResultCall.isCanceled() || sessionResultCall.isExecuted())) {
                sessionResultCall.cancel();
            }
        }
    }
}
