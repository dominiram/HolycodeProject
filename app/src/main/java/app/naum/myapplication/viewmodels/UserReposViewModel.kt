package app.naum.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.naum.myapplication.networking.entities.UserRepoNetworkEntity
import app.naum.myapplication.repository.UserRepository
import app.naum.myapplication.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReposViewModel
@Inject
constructor(
    private val searchUserRepository: UserRepository
):
    ViewModel() {
    private val mutableUserRepos: MutableLiveData<DataState<List<UserRepoNetworkEntity>>> = MutableLiveData()
    val userRepos: LiveData<DataState<List<UserRepoNetworkEntity>>>
        get() = mutableUserRepos

    fun getUserRepos(user: String) {
        viewModelScope.launch {
            searchUserRepository.getUserRepos(user)
                .collect {
                    mutableUserRepos.value = it
                }
        }
    }
}
