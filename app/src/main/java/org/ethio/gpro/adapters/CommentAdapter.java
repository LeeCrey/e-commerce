package org.ethio.gpro.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.ethio.gpro.R;
import org.ethio.gpro.databinding.LayoutCommentBinding;
import org.ethio.gpro.models.Comment;
import org.ethio.gpro.viewholders.CommentViewHolder;

import java.util.List;

public class CommentAdapter extends ListAdapter<Comment, CommentViewHolder> {
    private static final String TAG = "CommentAdapter";
    private static final DiffUtil.ItemCallback<Comment> CALL_BACK = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            int firstId = oldItem.getId();
            int lastId = newItem.getId();
            return firstId == lastId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.hasTheSameContent(newItem);
        }
    };

    private final LayoutInflater inflater;

    public CommentAdapter(@NonNull FragmentActivity activity) {
        super(CALL_BACK);

        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCommentBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_comment, parent, false);

        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }

    public void updateList(List<Comment> comments) {
        if (comments == null) {
            return;
        }

        submitList(comments);
    }
}
