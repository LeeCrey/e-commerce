package org.ethio.gpro.repositories;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.data.base.AppDataBase;
import org.ethio.gpro.helpers.ResponseCode;
import org.ethio.gpro.models.Category;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.repositories.api.ProductApi;
import org.ethio.gpro.repositories.retrofit.RetrofitConnectionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsRepository {
    private static final String TAG = "ProductsRepository";
    private final ProductApi api;
    private final LiveData<List<Category>> mCategories;
    private final MutableLiveData<List<Product>> mProducts;
    private final MutableLiveData<List<Product>> mRecommended;
    private Call<List<Category>> categoryCall;

    public ProductsRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(ProductApi.class);
        mProducts = new MutableLiveData<>();
        mRecommended = new MutableLiveData<>();

        // get from db
        mCategories = AppDataBase.getInstance(application).categoryDAO().getAll();
    }

    public LiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public LiveData<List<Product>> getProducts() {
        return mProducts;
    }

    public LiveData<List<Product>> getRecommend() {
        return mRecommended;
    }

    public void makeApiRequestForCategory() {
        // code for e-tag
        categoryCall = api.categories("");
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (!ResponseCode.isNotModified(response.code())) {
                    // if there is change in back-end update table
                    // TO-DO
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    public void makeApiRequestForProducts() {
        new Handler().postDelayed(() -> {
            List<Product> ps = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                Product p = new Product();
                p.setName("SAMSUNG");
                p.setId(i + 1);
                p.setPrice(3453.43);
                if (i % 2 == 0) {
                    p.setDiscount(342.0);
                }
                p.setRate(3.0f);
                ps.add(p);
            }

            mProducts.postValue(ps);
            mRecommended.postValue(ps);
        }, 4_000);
    }

    public void cancelConnection() {
        if (null != categoryCall) {
            if (!categoryCall.isExecuted() || !categoryCall.isExecuted()) {
                categoryCall.cancel();
            }
        }
    }

    public void searchProduct(String cat) {
        Log.d(TAG, "searchProduct: " + cat);
        makeApiRequestForProducts();
    }
}
