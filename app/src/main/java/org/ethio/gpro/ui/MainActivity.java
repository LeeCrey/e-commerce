package org.ethio.gpro.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.databinding.ActivityMainBinding;
import org.ethio.gpro.helpers.ApplicationHelper;
import org.ethio.gpro.helpers.PreferenceHelper;
import org.ethio.gpro.models.Product;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements MainActivityCallBackInterface {
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private String authToken = null;

    private View headerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authToken = PreferenceHelper.getAuthToken(this);

        // theme
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String darkTheme = preferences.getString("theme_mode", "off");
        onThemeChange(darkTheme);

        toolbar = binding.toolBar;
        setSupportActionBar(toolbar);

        bottomNavigationView = binding.bottomNavView;

        final DrawerLayout drawerLayout = binding.drawerLayout;
        final NavigationView navigationView = binding.navView;

        // top level des...
        final int home = R.id.navigation_home;
        final int carts = R.id.navigation_carts;
        final int profile = R.id.navigation_profile;
        final int search = R.id.navigation_search;

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        appBarConfiguration = new AppBarConfiguration.Builder(home, carts, profile, search).setOpenableLayout(drawerLayout).build();

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        checkPermission();

        // event listener ...
        navigationView.setNavigationItemSelectedListener(item -> {
            final int id = item.getItemId();
            if (id == R.id.menu_item_rate_app) {
                rateApp();
            } else if (id == R.id.menu_item_share) {
                shareApp();
            } else {
                NavigationUI.onNavDestinationSelected(item, navController);
            }
            drawerLayout.close();
            return false;
        });

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case home:
                case carts:
                case profile:
                case search:
                    if (authToken != null) {
                        showBottomNavView();
                    }
                    break;
                default:
                    hideBottomNavView();
            }
        });

        //
        headerView = navigationView.getHeaderView(0);

        // rate
//        AppRate.with(this)
//                .setInstallDays(1)
//                .setLaunchTimes(3)
//                .setRemindInterval(1)
//                .monitor();
//        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void hideBottomNavView() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void showBottomNavView() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onThemeChange(final String themeValue) {
        if (themeValue.equals("auto")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (themeValue.equals("on")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void openRecommended(View view) {
        navController.navigate(R.id.open_recommended_products);
    }

    @Override
    public void closeKeyBoard() {
        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public String getAuthorizationToken() {
        return authToken;
    }

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void checkPermission() {
        // permissions
        if (!ApplicationHelper.isInternetAccessGranted(this)) {
            ApplicationHelper.requestInternetAccessPermission(this);
        }
        if (!ApplicationHelper.isLocationAccessGranted(this)) {
            ApplicationHelper.requestLocationAccessPermission(this);
        }
    }

    @Override
    public void logout() {
        PreferenceHelper.clearPref(this);
        authToken = null;
        navController.navigateUp();
        hideBottomNavView();
    }

    @Override
    public void setCurrentUser() {
        String fullName = PreferenceHelper.getFullName(this);
        String msg = "Welcome back!";

        TextView name = headerView.findViewById(R.id.userFullName);
        TextView msgView = headerView.findViewById(R.id.message);

        if (fullName == null) {
            fullName = "Guest";
            msg = "Sign in to continue";
        }

        name.setText(fullName);
        msgView.setText(msg);
    }

    @Override
    public void onProductClick(@NonNull Product product) {
        final Bundle args = new Bundle();
        args.putString("productName", product.getName());
        args.putInt("productId", product.getId());
        navController.navigate(R.id.show_product, args);
    }

    @Override
    public void hiddeToolBar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolBar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    private void shareApp() {
        final String appUri = getString(R.string.app_play_store_url);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appUri);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void rateApp() {
        final String pkgName = "org.ethio.gpro";
        final String appStoreUrl = "https://play.google.com/store/apps/details?id=" + pkgName;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appStoreUrl);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,appStoreUrl));
    }
}
