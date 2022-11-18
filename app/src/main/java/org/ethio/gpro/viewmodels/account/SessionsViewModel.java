package org.ethio.gpro.viewmodels.account;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.ethio.gpro.helpers.InputHelper;
import org.ethio.gpro.helpers.PreferenceHelper;
import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.FormErrors;
import org.ethio.gpro.models.responses.SessionResponse;
import org.ethio.gpro.repositories.SessionsRepository;


// view model for both login and logout
public class SessionsViewModel extends ViewModel {
    private SessionsRepository repository;
    private String email, password;

    private MutableLiveData<FormErrors> mFormErrors;
    private MutableLiveData<SessionResponse> mSessionResult;

    public void initForLogin() {
        if (initForLogout()) {
            return;
        }

        mFormErrors = new MutableLiveData<>();
    }

    public boolean initForLogout() {
        if (mSessionResult != null) {
            return true;
        }

        repository = new SessionsRepository();
        mSessionResult = repository.getSessionResult();
        return false;
    }

    public LiveData<SessionResponse> getSessionResult() {
        return mSessionResult;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*BEGIN LOGIN PART*/

    public LiveData<FormErrors> getFormErrors() {
        return mFormErrors;
    }

    public void setFormErrors(FormErrors mFormError) {
        mFormErrors.postValue(mFormError);
    }

    public void loginInputChanged(Context context, String emailValue, String passwordValue) {
        email = emailValue;
        password = passwordValue;
        FormErrors errors = new FormErrors();
        errors.setEmailError(InputHelper.checkEmail(emailValue, context));
        errors.setPasswordError(InputHelper.checkInput(password, context));
        setFormErrors(errors);
    }

    // Login
    public void sendLoginRequest() {
        Customer customer = new Customer().setCredentials(email, password);
        repository.login(customer);
    }
    /*END LOGIN PART*/

    /*BEGIN LOGOUT PART*/
    public void logout(Context context) {
        String authToken = PreferenceHelper.getAuthToken(context);
        repository.logout(authToken);
    }
    /*END LOGOUT PART*/

    @Override
    protected void onCleared() {
        super.onCleared();

        if (repository != null) {
            repository.cancelConnection();
            repository = null;
        }
    }
}
