package app.naum.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.naum.myapplication.networking.entities.CommitDetailsNetworkEntity
import app.naum.myapplication.repository.UserRepository
import app.naum.myapplication.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommitDetailsViewModel
@Inject
constructor(
    private val searchUserRepository: UserRepository
) : ViewModel() {

    private val mutableCommitsState: MutableLiveData<DataState<List<CommitDetailsNetworkEntity>>> = MutableLiveData()
    val commitsState: LiveData<DataState<List<CommitDetailsNetworkEntity>>>
        get() = mutableCommitsState

    fun getCommitDetails(user: String, repo: String) {
        viewModelScope.launch {
            searchUserRepository.getCommits(user, repo)
                .collect {
                    mutableCommitsState.value = it
                }
        }
    }
}
