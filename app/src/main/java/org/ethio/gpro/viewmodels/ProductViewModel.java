package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.models.Category;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.repositories.ProductsRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private static final String TAG = "ProductViewModel";

    // o just to mean observable
    private final LiveData<List<Category>> oCategories;
    private final LiveData<List<Product>> oProducts;
    private final LiveData<List<Product>> oRecommended;

    private final ProductsRepository repository;
    //
    private final MutableLiveData<Integer> selectedCategoryPosition;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        selectedCategoryPosition = new MutableLiveData<>(-1);

        repository = new ProductsRepository(application);
        oCategories = repository.getCategories();
        oProducts = repository.getProducts();
        oRecommended = repository.getRecommend();

        repository.makeApiRequestForCategory();
        repository.makeApiRequestForProducts();
    }

    // categories
    public LiveData<List<Category>> getCategoryList() {
        return oCategories;
    }

    public LiveData<Integer> getSelectedCategoryPosition() {
        return selectedCategoryPosition;
    }

    public void setSelectedCategoryPosition(int position) {
        selectedCategoryPosition.postValue(position);
    }

    public LiveData<List<Product>> getProducts() {
        return oProducts;
    }

    public LiveData<List<Product>> getRecommended() {
        return oRecommended;
    }

    public void makeProductRequest(String query) {
        String cat = query.trim();
        if (cat.isEmpty()) {
            cat = "all";
        }
        repository.searchProduct(cat);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (null != repository) {
            repository.cancelConnection();
        }
    }
}
