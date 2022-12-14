package org.ethio.gpro.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.InstructionsResponse;
import org.ethio.gpro.repositories.api.account.InstructionsApi;
import org.ethio.gpro.repositories.retrofit.RetrofitConnectionUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructionRepository {
    private final MutableLiveData<InstructionsResponse> mInstructionResponse;
    private final InstructionsApi api;
    private Call<InstructionsResponse> instructionsResponseCall;

    public InstructionRepository(Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(InstructionsApi.class);
        mInstructionResponse = new MutableLiveData<>();
    }

    public MutableLiveData<InstructionsResponse> getInstructionResponse() {
        return mInstructionResponse;
    }

    // APIs
    public void sendRequest(Customer customer, boolean accountLockRequest) {
        cancelConnection();

        if (accountLockRequest) {
            instructionsResponseCall = api.sendUnlockInstruction(customer);
        } else {
            instructionsResponseCall = api.sendResetPasswordInstruction(customer);
        }

        instructionsResponseCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                final InstructionsResponse resp = response.body();
                if (resp != null) {
                    mInstructionResponse.postValue(resp);
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mInstructionResponse.postValue(response);
            }
        });
    }

    // odd
    // feedback
    public void sendFeedback(String header, String message) {
        cancelConnection();

        instructionsResponseCall = api.sendFeedback(header, message);
        instructionsResponseCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                if (response.isSuccessful()) {
                    final InstructionsResponse resp = response.body();
                    if (resp != null) {
                        mInstructionResponse.postValue(resp);
                    }
                } else {
                    InstructionsResponse instructionsResponse = new InstructionsResponse();
                    instructionsResponse.setOkay(false);
                    instructionsResponse.setMessage("Un-Authorized request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mInstructionResponse.postValue(response);
            }
        });
    }

    public void cancelConnection() {
        if (null != instructionsResponseCall) {
            if (!(instructionsResponseCall.isExecuted() || instructionsResponseCall.isCanceled())) {
                instructionsResponseCall.cancel();
            }
        }
    }
}
