package org.ethio.gpro.callbacks;

import androidx.annotation.NonNull;

import org.ethio.gpro.models.Product;

public interface ProductCallBackInterface extends SearchCallBackInterface {
    boolean onCategorySelected(int position);

    void onProductClick(@NonNull Product product);
}
