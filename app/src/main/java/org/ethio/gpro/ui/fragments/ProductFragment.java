package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.adapters.ProductImagesAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.databinding.FragmentProductShowBinding;
import org.ethio.gpro.helpers.ProductHelper;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.viewmodels.ProductDetailFragmentViewModel;

public class ProductFragment extends Fragment {
    private MainActivityCallBackInterface callBackInterface;
    private FragmentProductShowBinding binding;

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductShowBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        callBackInterface.hiddeToolBar(); // hidde activity's toolbar

        // p-holders
        RecyclerView recommendedRecyclerView = binding.recommendedProducts;
        final Button addToCart = binding.addItemToCart;
        viewPager = binding.productImages;

        NavController navController = Navigation.findNavController(view);
        ProductDetailFragmentViewModel thisViewModel = new ViewModelProvider(this).get(ProductDetailFragmentViewModel.class);
        ProductAdapter recommendedAdapter = ProductHelper.initRecommendedProducts(this, recommendedRecyclerView);
        // set callback

        // config
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        if (callBackInterface.getAuthorizationToken() != null) {
            addToCart.setVisibility(View.VISIBLE);
        }

        // event ...
        addToCart.setOnClickListener(v -> Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_SHORT).show());

        // observers
        thisViewModel.getProduct().observe(getViewLifecycleOwner(), this::setUiData);
        thisViewModel.getProducts().observe(getViewLifecycleOwner(), recommendedAdapter::setProducts);

        //api
        thisViewModel.makeShowApiRequest(ProductFragmentArgs.fromBundle(getArguments()).getProductId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        callBackInterface = null;
        viewPager = null;
    }

    // custom methods

    private void setUiData(Product product) {
        if (product == null) {
            return;
        }

        binding.productOrigin.setText(product.getOrigin());
        String price = getString(R.string.price_in_ethio, product.getPrice());
        binding.priceValue.setText(price);
        binding.productDescription.setText(product.getDescription());
        binding.productRates.setRating(product.getRate());

        ProductImagesAdapter adapter = new ProductImagesAdapter(requireContext(), product.getImages());
        viewPager.setAdapter(adapter);
    }
}