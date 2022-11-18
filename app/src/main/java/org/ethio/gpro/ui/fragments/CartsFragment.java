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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ethio.gpro.R;
import org.ethio.gpro.adapters.CartAdapter;
import org.ethio.gpro.callbacks.CartCallBackInterface;
import org.ethio.gpro.models.Cart;
import org.ethio.gpro.viewmodels.CartsViewModel;

public class CartsFragment extends Fragment implements MenuProvider, CartCallBackInterface {
    private CartAdapter cartAdapter;
    private CartsViewModel viewModel;
    private RecyclerView recyclerView;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(CartsViewModel.class);

        initRecyclerView(view);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.deleteCart(viewHolder.getAdapterPosition());
            }
        });

        viewModel.getCarts().observe(getViewLifecycleOwner(), carts -> {
//            if (carts != null) {
//                new Handler().postDelayed(() -> {
//                    cartAdapter.setLoadShimmer(false);
//                    cartAdapter.submitList(carts);
//                    helper.attachToRecyclerView(recyclerView);
//                }, 1_000);
//            }
        });

        // add menu host
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView = null;
        cartAdapter = null;
        viewModel = null;
        navController = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.carts_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.clear_carts) {
            viewModel.deleteCarts();
        }
        return false;
    }

    @Override
    public void onCartClick(Cart cart) {
        Bundle arg = new Bundle();
        arg.putString("shopName", cart.getName());
        navController.navigate(R.id.show_cart, arg);
    }

    private void initRecyclerView(View view) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireActivity());
        recyclerView = view.findViewById(R.id.carts_recycler_view);
        recyclerView.setLayoutManager(manager);

        cartAdapter = new CartAdapter(requireActivity(), this);
        cartAdapter.setLoadShimmer(true);
//        cartAdapter.setHasStableIds(true);
        recyclerView.setAdapter(cartAdapter);
    }
}