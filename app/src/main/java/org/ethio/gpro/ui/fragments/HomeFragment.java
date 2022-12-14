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
import androidx.viewpager.widget.ViewPager;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.CategoryAdapter;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.adapters.TrendingAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.callbacks.ProductCallBackInterface;
import org.ethio.gpro.databinding.FragmentHomeBinding;
import org.ethio.gpro.helpers.ProductHelper;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.viewmodels.HomeFragmentViewModel;

public class HomeFragment extends Fragment implements MenuProvider, ProductCallBackInterface {
    private SwipeRefreshLayout swipeRefreshLayout;
    private HomeFragmentViewModel viewModel;
    private MainActivityCallBackInterface callBack;
    private RecyclerView productRecyclerView, categoryRecyclerView, recommendedRecyclerView;
    private ViewPager trendingProducts;
    private TextView recommended, seeAll, categoriesTitle;
    private NavController navController;
    //
    private Runnable categoriesRunnable, productsRunnable, recommendRunnable;
    private Handler customHandler;
    private HandlerThread handlerThread;

    /* Adapters  */
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter, recommendedAdapter;
    private TrendingAdapter trendingAdapter;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBack = (MainActivityCallBackInterface) requireActivity();
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        navController = Navigation.findNavController(view);

        //
        productRecyclerView = binding.productsRecyclerView;
        swipeRefreshLayout = binding.refreshLayout;
        recommended = binding.titleRecommended;
        categoriesTitle = binding.categoryListText;
        recommendedRecyclerView = binding.recommendedProducts;
        seeAll = binding.seeAllRecommendedProducts;

        // init ui
        initCategories();
        initTrending();
        productAdapter = ProductHelper.initProducts(this, productRecyclerView, true, false);
        productAdapter.setCallBack(this);
        productAdapter.setCalculateProductWidth(false);
        productAdapter.setHeight(callBack.getScreenHeight());
        recommendedAdapter = ProductHelper.initRecommendedProducts(this, recommendedRecyclerView);
        recommendedAdapter.setCallBack(this);
        recommendedAdapter.setCalculateProductWidth(true);

        // event list...
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.makeProductRequest(categoryAdapter.getSelectedCategoryName());
            viewModel.makeCategoryRequest();
            swipeRefreshLayout.setRefreshing(false);
        });
        seeAll.setOnClickListener(v -> navController.navigate(R.id.open_recommended_products));

        // handlers
        handlerThread = new HandlerThread("customUiHandler");
        handlerThread.start();
        customHandler = new Handler(handlerThread.getLooper());

        // observers
        viewModel.getCategoryList().observe(getViewLifecycleOwner(), categoryAdapter::setCategories);
        viewModel.getProducts().observe(getViewLifecycleOwner(), productAdapter::setProducts);
        viewModel.getRecommended().observe(getViewLifecycleOwner(), recommendedAdapter::setProducts);
        viewModel.getTrending().observe(getViewLifecycleOwner(), products -> {
            if (products == null) {
                return;
            }
            if (products.isEmpty()) {
                trendingProducts.setVisibility(View.GONE);
            }
            trendingAdapter.updateList(products);
        });
        viewModel.getSelectedCategoryPosition().observe(getViewLifecycleOwner(), this::sendProductRequest);

        // menu host
        if (callBack.getAuthorizationToken() == null) {
            requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        }

        showRecyclerViewsWithDelay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;

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
        trendingAdapter = null;

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
        // when ever re-click
        if (position != categoryAdapter.getSelectedCategoryPosition()) {
            viewModel.setSelectedCategoryPosition(position);
        }

        return true;
    }

    @Override
    public void onProductClick(@NonNull Product product) {
        viewModel.setCurrentProduct(product);
        callBack.onProductClick(product); // do the navigation part
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if (callBack.getAuthorizationToken() != null) {
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
    private void sendProductRequest(Integer position) {
        String cat = "all";
        if (position != -1) {
            categoryAdapter.setSelectedCategoryPosition(position);
            cat = categoryAdapter.getSelectedCategoryName();
        }
        viewModel.makeProductRequest(cat);
    }

    private void initCategories() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext());
        linearLayout.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView = binding.categoryList;
        categoryRecyclerView.setLayoutManager(linearLayout);

        categoryAdapter = new CategoryAdapter(requireActivity());
        categoryAdapter.setCallBack(this);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void initTrending() {
        trendingProducts = binding.trendingProductList;
        trendingProducts.setPadding(20, 0, 20, 0);
        trendingAdapter = new TrendingAdapter(requireContext());
        trendingProducts.setAdapter(trendingAdapter);
    }

    private void showRecyclerViewsWithDelay() {
        //categories
        categoriesRunnable = () -> requireActivity().runOnUiThread(() -> {
            categoriesTitle.setVisibility(View.VISIBLE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
        });
        customHandler.postDelayed(categoriesRunnable, 500);

        // products
        productsRunnable = () -> requireActivity().runOnUiThread(() -> productRecyclerView.setVisibility(View.VISIBLE));
        customHandler.postDelayed(productsRunnable, 1_000);

        recommendRunnable = () -> requireActivity().runOnUiThread(() -> {
            recommendedRecyclerView.setVisibility(View.VISIBLE);
            seeAll.setVisibility(View.VISIBLE);
            recommended.setVisibility(View.VISIBLE);
        });
        customHandler.postDelayed(recommendRunnable, 2_000);
    }
}
