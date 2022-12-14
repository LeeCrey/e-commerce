package org.ethio.gpro.repositories.api;

import org.ethio.gpro.models.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RateApi {
    //    INDEX
    @GET("products/{product_id}/comments.json")
    Call<List<Comment>> getComments(@Path("product_id") Integer productId, @Header("Authorization") String authorizationToken);

    //    CREATE
    @POST("products/{product_id}/comments")
    Call<String> createComment(@Body Comment comment, @Header("Authorization") String authorizationToke);

    //    UPDATE
    @PATCH("comments/{id}")
    Call<String> updateComment(@Header("Authorization") String authorizationToke);

    //    DELETE
    @DELETE("comments/{id}")
    Call<String> deleteComment(@Header("Authorization") String authorizationToke);
}
