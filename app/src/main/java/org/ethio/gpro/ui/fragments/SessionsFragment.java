package org.ethio.gpro.ui.fragments;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.ethio.gpro.R;
import org.ethio.gpro.callbacks.MainActivityCallBackInterface;

public class SessionsFragment extends Fragment {
    private MainActivityCallBackInterface callBackInterface;
    private NavController navController;
    private TextInputEditText email, password;
    private TextInputLayout emailLayout, passwordLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callBackInterface = (MainActivityCallBackInterface) requireActivity();
        navController = Navigation.findNavController(view);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        passwordLayout = view.findViewById(R.id.password_layout);
        emailLayout = view.findViewById(R.id.email_layout);
        Button signUp = view.findViewById(R.id.btn_sign_up);
        Button signIn = view.findViewById(R.id.btn_sign_in);
        TextView textView = view.findViewById(R.id.unlock_account);
        TextView forgotPassword = view.findViewById(R.id.forgot_password);
        ProgressBar loading = view.findViewById(R.id.progress_circular);

        // event list...
        signUp.setOnClickListener(v -> navController.navigate(R.id.login_to_sign_up));
        textView.setOnClickListener(v -> navController.navigate(R.id.from_login_to_unlock));
        forgotPassword.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putString("lable_name", getString(R.string.lbl_password));
            arg.putBoolean(UnlockFragment.EXTRA, false);
            navController.navigate(R.id.from_login_to_unlock, arg);
        });
        signIn.setOnClickListener(v -> {
            // logic
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        callBackInterface.hideBottomNavView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        emailLayout = null;
        passwordLayout = null;
        email = null;
        password = null;
        navController = null;
        callBackInterface = null;
    }
}
