package org.ethio.gpro.repositories.api;

import org.ethio.gpro.models.Category;
import org.ethio.gpro.models.responses.ProductResponse;
import org.ethio.gpro.models.responses.ProductShowResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    // controllers/products_controllers.rb#method => index
    @Headers({"accept: application/json"})
    @GET("products")
    Call<ProductResponse> index(@Query("category") String category);

    // controllers/products_controllers.rb#method => show
    @Headers({"accept: application/json"})
    @GET("products/{id}")
    Call<ProductShowResponse> show(@Path("id") int productId);

    // controllers/products_controller.rb#method => categories
    @Headers({"accept: application/json"})
    @GET("categories")
    Call<List<Category>> categories();
}
