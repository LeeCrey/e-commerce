package org.ethio.gpro.repositories.api.account;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SessionsApi {
    @Headers("accept: application/json")
    @POST("customers/sign_in")
    Call<SessionResponse> login(@Body Customer customer);

    @Headers("accept: application/json")
    @DELETE("customers/sign_out")
    Call<SessionResponse> logout(@Header("Authorization") String token);
}
