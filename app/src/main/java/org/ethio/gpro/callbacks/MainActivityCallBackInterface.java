package org.ethio.gpro.callbacks;

import android.view.View;

import org.ethio.gpro.models.Product;

public interface MainActivityCallBackInterface {

    void hideBottomNavView();

    void showBottomNavView();

    void onThemeChange(String themeValue);

    void openRecommended(View view);

    void closeKeyBoard();

    void onProductClick(Product product);

    String getAuthorizationToken();

    void checkPermission();
}
