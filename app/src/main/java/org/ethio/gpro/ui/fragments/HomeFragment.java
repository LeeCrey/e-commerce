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
import android.widget.Toast;

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
import org.ethio.gpro.models.Category;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.viewmodels.ProductViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements MenuProvider, ProductCallBackInterface {
    private final boolean loggedIn = true;
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
        viewModel.getCategoryList().observe(getViewLifecycleOwner(), this::submitCategoryList);
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> setDataToAdapter(1, products));
        viewModel.getRecommended().observe(getViewLifecycleOwner(), products -> setDataToAdapter(2, products));
        viewModel.getSelectedCategoryPosition().observe(getViewLifecycleOwner(), categoryAdapter::setSelectedCategoryPosition);

        // menu host
        if (!loggedIn) {
            requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        }
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

        categoriesTitle = null;
        categoryAdapter = null;

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
        if (loggedIn) {
            return;
        }

        menuInflater.inflate(R.menu.home_menu, menu);

        ProductHelper.registerSearchFunction(requireContext(), menu, this);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return NavigationUI.onNavDestinationSelected(menuItem, navController);
    }

    @Override
    public void productSearch(String query) {
        Toast.makeText(requireContext(), "THis is from home fragment", Toast.LENGTH_SHORT).show();
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

    private void submitCategoryList(List<Category> categories) {
        if (categories == null) {
            return;
        }
        categoriesRunnable = () -> requireActivity().runOnUiThread(() -> {
            categoryAdapter.setCategories(categories);
        });
        customHandler.postDelayed(categoriesRunnable, 2_000);
    }

    // flat 1 products in grid, 2 recommended
    private void setDataToAdapter(final int flag, final List<Product> products) {
        if (products == null) {
            return;
        }

        if (flag == 1) {
            productsRunnable = () -> requireActivity().runOnUiThread(() -> {
                productAdapter.setProducts(products);
            });
            customHandler.postDelayed(productsRunnable, 2_000);
        } else {
            recommendRunnable = () -> requireActivity().runOnUiThread(() -> {
                recommendedAdapter.setProducts(products);
            });
            customHandler.postDelayed(recommendRunnable, 3_000);
        }
    }
}
