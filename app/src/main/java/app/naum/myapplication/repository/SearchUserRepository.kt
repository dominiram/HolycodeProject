package app.naum.myapplication.repository

import app.naum.myapplication.models.UserModel
import app.naum.myapplication.networking.SearchUserNetworkMapper
import app.naum.myapplication.networking.SearchUserService
import app.naum.myapplication.networking.entities.UserInfoNetworkEntity
import app.naum.myapplication.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call

class SearchUserRepository constructor(
    private val service: SearchUserService,
    private val mapper: SearchUserNetworkMapper
) {
    suspend fun getUserInfo(user: String): Flow<DataState<UserModel>> = flow {
        emit(DataState.Loading)

        try {
            val networkUserInfo = service.getUserInfo(user)
            val userModel = mapper.mapFromEntity(networkUserInfo)
            emit(DataState.Success(userModel))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}
