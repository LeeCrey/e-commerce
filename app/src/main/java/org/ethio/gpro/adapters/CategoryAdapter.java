package org.ethio.gpro.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.ProductCallBackInterface;
import org.ethio.gpro.models.Category;
import org.ethio.gpro.viewholders.CategoryViewHolder;

import java.util.List;

public class CategoryAdapter extends ListAdapter<Category, CategoryViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private static final DiffUtil.ItemCallback<Category> DIFF_CALC_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getName().equals(newItem.getName()) && (oldItem.isSelected() == newItem.isSelected());
        }
    };
    private final int SHIMMER_SIZE = 9;
    private final LayoutInflater inflater;
    private final Context context;
    private boolean loadShimmer = true;
    private int selectedCategoryPosition;
    private ProductCallBackInterface callBackInterface;

    public CategoryAdapter(final Activity activity) {
        super(DIFF_CALC_CALLBACK);

        inflater = LayoutInflater.from(activity);
        context = activity.getApplicationContext();
        selectedCategoryPosition = 0;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.layout_category, parent, false);

        final CategoryViewHolder vh = new CategoryViewHolder(view);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        if (loadShimmer) {
            params.width = 50;
            return vh;
        }
        params.width = RecyclerView.LayoutParams.WRAP_CONTENT;

        // event ...
        final MaterialCardView cardView = (MaterialCardView) view;
//        cardView.setOnLongClickListener(v -> callBackInterface.onCategorySelected(vh.getAdapterPosition()));
        cardView.setOnClickListener(v -> callBackInterface.onCategorySelected(vh.getAdapterPosition()));

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (!loadShimmer) {
            holder.bindView(getItem(position), context);
        }
    }

    @Override
    public int getItemCount() {
        if (loadShimmer) {
            return SHIMMER_SIZE;
        }
        return super.getItemCount();
    }

    public void setSelectedCategoryPosition(final @NonNull Integer position) {
        if (position == -1) {
            return;
        }

        Category category = getItem(position);
        category.setSelected(true);

        notifyItemChanged(position);

        // old
        Category oldCategory = getItem(selectedCategoryPosition);
        oldCategory.setSelected(false);

        notifyItemChanged(position, category);
        notifyItemChanged(selectedCategoryPosition, oldCategory);

        selectedCategoryPosition = position;
    }

    public void setCallBack(ProductCallBackInterface callBack) {
        callBackInterface = callBack;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(final List<Category> list) {
        loadShimmer = false;
//        getCurrentList().clear();
        notifyDataSetChanged(); // reset adapter position
        submitList(list);
    }
}
