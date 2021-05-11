package app.naum.myapplication.networking.entities

import com.google.gson.annotations.SerializedName

data class CommentNetworkEntity(
    val user: User,
    @SerializedName("created_at")
    val commentTime: String,
    @SerializedName("body")
    val message: String
)

data class User(
    @SerializedName("login")
    val name: String
)
