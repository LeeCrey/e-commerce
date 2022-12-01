package org.ethio.gpro.ui.fragments.account;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.ethio.gpro.R;

public class ConfirmationsFragment extends UnlocksFragment {
    private String confirmationToken = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            confirmationToken = args.getString("confirmationToken");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.confirmAccount.setVisibility(View.GONE);
        binding.headerForInstruction.setText(R.string.not_confirmed_yet);
        binding.textView2.setText(R.string.confirm_instruction);

        if (confirmationToken != null) {
            binding.emailLayout.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        confirmationToken = null;
    }
}