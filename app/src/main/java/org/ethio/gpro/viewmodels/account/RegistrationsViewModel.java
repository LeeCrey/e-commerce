package org.ethio.gpro.viewmodels.account;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ethio.gpro.R;
import org.ethio.gpro.helpers.InputHelper;
import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.FormErrors;
import org.ethio.gpro.models.responses.RegistrationResponse;
import org.ethio.gpro.repositories.RegistrationsRepository;

import java.util.Map;


public class RegistrationsViewModel extends AndroidViewModel {
    private final MutableLiveData<RegistrationResponse> mRegistrationResponse;
    private final MutableLiveData<FormErrors> mFormState;
    private final RegistrationsRepository repository;
    private Map<String, String> map;

    public RegistrationsViewModel(@NonNull Application application) {
        super(application);

        mFormState = new MutableLiveData<>();
        repository = new RegistrationsRepository(application);
        mRegistrationResponse = repository.getRegistrationResponse();
    }

    public LiveData<RegistrationResponse> getRegistrationResponse() {
        return mRegistrationResponse;
    }

    public LiveData<FormErrors> getFormState() {
        return mFormState;
    }

    public void dataChanged(Map<String, String> data, Context context) {
        map = data;

        String fName = data.get(context.getString(R.string.firstName));
        String lName = data.get(context.getString(R.string.lastName));
        String email = data.get(context.getString(R.string.email));
        String password = data.get(context.getString(R.string.password));
        String passwordConfirmation = data.get(context.getString(R.string.passwordConfirmation));

        FormErrors errors = new FormErrors();
        errors.setFirstNameError(InputHelper.checkInput(fName, context));
        errors.setLastNameError(InputHelper.checkInput(lName, context));
        errors.setEmailError(InputHelper.checkEmail(email, context));
        errors.setPasswordError(InputHelper.checkPassword(password, context));
        errors.setPasswordConfirmationError(InputHelper.checkPasswordConfirmation(password, passwordConfirmation, context));

        mFormState.postValue(errors);
    }

    // APIs
    public void signUp(Context context) {
        if (null != repository) {
            Customer customer = new Customer()
                    .setCredentials(
                            map.get(context.getString(R.string.email)),
                            map.get(context.getString(R.string.password)))
                    .setFullName(
                            map.get(context.getString(R.string.firstName)),
                            map.get(context.getString(R.string.lastName)));
            customer.setPasswordConfirmation(map.get(context.getString(R.string.passwordConfirmation)));
            repository.signUp(customer);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (null != repository) {
            repository.cancelConnection();
        }
    }
}
