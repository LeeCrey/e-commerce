package org.ethio.gpro.ui.fragments.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;
import org.ethio.gpro.helpers.ApplicationHelper;
import org.ethio.gpro.viewmodels.account.SessionsViewModel;

import java.util.Objects;

public class SessionsFragment extends Fragment {
    private MainActivityCallBackInterface callBackInterface;
    private NavController navController;
    private TextInputEditText email, password;
    private TextInputLayout passwordLayout;

    private SessionsViewModel sessionsViewModel;
    private Button signIn;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        navController = Navigation.findNavController(view);

        sessionsViewModel = new ViewModelProvider(this).get(SessionsViewModel.class);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        passwordLayout = view.findViewById(R.id.password_layout);
        Button signUp = view.findViewById(R.id.btn_sign_up);
        signIn = view.findViewById(R.id.btn_sign_in);
        loading = view.findViewById(R.id.progress_circular);
        TextView textView = view.findViewById(R.id.unlock_account);
        TextView forgotPassword = view.findViewById(R.id.forgot_password);

        // event list...
        signUp.setOnClickListener(v -> navController.navigate(R.id.login_to_sign_up));
//        textView.setOnClickListener(v -> navController.navigate(R.id.from_login_to_unlock));
//        forgotPassword.setOnClickListener(v -> {
//            Bundle arg = new Bundle();
//            arg.putString("lable_name", getString(R.string.lbl_password));
//            arg.putBoolean(UnlocksFragment.EXTRA, false);
//            navController.navigate(R.id.from_login_to_unlock, arg);
//        });
        signIn.setOnClickListener(v -> {
            callBackInterface.closeKeyBoard();
            callBackInterface.checkPermission();
            if (ApplicationHelper.checkConnection(requireActivity())) {
                setUiPlace(false);
                sessionsViewModel.login();
            }
        });
        afterInputChanged();

        // observers
        sessionsViewModel.getSessionResult().observe(getViewLifecycleOwner(), sessionResult -> {
            if (sessionResult == null) {
                return;
            }

            if (sessionResult.getOkay()) {
                Toast.makeText(requireContext(), sessionResult.getMessage(), Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            } else {
                Toast.makeText(requireContext(), sessionResult.getError(), Toast.LENGTH_SHORT).show();
                setUiPlace(true);
            }
        });
        sessionsViewModel.getFormErrors().observe(getViewLifecycleOwner(), errors -> {
            if (errors == null) {
                return;
            }

            email.setError(errors.getEmailError());
            if (null == errors.getPasswordError()) {
                passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            } else {
                passwordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
            }
            password.setError(errors.getPasswordError());

            signIn.setEnabled(errors.isRegistrationValid());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        passwordLayout = null;
        email = null;
        password = null;
        navController = null;
        callBackInterface = null;
        sessionsViewModel = null;

        signIn = null;
        loading = null;
    }

    private void setUiPlace(boolean status) {
        signIn.setEnabled(status);
        loading.setVisibility(status ? View.GONE : View.VISIBLE);
        passwordLayout.setEndIconMode(status ? TextInputLayout.END_ICON_PASSWORD_TOGGLE : TextInputLayout.END_ICON_NONE);

        email.setEnabled(status);
        password.setEnabled(status);
    }

    private void afterInputChanged() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailValue = Objects.requireNonNull(email.getText()).toString();
                String passwordValue = Objects.requireNonNull(password.getText()).toString();
                sessionsViewModel.loginInputChanged(requireContext(), emailValue, passwordValue);
            }
        };

        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }
}
