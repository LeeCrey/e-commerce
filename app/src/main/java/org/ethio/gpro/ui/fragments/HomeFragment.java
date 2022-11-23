package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.CategoryAdapter;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.callbacks.ProductCallBackInterface;
import org.ethio.gpro.helpers.ProductHelper;
import org.ethio.gpro.viewmodels.ProductViewModel;

public class HomeFragment extends Fragment implements MenuProvider, ProductCallBackInterface {
    private final boolean loggedIn = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProductViewModel viewModel;
    private MainActivityCallBackInterface callBack;
    private RecyclerView productRecyclerView, categoryRecyclerView, recommendedRecyclerView;
    private TextView recommended, seeAll, categoriesTitle;
    private NavController navController;

    //
    private Runnable categoriesRunnable, productsRunnable, recommendRunnable;
    private Handler customHandler;
    private HandlerThread handlerThread;

    /* Adapters  */
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter, recommendedAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBack = (MainActivityCallBackInterface) requireActivity();
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        navController = Navigation.findNavController(view);

        //
        recommendedRecyclerView = view.findViewById(R.id.recommended_products);
        productRecyclerView = view.findViewById(R.id.products_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        recommended = view.findViewById(R.id.title_recommended);
        seeAll = view.findViewById(R.id.see_all_recommended_products);
        categoriesTitle = view.findViewById(R.id.category_list_text);

        initCategories(view);
        productAdapter = ProductHelper.initProducts(view, requireActivity(), productRecyclerView, true);
        recommendedAdapter = ProductHelper.initProducts(view, requireActivity(), recommendedRecyclerView, false);

        // event list...
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
        seeAll.setOnClickListener(callBack::openRecommended);

        // handlers
        handlerThread = new HandlerThread("customUiHandler");
        handlerThread.start();
        customHandler = new Handler(handlerThread.getLooper());

        // observers
        viewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null) {
                return;
            }
            categoryAdapter.setCategories(categories);
        });
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products == null) {
                return;
            }
            productAdapter.setProducts(products);
        });
        viewModel.getRecommended().observe(getViewLifecycleOwner(), products -> {
            if (products == null) {
                return;
            }
            recommendedAdapter.setProducts(products);
        });
        viewModel.getSelectedCategoryPosition().observe(getViewLifecycleOwner(), categoryAdapter::setSelectedCategoryPosition);

        // menu host
        if (!callBack.getLoggedIn()) {
            requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        }

        showRecyclerViewsWithDelay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /* stop handler  */
        handlerThread.quit();
        customHandler.removeCallbacks(categoriesRunnable);
        customHandler.removeCallbacks(productsRunnable);
        customHandler.removeCallbacks(recommendRunnable);

        handlerThread = null;
        customHandler = null;
        productsRunnable = null;
        categoriesRunnable = null;

        categoryAdapter = null;
        productAdapter = null;
        recommendedAdapter = null;

        categoriesTitle = null;
        categoryRecyclerView = null;
        productRecyclerView = null;
        recommendedRecyclerView = null;
        recommended = null;
        seeAll = null;

        navController = null;
        swipeRefreshLayout = null;
        viewModel = null;
        callBack = null;
    }

    @Override
    public boolean onCategorySelected(int position) {
        viewModel.setSelectedCategoryPosition(position);

        return true;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if (callBack.getLoggedIn()) {
            return;
        }

        menuInflater.inflate(R.menu.home_menu, menu);

        ProductHelper.registerSearchFunctionality(requireContext(), menu, this);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return NavigationUI.onNavDestinationSelected(menuItem, navController);
    }

    @Override
    public void productSearch(String query) {
        viewModel.makeProductRequest(query);
    }

    // custom
    private void initCategories(final View view) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext());
        linearLayout.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView = view.findViewById(R.id.category_list);
        categoryRecyclerView.setLayoutManager(linearLayout);

        categoryAdapter = new CategoryAdapter(requireActivity());
        categoryAdapter.setCallBack(this);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void showRecyclerViewsWithDelay() {
        //categories
        categoriesRunnable = () -> requireActivity().runOnUiThread(() -> {
            categoriesTitle.setVisibility(View.VISIBLE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
        });
        customHandler.postDelayed(categoriesRunnable, 500);

        // products
        productsRunnable = () -> requireActivity().runOnUiThread(() -> {
            productRecyclerView.setVisibility(View.VISIBLE);
        });
        customHandler.postDelayed(productsRunnable, 1_000);

        // recommended
        recommendRunnable = () -> requireActivity().runOnUiThread(() -> {
            seeAll.setVisibility(View.VISIBLE);
            recommended.setVisibility(View.VISIBLE);
            recommendedRecyclerView.setVisibility(View.VISIBLE);
        });
        customHandler.postDelayed(recommendRunnable, 2_000);
    }
}
