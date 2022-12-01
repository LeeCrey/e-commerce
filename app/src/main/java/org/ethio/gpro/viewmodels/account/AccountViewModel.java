package org.ethio.gpro.viewmodels.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ethio.gpro.models.AccountResponse;
import org.ethio.gpro.repositories.AccountRepository;

public class AccountViewModel extends AndroidViewModel {
    private final AccountRepository accountRepository;
    private final LiveData<AccountResponse> accountResponse;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        accountRepository = new AccountRepository(application);
        accountResponse = accountRepository.getAccountResponse();
    }

    public LiveData<AccountResponse> getAccountResponse() {
        return accountResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        accountRepository.cancelConnection();
    }

    // Operations
    public void unlock(@NonNull String unlockToken) {
        accountRepository.unlock(unlockToken);
    }

    public void confirm(@NonNull String confirmationToken) {
        accountRepository.confirm(confirmationToken);
    }

    public void passwordReset(@NonNull String resetPasswordToken, String password, String passwordConfirmation) {
        accountRepository.passwordReset(resetPasswordToken, password, passwordConfirmation);
    }


}
