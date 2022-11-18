package org.ethio.gpro.viewholders;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.ethio.gpro.R;
import org.ethio.gpro.models.Product;


public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final ImageView productImage;
    private final Picasso picasso;
    private final TextView productName;
    private final TextView productPrice;
    private final RatingBar productRating;
    private final TextView oldPrice;
    private final ShimmerFrameLayout frameLayout;

    public ProductViewHolder(@NonNull View view, Picasso picasso) {
        super(view);

        productImage = view.findViewById(R.id.product_image);
        productName = view.findViewById(R.id.product_name);
        productPrice = view.findViewById(R.id.new_product_price);
        oldPrice = view.findViewById(R.id.old_product_price);
        productRating = view.findViewById(R.id.product_rating_bar);
        frameLayout = view.findViewById(R.id.shimmer_layout);
        this.picasso = picasso;
    }

    // if it is search layout attach only image.
    public void bindView(final Context context, final Product product) {
        String price = context.getString(R.string.price_in_ethio, product.getLastPrice());
//        String rates = " (" + product.getNumberOfRate() + ")";

        // remove bg color
        frameLayout.stopShimmer();
        frameLayout.setShimmer(null);
        productName.setBackground(null);
        productPrice.setBackground(null);
        productRating.setBackground(null);
        productImage.setBackground(null);

        productName.setText(product.getName());
        productPrice.setText(price);
        productRating.setRating(product.getRate());

        if (product.getDiscount() != null) {
            final String sp = String.valueOf(product.getPrice());
            SpannableString old = new SpannableString(sp);
            old.setSpan(new StrikethroughSpan(), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            oldPrice.setText(old);
            oldPrice.setVisibility(View.VISIBLE);
        }

        attachImage("https://t.me/hello.png");
    }

    private void attachImage(final String url) {
        picasso.load(url)
                .error(R.drawable.load_error)
                .into(productImage);
    }
}
