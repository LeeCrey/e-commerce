package org.ethio.gpro.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.ethio.gpro.databinding.FragmentFeedbackBinding;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedbackFragment extends Fragment {
    private FragmentFeedbackBinding binding;

    private CircleImageView imageView;
    private TextInputEditText feedback;
    private TextView send;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedbackBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        feedback = binding.feedBack;
        imageView = binding.currentUserProfilePicture;
        send = binding.sendFeedback;
        // TODO
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        feedback = null;
        imageView = null;
        send = null;
    }
}
