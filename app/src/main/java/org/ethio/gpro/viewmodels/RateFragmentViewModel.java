package org.ethio.gpro.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.ethio.gpro.models.Comment;
import org.ethio.gpro.repositories.RateRepository;

import java.util.List;

public class RateFragmentViewModel extends ViewModel {
    private final RateRepository repository;
    private final LiveData<List<Comment>> oCommentList;

    public RateFragmentViewModel(@NonNull RateRepository repository) {
        this.repository = repository;
        oCommentList = repository.getCommentList();

        repository.getCommentsFromApi();
    }

    public LiveData<List<Comment>> getCommentList() {
        return oCommentList;
    }

    // APIs
    public void createComment(@NonNull String content) {

    }

    public void updateComment(@NonNull String content, int commentId) {

    }

    public void deleteComment(int commentId) {

    }

//    LIFE cycle OF view model

    @Override
    protected void onCleared() {
        super.onCleared();

        if (repository != null) {
            repository.cancelConnection();
        }
    }
}
