package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.callbacks.SearchCallBackInterface;
import org.ethio.gpro.helpers.ProductHelper;
import org.ethio.gpro.viewmodels.SearchFragmentViewModel;

public class SearchFragment extends Fragment implements MenuProvider, SearchCallBackInterface {
    private MainActivityCallBackInterface callBackInterface;
    private ProductAdapter productAdapter;
    private SearchFragmentViewModel viewModel;
    private RecyclerView recyclerView;

    private Handler uiHandler;
    private Runnable uiRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final FragmentActivity activity = requireActivity();
        callBackInterface = (MainActivityCallBackInterface) activity;

        viewModel = new ViewModelProvider(this).get(SearchFragmentViewModel.class);
        callBackInterface = (MainActivityCallBackInterface) activity;

        initRecyclerView(view);

        viewModel.getProductList().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productAdapter.submitList(products);
            }
        });

        activity.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        showProducts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        uiHandler.removeCallbacks(uiRunnable);
        uiHandler = null;
        uiRunnable = null;

        viewModel = null;
        callBackInterface = null;
        productAdapter = null;
        recyclerView = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu);

        ProductHelper.registerSearchFunctionality(requireContext(), menu, this);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void productSearch(String query) {
        viewModel.searchProduct(query);
        callBackInterface.closeKeyBoard();
    }

    private void initRecyclerView(final View view) {
        recyclerView = view.findViewById(R.id.products_recycler_view);

        productAdapter = new ProductAdapter(requireActivity());

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(productAdapter);
        productAdapter.setCallBack(callBackInterface);
    }

    // delay some time
    private void showProducts() {
        uiRunnable = () -> {
            recyclerView.setVisibility(View.VISIBLE);
        };

        uiHandler = new Handler();
        uiHandler.postDelayed(uiRunnable, 1_000);
    }
}