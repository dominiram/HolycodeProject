package app.naum.myapplication.networking.entities

import com.google.gson.annotations.SerializedName

data class UserRepoNetworkEntity(
    val id: Int,
    val name: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("has_issues")
    val hasIssues: Boolean,
    @SerializedName("open_issues_count")
    val issueCount: Int
)
