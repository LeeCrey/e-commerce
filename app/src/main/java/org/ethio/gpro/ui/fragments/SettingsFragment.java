package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;

public class SettingsFragment extends PreferenceFragmentCompat {
    private MainActivityCallBackInterface callBack;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callBack = (MainActivityCallBackInterface) requireActivity();

        // theme change handler
        ListPreference themePref = findPreference("theme_mode");
        if (null != themePref) {
            themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                callBack.onThemeChange(newValue.toString());
                return true;
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        callBack.hideBottomNavView();
    }
}