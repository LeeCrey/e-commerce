package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
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

import com.facebook.shimmer.ShimmerFrameLayout;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.ProductAdapter;
import org.ethio.gpro.adapters.ProductImagesAdapter;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.databinding.FragmentDetailBinding;
import org.ethio.gpro.helpers.ProductHelper;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.viewmodels.ProductDetailFragmentViewModel;

public class DetailFragment extends Fragment {
    private MainActivityCallBackInterface callBackInterface;
    private FragmentDetailBinding binding;

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBackInterface = (MainActivityCallBackInterface) requireActivity();

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

        final int productId = DetailFragmentArgs.fromBundle(getArguments()).getProductId();
        // event ...
        addToCart.setOnClickListener(v -> Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_SHORT).show());
        binding.rateProduct.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putInt("productId", productId);
            navController.navigate(R.id.detail_to_rate_product, arg);
        });

        // observers
        thisViewModel.getProduct().observe(getViewLifecycleOwner(), this::setUiData);
        thisViewModel.getProducts().observe(getViewLifecycleOwner(), recommendedAdapter::setProducts);

        //api
        thisViewModel.makeShowApiRequest(productId);
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

        // origin
        ShimmerFrameLayout originShimmer = binding.productOriginShimmer;
        stopShimmer(originShimmer);
        TextView productOrigin = binding.productOrigin;
        productOrigin.setBackground(null);
        productOrigin.setText(product.getOrigin());

        // price
        ShimmerFrameLayout priceShimmer = binding.priceShimmer;
        stopShimmer(priceShimmer);
        String price = getString(R.string.price_in_ethio, product.getPrice());
        TextView priceView = binding.priceValue;
        priceView.setBackground(null);
        priceView.setText(price);

        // description
        ShimmerFrameLayout detailShimmer = binding.detailShimmer;
        stopShimmer(detailShimmer);
        TextView productDescription = binding.productDescription;
        productDescription.setBackground(null);
        productDescription.setText(product.getDescription());
        productDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, callBackInterface.getFontSizeForDescription());

        // rating
        ShimmerFrameLayout ratingShimmer = binding.ratingShimmer;
        stopShimmer(ratingShimmer);
        RatingBar productRate = binding.productRates;
        productRate.setBackground(null);
        productRate.setRating(product.getRate());

        // detail
        ProductImagesAdapter adapter = new ProductImagesAdapter(requireContext(), product.getImages());
        viewPager.setAdapter(adapter);

    }

    private void stopShimmer(ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setShimmer(null);
    }
}