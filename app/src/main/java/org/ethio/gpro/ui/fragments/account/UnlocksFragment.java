package org.ethio.gpro.ui.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.databinding.FragmentInstructionBinding;
import org.ethio.gpro.helpers.ApplicationHelper;
import org.ethio.gpro.viewmodels.account.AccountViewModel;
import org.ethio.gpro.viewmodels.account.InstructionsViewModel;

import java.util.Objects;

// This fragment is for both unlock instruction request and password reset instruction request
// The only difference for both is just only api(end point)
public class UnlocksFragment extends Fragment {
    public final static String EXTRA = "isUnlock";
    protected FragmentInstructionBinding binding;
    protected TextView confirm;
    protected Boolean isUnlock;
    private String unlockToken = null;

    private MainActivityCallBackInterface callBackInterface;
    private AccountViewModel accountViewModel;
    private InstructionsViewModel instructionsViewModel;
    private TextInputEditText email;
    private ProgressBar loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUnlock = bundle.getBoolean(EXTRA);
            unlockToken = bundle.getString("unlockToken");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstructionBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController navController = Navigation.findNavController(view);
        final SwipeRefreshLayout refreshLayout = binding.refreshLayout;
        confirm = binding.confirmAccount;
        email = binding.email;
        loading = binding.progressCircular;

        refreshLayout.setOnRefreshListener(() -> {
            sendRequestIfThereIsConnection();
            refreshLayout.setRefreshing(false);
        });

        // if it's unlock request not unlock instruction request
        // enters this condition when a user navigate through deeplink
        // deeplink format: rootUrl/customers/unlock?unlock_token={unlockToken}
        if (unlockToken != null) {
            accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
            // conditional observer
            accountViewModel.getAccountResponse().observe(getViewLifecycleOwner(), accountResponse -> {
                if (accountResponse == null) {
                    return;
                }

                if (accountResponse.getOkay()) {
                    navController.navigateUp();
                } else {
                    loading.setVisibility(View.GONE);
                }
                Toast.makeText(requireContext(), accountResponse.getMessage(), Toast.LENGTH_SHORT).show();
            });
            // p-holders
            confirm.setVisibility(View.GONE);
            email.setEnabled(false);
            sendRequestIfThereIsConnection();

            return;
        }

        // If it's account unlock instruction link request
        final Button buttonSend = binding.sendInstructions;
        callBackInterface = (MainActivityCallBackInterface) requireContext();
        instructionsViewModel = new ViewModelProvider(this).get(InstructionsViewModel.class);
        refreshLayout.setEnabled(false); // refreshing is not required since there is a send button

        if (!isUnlock) {
            binding.headerForInstruction.setText(R.string.forgot_password);
        }

        // observers
        instructionsViewModel.getInstructionResponse().observe(getViewLifecycleOwner(), instructionsResponse -> {
            if (instructionsResponse == null) {
                return;
            }

            Toast.makeText(requireContext(), instructionsResponse.getMessage(), Toast.LENGTH_SHORT).show();
        });

        // event
        ApplicationHelper.initEmailWatcher(requireContext(), email, buttonSend);
        buttonSend.setOnClickListener(v -> {
            v.setEnabled(false);
            email.setEnabled(false);
            sendRequestIfThereIsConnection();
        });
        confirm.setOnClickListener(v -> navController.navigate(R.id.unlock_to_navigation_confirmation));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        callBackInterface = null;
        accountViewModel = null;
        instructionsViewModel = null;
        email = null;
        isUnlock = null;
        binding = null;
        confirm = null;
        unlockToken = null;
        loading = null;
    }

    private void sendRequestIfThereIsConnection() {
        if (unlockToken == null) {
            callBackInterface.closeKeyBoard();
            if (ApplicationHelper.checkConnection(requireActivity())) {
                loading.setVisibility(View.VISIBLE);
                instructionsViewModel.sendInstruction(Objects.requireNonNull(email.getText()).toString(), isUnlock);
            }
        } else {
            if (ApplicationHelper.isInternetAvailable(requireContext())) {
                loading.setVisibility(View.VISIBLE);
                accountViewModel.unlock(unlockToken);
            } else {
                String msg = "Open connection and pull down(refresh the page)";
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}