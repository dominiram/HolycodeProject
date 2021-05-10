package app.naum.myapplication.networking

import app.naum.myapplication.networking.entities.CommitDetailsNetworkEntity
import app.naum.myapplication.networking.entities.UserInfoNetworkEntity
import app.naum.myapplication.networking.entities.UserRepoNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{user}")
    suspend fun getUserInfo(@Path("user") user: String): UserInfoNetworkEntity

    @GET("users/{user}/repos")
    suspend fun getUserRepos(@Path("user") user: String): List<UserRepoNetworkEntity>

    @GET("repos/{user}/{repo}/commits")
    suspend fun getCommits(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): List<CommitDetailsNetworkEntity>
}
