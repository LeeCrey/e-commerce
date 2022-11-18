package org.ethio.gpro.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.CartCallBackInterface;
import org.ethio.gpro.models.Cart;
import org.ethio.gpro.viewholders.CartViewHolder;

public class CartAdapter extends ListAdapter<Cart, CartViewHolder> {
    private static final DiffUtil.ItemCallback<Cart> CALL_BACK = new DiffUtil.ItemCallback<Cart>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };
    private static final int SHIMMER_SIZE = 3;
    private final LayoutInflater inflater;
    private final CartCallBackInterface callBackInterface;
    private boolean loadShimmer = true;

    public CartAdapter(Activity activity, CartCallBackInterface cartCallBackInterface) {
        super(CALL_BACK);

        this.callBackInterface = cartCallBackInterface;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.layout_cart, parent, false);

        CartViewHolder viewHolder = new CartViewHolder(view);

        if (loadShimmer) {
            return viewHolder;
        }

//        // on click listener...
        view.setOnClickListener(v -> callBackInterface.onCartClick(getItem(viewHolder.getAdapterPosition())));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (!loadShimmer) {
            holder.bindView(getItem(position));
        }
    }

    public void setLoadShimmer(boolean loadShimmer) {
        this.loadShimmer = loadShimmer;
    }
}
