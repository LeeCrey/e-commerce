package org.ethio.gpro.repositories.api;

import org.ethio.gpro.models.ProductResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CartApi {
    @Headers({"accept: application/json"})
    @GET("carts")
    Call<ProductResponse> index(@Header("If-None-Match") String etag);

    @Headers({"accept: application/json"})
    @GET("carts/{id}")
    Call<ProductResponse> show(@Path("id") int productId, @Header("If-None-Match") String etag);

    @Headers({"accept: application/json"})
    @DELETE("carts/{id}")
    Call<ProductResponse> delete(@Path("id") int cartId);
}
