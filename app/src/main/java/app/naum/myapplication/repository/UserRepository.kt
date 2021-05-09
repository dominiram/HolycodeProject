package app.naum.myapplication.repository

import app.naum.myapplication.models.UserModel
import app.naum.myapplication.networking.SearchUserNetworkMapper
import app.naum.myapplication.networking.UserService
import app.naum.myapplication.networking.entities.UserRepoNetworkEntity
import app.naum.myapplication.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository constructor(
    private val service: UserService,
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

    suspend fun getUserRepos(user: String): Flow<DataState<List<UserRepoNetworkEntity>>> =
        flow {
            emit(DataState.Loading)
            try {
                val userRepos = service.getUserRepos(user)
                emit(DataState.Success(userRepos))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
}
