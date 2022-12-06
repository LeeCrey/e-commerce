package org.ethio.gpro.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.models.Category;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.models.responses.ProductResponse;
import org.ethio.gpro.models.responses.ProductShowResponse;
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
    private final MutableLiveData<List<Product>> mProducts;
    private MutableLiveData<List<Category>> mCategories;
    private MutableLiveData<List<Product>> mRecommended;
    private MutableLiveData<List<Product>> mTrending;

    private MutableLiveData<Product> mProduct; // show product => products/id

    private Call<List<Category>> categoryCall;
    private Call<ProductResponse> productCall;

    private Call<ProductShowResponse> productShowResponseCall;

    public ProductsRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(ProductApi.class);
        mProducts = new MutableLiveData<>();
    }

    // init
    public void initForHomeFragment() {
        mRecommended = new MutableLiveData<>();
        mCategories = new MutableLiveData<>();
        mTrending = new MutableLiveData<>();
    }

    public void initForShowFragment() {
        mProduct = new MutableLiveData<>();
    }

    // for observers
    public LiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public LiveData<List<Product>> getProducts() {
        return mProducts;
    }

    public LiveData<List<Product>> getRecommend() {
        return mRecommended;
    }

    public LiveData<List<Product>> getTrending() {
        return mTrending;
    }

    // api
    public void makeApiRequestForCategory() {
        if (categoryCall != null) {
            categoryCall.cancel();
        }

        // code for e-tag
        categoryCall = api.categories();
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                Category all = new Category();
                all.setCategoryId(-1);
                all.setName("All");
                all.setSelected(true);
                List<Category> categories = new ArrayList<>();
                categories.add(all);
                List<Category> res = response.body();
                if (res != null) {
                    categories.addAll(res);
                }
                mCategories.postValue(categories);
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    public void makeApiRequestForProducts(@NonNull String cat) {
        if (productCall != null) {
            productCall.cancel();
        }

        productCall = api.index(cat);
        productCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                final ProductResponse productResponse = response.body();
                if (productResponse != null) {
                    mProducts.postValue(productResponse.getProducts());
                    mRecommended.postValue(productResponse.getRecommended());
                    mTrending.postValue(productResponse.getTrending());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void searchProduct(String cat) {
        makeApiRequestForProducts(cat);
    }

    public void cancelConnection() {
        if (null != categoryCall) {
            if (!categoryCall.isExecuted() || !categoryCall.isExecuted()) {
                categoryCall.cancel();
            }
        }
    }


    // for product show fragment
    public LiveData<Product> showProduct() {
        return mProduct;
    }

    public void makeProductShowRequest(int productId) {
        if (null != productShowResponseCall) {
            productShowResponseCall.cancel();
        }

        productShowResponseCall = api.show(productId);
        productShowResponseCall.enqueue(new Callback<ProductShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductShowResponse> call, @NonNull Response<ProductShowResponse> response) {
                if (response.isSuccessful()) {
                    ProductShowResponse productResponse = response.body();
                    if (productResponse != null) {
                        mProduct.postValue(productResponse.getProduct());
                        mProducts.setValue(productResponse.getRelatedProducts());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductShowResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
