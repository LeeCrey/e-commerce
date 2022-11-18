package org.ethio.gpro.repositories.api.account;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface RegistrationsApi {
    // create account
    @Headers("accept: application/json")
    @POST("customers")
    Call<RegistrationResponse> signUp(@Body Customer customer);

    // delete account
    @Headers("accept: application/json")
    @DELETE("customers")
    Call<RegistrationResponse> destroyAccount(@Header("Authorization") String authorizationToken);

    // edit account
    @Headers("accept: application/json")
    @PATCH("customers")
    Call<RegistrationResponse> updateAccount(@Header("Authorization") String authorizationToken, @Body Customer customer);
}
