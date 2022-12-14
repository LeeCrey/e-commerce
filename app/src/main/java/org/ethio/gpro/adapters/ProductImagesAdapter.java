package org.ethio.gpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import org.ethio.gpro.R;

import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {
    private final LayoutInflater inflater;
    private final List<String> imagesUrl;

    public ProductImagesAdapter(@NonNull Context context, @NonNull List<String> urls) {
        inflater = LayoutInflater.from(context);
        imagesUrl = urls;
    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.layout_product_image, container, false);
        ImageView imageView = itemView.findViewById(R.id.product_image);
        Picasso.get().load(imagesUrl.get(position))
                .into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
