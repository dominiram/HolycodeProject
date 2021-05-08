package app.naum.myapplication.networking

import app.naum.myapplication.networking.entities.UserInfoNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchUserService {
    @GET("users/{user}")
    suspend fun getUserInfo(@Path("user") user: String): UserInfoNetworkEntity
}
