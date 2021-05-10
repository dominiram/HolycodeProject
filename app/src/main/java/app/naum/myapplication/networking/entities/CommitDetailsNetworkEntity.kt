package app.naum.myapplication.networking.entities

import com.google.gson.annotations.SerializedName

data class CommitDetailsNetworkEntity(
    val commit: Commit
)

data class Commit(
    val author: Author,
    val committer: Committer,
    @SerializedName("message")
    val commitMessage: String,
    val comments: List<Comment>
)

data class Author(
    val name: String
)

data class Committer(
    val name: String,
    val date: String
)

data class Comment(
    val user: CommitMessageAuthor,
    @SerializedName("body")
    val message: String
)

data class CommitMessageAuthor(
    val login: String
)
