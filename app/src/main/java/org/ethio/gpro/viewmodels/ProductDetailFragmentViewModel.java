package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ethio.gpro.models.Product;
import org.ethio.gpro.repositories.ProductsRepository;

import java.util.List;

public class ProductDetailFragmentViewModel extends AndroidViewModel {
    protected final LiveData<List<Product>> oProducts;
    protected final ProductsRepository productsRepository;
    private final LiveData<Product> oProduct;

    public ProductDetailFragmentViewModel(@NonNull Application application) {
        super(application);

        productsRepository = new ProductsRepository(application);
        productsRepository.initForShowFragment();

        oProducts = productsRepository.getProducts();
        oProduct = productsRepository.showProduct();
    }

    // for observe
    public LiveData<List<Product>> getProducts() {
        return oProducts;
    }

    public LiveData<Product> getProduct() {
        return oProduct;
    }

    // API
    public void makeShowApiRequest(int productId) {
        productsRepository.makeRelatedProductCategory(productId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (productsRepository != null) {
            productsRepository.cancelConnection();
        }
    }
}
