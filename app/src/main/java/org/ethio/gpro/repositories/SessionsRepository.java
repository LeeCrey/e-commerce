package org.ethio.gpro.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
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
    private MutableLiveData<SessionResponse> mSessionResult;
    private Call<SessionResponse> sessionResultCall;
    private SessionsApi api;

    public SessionsRepository(@NonNull Application application) {
        if (mSessionResult != null) {
            return;
        }

        mSessionResult = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(SessionsApi.class);
    }

    public MutableLiveData<SessionResponse> getSessionResult() {
        return mSessionResult;
    }

    public void login(Customer customer) {
        if (null != sessionResultCall) {
            sessionResultCall.cancel();
        }

        sessionResultCall = api.login(customer);
        sessionResultCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (ResponseCode.isUnAuthorized(response.code())) {
                    mSessionResult.postValue(getErrorMessage("Incorrect email or password"));
                } else {
                    mSessionResult.postValue(response.body());
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
        if (null != sessionResultCall) {
            sessionResultCall.cancel();
        }

        sessionResultCall = api.logout(authorizationToken);
        sessionResultCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                mSessionResult.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                String a = String.valueOf(t instanceof IOException);
                mSessionResult.postValue(getErrorMessage(a));
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
