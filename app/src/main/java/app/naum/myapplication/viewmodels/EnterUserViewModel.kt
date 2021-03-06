package app.naum.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.naum.myapplication.models.UserModel
import app.naum.myapplication.repository.UserRepository
import app.naum.myapplication.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class
EnterUserViewModel
    @Inject
    constructor(
        private val searchUserRepository: UserRepository
    ): ViewModel() {
    private val TAG = "EnterUserViewModel"
    private val mutableUserState: MutableLiveData<DataState<UserModel>> = MutableLiveData()
    val userState: LiveData<DataState<UserModel>>
        get() = mutableUserState

    fun getSearchUserInfo(user: String) {
        viewModelScope.launch {
            searchUserRepository.getUserInfo(user)
                .collect {
                    mutableUserState.value = it
                }
        }
    }
}
