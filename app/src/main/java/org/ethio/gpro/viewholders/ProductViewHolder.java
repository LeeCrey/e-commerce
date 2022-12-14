package org.ethio.gpro.viewholders;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.ethio.gpro.R;
import org.ethio.gpro.databinding.LayoutProductBinding;
import org.ethio.gpro.models.Product;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final LayoutProductBinding binding;

    public ProductViewHolder(@NonNull LayoutProductBinding _binding) {
        super(_binding.getRoot());

        binding = _binding;
    }

    @BindingAdapter("productImage")
    public static void setProductImage(@NonNull ImageView view, @NonNull String url) {
        Picasso.get()
                .load(url)
                .error(R.drawable.load_error)
                .into(view);
    }

    @BindingAdapter("setStrikeThroughText")
    public static void makeStrikeThrough(@NonNull TextView textView, Product product) {
        if (product == null) {
            return;
        }

        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (product.getDiscount() != null) {
            String oldP = String.valueOf(product.getPrice() - product.getDiscount());
            textView.setText(oldP);
        }
    }

    // if it is search layout attach only image.
    public void bindView(final Context context, final Product product) {
        binding.setProduct(product);
        binding.setPrice(context.getString(R.string.price_in_ethio, product.getPrice()));
    }
}
