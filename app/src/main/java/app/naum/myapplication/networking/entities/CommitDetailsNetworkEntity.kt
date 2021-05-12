package app.naum.myapplication.networking.entities

import com.google.gson.annotations.SerializedName

data class CommitDetailsNetworkEntity(
    val sha: String,
    val commit: Commit
)

data class Commit(
    val author: Author,
    val committer: Committer,
    @SerializedName("message")
    val commitMessage: String
)

data class Author(
    val name: String
)

data class Committer(
    val name: String,
    val date: String
)
