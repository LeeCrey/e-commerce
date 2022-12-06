package org.ethio.gpro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ethio.gpro.models.Cart;
import org.ethio.gpro.repositories.CartRepository;

import java.util.List;

public class CartsViewModel extends AndroidViewModel {
    private final CartRepository cartRepository;

    public CartsViewModel(@NonNull Application application) {
        super(application);

        cartRepository = new CartRepository(application);
    }
}
