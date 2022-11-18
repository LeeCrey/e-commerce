package org.ethio.gpro.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.ethio.gpro.R;
import org.ethio.gpro.models.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "CategoryViewHolder";
    private final TextView categoryName;
    private final ShimmerFrameLayout shimmerFrameLayout;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.category_name);
        shimmerFrameLayout = itemView.findViewById(R.id.category_shimmer);
    }

    public TextView getCategoryName() {
        return categoryName;
    }

    public void bindView(Category category, Context context) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setShimmer(null);

        categoryName.setBackground(null);
        categoryName.setText(category.getName());

        if (category.isSelected()) {
            categoryName.setBackgroundColor(context.getColor(R.color.primary));
            categoryName.setTextColor(context.getColor(R.color.onPrimary));
        } else {
            categoryName.setBackgroundColor(context.getColor(R.color.category));
            categoryName.setTextColor(context.getColor(R.color.textPrimaryColor));
        }
    }
}
