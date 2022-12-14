package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.ethio.gpro.repositories.RateRepository;

public class RateFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final int productId;
    private final String token;
    private final Application application;

    public RateFragmentViewModelFactory(@NonNull Application application, int productId, String token) {
        this.productId = productId;
        this.token = token;
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RateFragmentViewModel.class)) {
            RateRepository repository = new RateRepository(application, productId, token);
            return (T) new RateFragmentViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
