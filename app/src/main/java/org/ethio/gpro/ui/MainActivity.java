package org.ethio.gpro.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MenuItem;
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
import org.ethio.gpro.helpers.LocaleHelper;
import org.ethio.gpro.helpers.PreferenceHelper;
import org.ethio.gpro.models.Product;

public class MainActivity extends AppCompatActivity implements MainActivityCallBackInterface, LocationListener {
    private final String authTokenKey = "auth_token";
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private String authToken = null;
    private View headerView;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private SharedPreferences.OnSharedPreferenceChangeListener customListener;

    private String mapType;
    private int height;
    private SharedPreferences preferences;

    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Toolbar toolbar = binding.toolBar;
        final BottomNavigationView bottomNavigationView = binding.bottomNavView;
        final DrawerLayout drawerLayout = binding.drawerLayout;
        final NavigationView navigationView = binding.navView;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences pref = PreferenceHelper.getSharePref(this);

        // listeners
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("theme_mode")) {
                    onThemeChange(sharedPreferences);
                } else if (key.equals("language")) {
                    // set lang first then recreate activity
                    LocaleHelper.setLocale(MainActivity.this, preferences.getString(key, "en"));
                    recreate();
                }
            }
        };
        customListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key == null) {
                    authToken = null;
                } else {
                    if (key.equals(authTokenKey)) {
                        authToken = sharedPreferences.getString(key, null);
                    }
                }
                setCurrentUser();
            }
        };

        //
        authToken = pref.getString(authTokenKey, null);
        setSupportActionBar(toolbar);
        onThemeChange(preferences);

        // top level des...
        final int home = R.id.navigation_home;
        final int carts = R.id.navigation_carts;
        final int profile = R.id.navigation_profile;
        final int search = R.id.navigation_search;
        final int productPage = R.id.navigation_product_detail;
        final int rateProduct = R.id.navigation_rate;
        final int location = R.id.navigation_location;

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        appBarConfiguration = new AppBarConfiguration.Builder(home, carts, profile, search).setOpenableLayout(drawerLayout).build();

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        // event handlers ...
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
                    toolbar.setVisibility(View.VISIBLE);
                case carts:
                case profile:
                case search:
                    // show bottom navigation if and only if there is authorization token
                    // which means use is logged in
                    if (authToken == null) {
                        bottomNavigationView.setVisibility(View.GONE);
                    } else {
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                    break;
                case productPage:
                    toolbar.setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.GONE); // redundant
                    break;
                case rateProduct:
                case location:
                    toolbar.setVisibility(View.VISIBLE);
                default:
                    bottomNavigationView.setVisibility(View.GONE);
            }
        });
        preferences.registerOnSharedPreferenceChangeListener(listener);
        pref.registerOnSharedPreferenceChangeListener(customListener);

        //
        headerView = navigationView.getHeaderView(0);
        MenuItem menuItem = navigationView.getMenu().getItem(1);
        checkPermission();
        setCurrentUser();


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled) {
            enableLocationSettings();
        }

        if (hasNetwork) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            if (ApplicationHelper.isLocationAccessGranted(this)) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                ApplicationHelper.requestLocationAccessPermission(this);
            }

            if (currentLocation != null) {
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                Toast.makeText(MainActivity.this, "lat: " + latitude + ", long: " + longitude,
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
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
    public void onProductClick(@NonNull Product product) {
        final Bundle args = new Bundle();
        args.putString("productName", product.getName());
        args.putInt("productId", product.getId());
        navController.navigate(R.id.show_product, args);
    }

    @Override
    public int getScreenHeight() {
        return height;
    }

    @Override
    public int getFontSizeForDescription() {
        return preferences.getInt("font_size", 16);
    }

    @Override
    public String getMapType() {
        return mapType;
    }

    // custom methods
    private void setCurrentUser() {
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

    private void onThemeChange(SharedPreferences preferences) {
        String themeValue = preferences.getString("theme_mode", "auto");

        if (themeValue.equals("auto")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (themeValue.equals("on")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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
        startActivity(Intent.createChooser(sendIntent, appStoreUrl));
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
