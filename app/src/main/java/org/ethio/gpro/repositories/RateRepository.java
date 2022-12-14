package org.ethio.gpro.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.models.Comment;
import org.ethio.gpro.repositories.api.RateApi;
import org.ethio.gpro.repositories.retrofit.RetrofitConnectionUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateRepository {
    private static final String TAG = "RateRepository";
    private final MutableLiveData<List<Comment>> mCommentList;
    private final RateApi api;
    private final String token;
    private final int productId;
    private Call<List<Comment>> commentCall;

    public RateRepository(@NonNull Application application, int productId, String token) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(RateApi.class);

        mCommentList = new MutableLiveData<>();
        this.token = token;
        this.productId = productId;
    }

    public LiveData<List<Comment>> getCommentList() {
        return mCommentList;
    }

    // apis implement
    public void getCommentsFromApi() {
        commentCall = api.getComments(productId, token);
        commentCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                List<Comment> list = response.body();
                mCommentList.postValue(list);
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    public void cancelConnection() {
        if (commentCall != null) {
            commentCall.cancel();
        }
    }
}
