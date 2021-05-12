package app.naum.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.naum.myapplication.networking.entities.CommentNetworkEntity
import app.naum.myapplication.networking.entities.CommitDetailsNetworkEntity
import app.naum.myapplication.repository.UserRepository
import app.naum.myapplication.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.CompletionException
import javax.inject.Inject

@HiltViewModel
class CommitDetailsViewModel
@Inject
constructor(
    private val searchUserRepository: UserRepository
) : ViewModel() {

    private val TAG = "CommitDetailsViewModel"
    private val mutableCommitsState: MutableLiveData<DataState<List<CommitDetailsNetworkEntity>>> =
        MutableLiveData()
    val commitsState: LiveData<DataState<List<CommitDetailsNetworkEntity>>>
        get() = mutableCommitsState
    val commentsList: MutableMap<Int, List<CommentNetworkEntity>?> = mutableMapOf()

    fun getCommitDetails(user: String, repo: String) {
        viewModelScope.launch {
//            searchUserRepository.getComments(user, repo, commitHash)
//                .collect {
//                    comments = it
//                }
            searchUserRepository.getCommits(user, repo)
                .collect {
                    when(it) {
                        is DataState.Success -> {
                            Log.d(TAG, "getCommitDetails: data = ${it.data}")
                            for (commit in it.data.withIndex())
                                searchUserRepository.getComments(user, repo, commit.value.sha)
                                    .collect { allComments ->
                                        Log.d(TAG, "getCommitDetails: comments = $allComments")
                                        commentsList[commit.index] = allComments
                                    }
                        }
                    }
                    mutableCommitsState.value = it
                }
        }
    }
}
