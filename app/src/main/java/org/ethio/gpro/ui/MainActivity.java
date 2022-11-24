package org.ethio.gpro.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
import org.ethio.gpro.models.Product;
import org.ethio.gpro.ui.fragments.ProductFragment;

public class MainActivity extends AppCompatActivity implements MainActivityCallBackInterface {
    private final boolean loggedIn = false;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // theme
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String darkTheme = preferences.getString("theme_mode", "off");
        onThemeChange(darkTheme);

        final Toolbar toolbar = binding.toolBar;
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

        // permissions
        if (!ApplicationHelper.isInternetAccessGranted(this)) {
            ApplicationHelper.requestInternetAccessPermission(this);
        }
        if (!ApplicationHelper.isLocationAccessGranted(this)) {
            ApplicationHelper.requestLocationAccessPermission(this);
        }

        // event listener ...
        navigationView.setNavigationItemSelectedListener(item -> {
            final int id = item.getItemId();
            if (id == R.id.menu_item_rate_app) {
                Toast.makeText(this, "rate app", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_item_share) {
                Toast.makeText(this, "share app", Toast.LENGTH_SHORT).show();
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
                    if (loggedIn) {
                        showBottomNavView();
                    }
                    break;
                default:
                    hideBottomNavView();
            }
        });
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
        try {
            // hide keyboard
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ignore) {
            // when a keyboard is already closed
        }
    }

    @Override
    public void onProductClick(Product product) {
        final Bundle args = new Bundle();
        args.putInt(ProductFragment.PRODUCT_ID, product.getId());
        args.putString(ProductFragment.PRODUCT_NAME, product.getName());
        navController.navigate(R.id.show_product, args);
    }

    @Override
    public boolean getLoggedIn() {
        return loggedIn;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        inputMethodManager = null;
    }
}
