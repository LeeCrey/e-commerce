package org.ethio.gpro.repositories.api.account;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InstructionsApi {
    @Headers("accept: application/json")
    @POST("customers/password")
    Call<InstructionsResponse> sendResetPasswordInstruction(@Body Customer customer);

    @Headers("accept: application/json")
    @POST("/customers/confirmation")
    Call<InstructionsResponse> sendConfirmationInstruction(@Body Customer customer);

    @Headers("accept: application/json")
    @POST("customers/unlock")
    Call<InstructionsResponse> sendUnlockInstruction(@Body Customer customer);
}
