package org.ethio.gpro.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.viewholders.ProductViewHolder;

import java.util.List;

public class ProductAdapter extends ListAdapter<Product, ProductViewHolder> {
    private static final DiffUtil.ItemCallback<Product> CALL_BACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.isContentTheSame(newItem);
        }
    };
    private final LayoutInflater inflater;
    private final Activity activity;
    private final Picasso picasso;
    private final int SHIMMER_SIZE = 8;
    private boolean loadShimmer = true;
    private MainActivityCallBackInterface callBack;
    private boolean calculateWidth;

    public ProductAdapter(Activity activity) {
        super(CALL_BACK);

        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_product, parent, false);

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.height = 450;

        // basically for recommended products
        if (calculateWidth) {
            params.width = 330;
        }

        final ProductViewHolder viewHolder = new ProductViewHolder(view, picasso);
        if (loadShimmer) {
            return viewHolder;
        }

        // on click listener...
        if (callBack != null) {
            view.setOnClickListener(v -> callBack.onProductClick(getItem(viewHolder.getAdapterPosition())));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (!loadShimmer) {
            holder.bindView(activity, getItem(position));
        }
    }

    @Override
    public int getItemCount() {
        if (loadShimmer) {
            return SHIMMER_SIZE;
        }
        return super.getItemCount();
    }

    public void setCalculateProductWidth(boolean b) {
        calculateWidth = b;
    }

    public void setCallBack(MainActivityCallBackInterface callBack) {
        this.callBack = callBack;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProducts(final List<Product> list) {
        if (list != null) {
            loadShimmer = false;
            getCurrentList().clear();
            notifyDataSetChanged();
            submitList(list);
        }
    }
}
