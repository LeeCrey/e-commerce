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

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.helpers.ProductHelper;
import org.ethio.gpro.viewmodels.ProductViewModel;

public class ProductFragment extends Fragment {
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";

    private Integer productId;
    private MainActivityCallBackInterface callBackInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        final ProductAdapter adapter = ProductHelper.initRecommendedProducts(requireActivity(), view.findViewById(R.id.recommended_products));

        // observers
        productViewModel.getRecommended().observe(getViewLifecycleOwner(), adapter::setProducts);

        Button addToCart = view.findViewById(R.id.add_to_cart);
        if (callBackInterface.getLoggedIn()) {
            addToCart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        productId = null;
        callBackInterface = null;
    }
}