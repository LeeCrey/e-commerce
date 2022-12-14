package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.CommentAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.viewmodels.RateFragmentViewModel;
import org.ethio.gpro.viewmodels.RateFragmentViewModelFactory;

public class RateFragment extends BottomSheetDialogFragment {
    private CommentAdapter adapter;
    private Integer productId;
    private MainActivityCallBackInterface callBackInterface;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComments(view);

        productId = RateFragmentArgs.fromBundle(getArguments()).getProductId();
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        NavController navController = Navigation.findNavController(view);
        RateFragmentViewModel viewModel = new ViewModelProvider(
                this, new RateFragmentViewModelFactory(requireActivity().getApplication(), productId, callBackInterface.getAuthorizationToken()))
                .get(RateFragmentViewModel.class);

        // observer
        viewModel.getCommentList().observe(getViewLifecycleOwner(), adapter::updateList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        adapter = null;
        callBackInterface = null;
        productId = null;
    }

    private void initComments(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_comments);
        adapter = new CommentAdapter(requireActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
}