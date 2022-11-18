package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.viewmodels.ProductViewModel;

public class ProductFragment extends Fragment {
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String LOGGED_IN = "logged_in";
    private boolean loggedIn = false;
    private Integer productId;

    private MainActivityCallBackInterface callBackInterface;
    private RecyclerView recommendedRecyclerView;
    private ProductAdapter recommendedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (null != args) {
            productId = args.getInt(PRODUCT_ID, -1);
            loggedIn = args.getBoolean(LOGGED_IN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callBackInterface = (MainActivityCallBackInterface) requireActivity();

        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        initRecommended(view);

        productViewModel.getRecommended().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                recommendedAdapter.submitList(products);
            }
        });

        Button addToCart = view.findViewById(R.id.add_to_cart);
        if (loggedIn) {
            addToCart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recommendedAdapter = null;
        recommendedRecyclerView = null;
        productId = null;
    }

    private void initRecommended(final View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recommendedRecyclerView = view.findViewById(R.id.recommended_products);
        recommendedRecyclerView.setNestedScrollingEnabled(false);
        recommendedRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recommendedRecyclerView.setLayoutManager(layoutManager);

        recommendedAdapter = new ProductAdapter(requireActivity());
        recommendedAdapter.setCalculateProductWidth(true);
//        recommendedAdapter.setCallBack(callBackInterface);

        recommendedRecyclerView.setAdapter(recommendedAdapter);
    }
}