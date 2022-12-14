package org.ethio.gpro.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ethio.gpro.databinding.LayoutCommentBinding;
import org.ethio.gpro.models.Comment;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private final LayoutCommentBinding binding;

    public CommentViewHolder(@NonNull LayoutCommentBinding _binding) {
        super(_binding.getRoot());

        binding = _binding;
    }

    public void bindView(@NonNull Comment comment) {
        binding.setComment(comment);
    }
}
