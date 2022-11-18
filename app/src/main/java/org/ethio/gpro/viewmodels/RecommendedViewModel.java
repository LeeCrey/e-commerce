package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ethio.gpro.models.Product;
import org.ethio.gpro.repositories.ProductsRepository;

import java.util.List;

public class RecommendedViewModel extends AndroidViewModel {
    private final ProductsRepository repository;
    private final LiveData<List<Product>> oProducts;

    public RecommendedViewModel(@NonNull Application application) {
        super(application);

        repository = new ProductsRepository(application);
        oProducts = repository.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return oProducts;
    }
}
