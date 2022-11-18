package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ethio.gpro.models.Product;
import org.ethio.gpro.repositories.ProductsRepository;

import java.util.List;

public class SearchFragmentViewModel extends AndroidViewModel {
    private final ProductsRepository repository;
    private final LiveData<List<Product>> mProductList;

    public SearchFragmentViewModel(@NonNull Application application) {
        super(application);

        repository = new ProductsRepository(application);
        mProductList = repository.getProducts();
    }

    public LiveData<List<Product>> getProductList() {
        return mProductList;
    }

    public void searchProduct(String query) {
        String dat = query.trim();
        if (dat.isEmpty()) {
            dat = "all";
        }
        repository.searchProduct(dat);
    }
}
