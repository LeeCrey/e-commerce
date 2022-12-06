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

import java.util.List;

public class CartsFragment extends Fragment implements MenuProvider, CartCallBackInterface {
    private CartAdapter cartAdapter;
    private CartsViewModel viewModel;
    private RecyclerView recyclerView;
    private NavController navController;
    private ItemTouchHelper touchHelper;

    private HandlerThread threadHandler;
    private Handler uiHandler;
    private Runnable uiRunnable;

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

//        touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            }
//        });

        // add menu host
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // thread handler
        threadHandler = new HandlerThread("uiHandler");
        threadHandler.start();
        uiHandler = new Handler(threadHandler.getLooper());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //
        threadHandler.quit();
        uiHandler.removeCallbacks(uiRunnable);
        threadHandler = null;
        uiHandler = null;
        uiRunnable = null;

        recyclerView = null;
        cartAdapter = null;
        viewModel = null;
        navController = null;
        touchHelper = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.carts_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        final int id = menuItem.getItemId();
        if (id == R.id.clear_carts) {
//            viewModel.deleteCarts();
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
        recyclerView.setAdapter(cartAdapter);
    }

    private void submitCarts(List<Cart> carts) {
        if (carts == null) {
            return;
        }

        uiRunnable = () -> requireActivity().runOnUiThread(() -> {
            cartAdapter.setCarts(carts);
            touchHelper.attachToRecyclerView(recyclerView);
        });

        uiHandler.postDelayed(uiRunnable, 1_000);
    }
}