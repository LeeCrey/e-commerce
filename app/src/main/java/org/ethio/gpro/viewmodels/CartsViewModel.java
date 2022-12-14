package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.ethio.gpro.repositories.CartRepository;

public class CartsViewModel extends AndroidViewModel {
    private final CartRepository cartRepository;

    public CartsViewModel(@NonNull Application application) {
        super(application);

        cartRepository = new CartRepository(application);
    }
}
