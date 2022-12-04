package org.ethio.gpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import org.ethio.gpro.R;
import org.ethio.gpro.models.Product;

import java.util.List;

public class TrendingAdapter extends PagerAdapter {
    private final LayoutInflater inflater;
    private List<Product> trendingProducts;

    public TrendingAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void updateList(List<Product> products) {
        if (!products.isEmpty()) {
            trendingProducts = products;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return trendingProducts == null ? 0 : trendingProducts.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.trending_product, container, false);
        bindView(view, position); // calling method
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    // private Methods
    private void bindView(View v, int pos) {
        Product product = trendingProducts.get(pos);
        ImageView productImage = v.findViewById(R.id.trending_product_image);
        TextView productName = v.findViewById(R.id.trending_product_name);
        productName.setText(product.getName());

        Picasso.get().load(product.getImages().get(0))
                .error(R.drawable.load_error)
                .into(productImage);
    }
}