package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.ethio.gpro.R;
import org.ethio.gpro.helpers.PreferenceHelper;
import org.ethio.gpro.viewmodels.account.SessionsViewModel;


public class ProfileFragment extends Fragment implements MenuProvider {
    private NavController navController;
    private SessionsViewModel sessionsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        sessionsViewModel = new ViewModelProvider(this).get(SessionsViewModel.class);

        //observers
        sessionsViewModel.getLogoutResult().observe(getViewLifecycleOwner(), sessionResponse -> {
            if (sessionResponse == null) {
                return;
            }

            PreferenceHelper.clearPref(requireActivity());
            navController.navigateUp();
        });
        // menu host
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        navController = null;
        sessionsViewModel = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        final int id = menuItem.getItemId();
        if (id == R.id.from_profile_to_settings) {
            navController.navigate(R.id.from_profile_to_settings);
        } else if (id == R.id.logout) {
            sessionsViewModel.logout(requireActivity());
        }
        return false;

//        return NavigationUI.onNavDestinationSelected(menuItem, navController);
    }
}