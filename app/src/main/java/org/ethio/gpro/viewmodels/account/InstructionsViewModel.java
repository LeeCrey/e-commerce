package org.ethio.gpro.viewmodels.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ethio.gpro.models.Customer;
import org.ethio.gpro.models.responses.InstructionsResponse;
import org.ethio.gpro.repositories.InstructionRepository;


public class InstructionsViewModel extends AndroidViewModel {
    private final LiveData<InstructionsResponse> oInstructionResponse;
    private final InstructionRepository repository;

    public InstructionsViewModel(@NonNull Application application) {
        super(application);

        repository = new InstructionRepository(application);
        oInstructionResponse = repository.getInstructionResponse();
    }

    public LiveData<InstructionsResponse> getInstructionResponse() {
        return oInstructionResponse;
    }

    public void sendFeedback(String header, String  msg) {
        repository.sendFeedback(header, msg);
    }

    public void sendInstruction(String email, boolean isUnlockRequest) {
        Customer customer = new Customer();
        customer.setEmail(email);
        repository.sendRequest(customer, isUnlockRequest);
    }

}
