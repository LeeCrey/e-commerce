package org.ethio.gpro.callbacks;

import android.view.View;

import androidx.annotation.NonNull;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.Product;

public interface MainActivityCallBackInterface {

    void hideBottomNavView();

    void showBottomNavView();

    void onThemeChange(String themeValue);

    void openRecommended(View view);

    void closeKeyBoard();

    String getAuthorizationToken();

    void setAuthToken(String authToken);

    void checkPermission();

    void logout();

    void setCurrentUser();

    void onProductClick(@NonNull Product product);

    void hiddeToolBar();

    void showToolBar();
}
