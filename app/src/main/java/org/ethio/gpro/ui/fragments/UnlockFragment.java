package org.ethio.gpro.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.helpers.ApplicationHelper;
import org.ethio.gpro.viewmodels.account.InstructionsViewModel;

import java.util.Objects;

public class UnlockFragment extends Fragment {
    public final static String EXTRA = "isUnlock";
    private MainActivityCallBackInterface callBackInterface;
    private Boolean isUnlock;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUnlock = bundle.getBoolean(EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callBackInterface = (MainActivityCallBackInterface) requireContext();
        InstructionsViewModel viewModel = new ViewModelProvider(this).get(InstructionsViewModel.class);

        //
        Button buttonSend = view.findViewById(R.id.send_instructions);
        TextInputEditText email = view.findViewById(R.id.email);
        ProgressBar loading = view.findViewById(R.id.progress_circular);

        if (!isUnlock) {
            TextView header = view.findViewById(R.id.header_for_instruction);
            header.setText(R.string.forgot_password);
        }

        // observers
        viewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            if (instructionsResponse.getOkay()) {
            }
        });

        // event
        ApplicationHelper.initEmailWatcher(requireContext(), email, buttonSend);
        buttonSend.setOnClickListener(v -> {
            callBackInterface.closeKeyBoard();
            if (ApplicationHelper.isInternetAccessGranted(requireContext())) {
                if (ApplicationHelper.checkConnection(requireActivity())) {
                    loading.setVisibility(View.VISIBLE);
                    buttonSend.setEnabled(false);
                    viewModel.sendInstruction(Objects.requireNonNull(email.getText()).toString(), isUnlock);
                }
            } else {
                ApplicationHelper.requestInternetAccessPermission((Activity) requireContext());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        callBackInterface = null;
        isUnlock = null;
    }
}