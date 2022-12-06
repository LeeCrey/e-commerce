package org.ethio.gpro.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.ProductCallBackInterface;
import org.ethio.gpro.models.Product;
import org.ethio.gpro.viewholders.ProductViewHolder;

import java.util.List;

public class ProductAdapter extends ListAdapter<Product, ProductViewHolder> {
    private static final String TAG = "ProductAdapter";
    private static final DiffUtil.ItemCallback<Product> CALL_BACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            final int oldItemId = oldItem.getId();
            final int newItemId = newItem.getId();
            return oldItemId == newItemId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.isContentTheSame(newItem);
        }
    };
    private final LayoutInflater inflater;
    private final Activity activity;
    private final Picasso picasso;
    private ProductCallBackInterface callBack;
    private boolean calculateWidth;

    public ProductAdapter(Fragment activity) {
        super(CALL_BACK);

        this.activity = activity.getActivity();
        inflater = LayoutInflater.from(this.activity);
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_product, parent, false);

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        // basically for recommended products
        if (calculateWidth) {
            params.width = 330;
        }

        final ProductViewHolder viewHolder = new ProductViewHolder(view, picasso);

        // on click listener...
        if (callBack != null) {
            view.setOnClickListener(v -> callBack.onProductClick(getItem(viewHolder.getAdapterPosition())));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bindView(activity, getItem(position));
    }

    public void setCalculateProductWidth(boolean b) {
        calculateWidth = b;
    }

    public void setCallBack(ProductCallBackInterface callBack) {
        this.callBack = callBack;
    }

    public void setProducts(final List<Product> list) {
        if (list == null) {
            return;
        }

        submitList(list);
    }
}
