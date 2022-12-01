package org.ethio.gpro.repositories.api;

import org.ethio.gpro.models.AccountResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountApi {

    @POST("/customers/unlock")
    Call<AccountResponse> unlock(@Query("unlock_token") String unlockToken);

    @POST("/customers/confirmation")
    Call<AccountResponse> confirm(@Query("confirmation_token") String confirmationToken);
}
