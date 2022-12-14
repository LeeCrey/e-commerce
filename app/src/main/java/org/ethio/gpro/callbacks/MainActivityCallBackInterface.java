package org.ethio.gpro.callbacks;

import androidx.annotation.NonNull;

import org.ethio.gpro.models.Product;

public interface MainActivityCallBackInterface {
    void closeKeyBoard();

    String getAuthorizationToken();

    void checkPermission();

    void onProductClick(@NonNull Product product);

    int getScreenHeight();

    int getFontSizeForDescription();

    String getMapType();
}
