package org.ethio.gpro.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.ethio.gpro.R;
import org.ethio.gpro.models.Cart;

public class CartViewHolder extends RecyclerView.ViewHolder {
    private final TextView cartName;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartName = itemView.findViewById(R.id.cart_name);
    }

    public void bindView(Cart item) {
        cartName.setText(item.getName());
    }
}
