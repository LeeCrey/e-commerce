package org.ethio.gpro.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.models.AccountResponse;
import org.ethio.gpro.repositories.api.AccountApi;
import org.ethio.gpro.repositories.retrofit.RetrofitConnectionUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// this is for confirm, reset and unlock operations
public class AccountRepository {
    private final AccountApi api;
    private final MutableLiveData<AccountResponse> mAccountResponse;
    private Call<AccountResponse> responseCall;

    public AccountRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(AccountApi.class);

        mAccountResponse = new MutableLiveData<>();
    }

    public LiveData<AccountResponse> getAccountResponse() {
        return mAccountResponse;
    }

    public void cancelConnection() {
        if (responseCall != null) {
            responseCall.cancel();
            responseCall = null;
        }
    }

    // implementing api

    public void unlock(@NonNull String token) {
        cancelConnection();

        responseCall = api.unlock(token);
        responseCall.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccountResponse> call, @NonNull Response<AccountResponse> response) {
                mAccountResponse.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AccountResponse> call, @NonNull Throwable t) {
                AccountResponse response = new AccountResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mAccountResponse.postValue(response);
            }
        });
    }

    public void confirm(String confirmationToken) {
        cancelConnection();

        responseCall = api.confirm(confirmationToken);
        responseCall.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccountResponse> call, @NonNull Response<AccountResponse> response) {
                mAccountResponse.postValue(response.body());
                // there may be error
            }

            @Override
            public void onFailure(@NonNull Call<AccountResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void passwordReset(String resetPasswordToken, String password, String passwordConfirmation) {
    }
}
