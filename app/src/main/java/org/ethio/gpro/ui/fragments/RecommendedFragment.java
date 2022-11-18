package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.viewmodels.RecommendedViewModel;


public class RecommendedFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecommendedViewModel recommendedViewModel;
    private MainActivityCallBackInterface callBackInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommended, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recommendedViewModel = new ViewModelProvider(this).get(RecommendedViewModel.class);
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        recyclerView = view.findViewById(R.id.recommended_products);
    }

    @Override
    public void onResume() {
        super.onResume();

        callBackInterface.hideBottomNavView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        callBackInterface = null;
        recommendedViewModel = null;
        recyclerView = null;
    }
}