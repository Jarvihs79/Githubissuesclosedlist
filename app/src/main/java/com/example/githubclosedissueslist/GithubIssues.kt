package com.example.githubclosedissueslist


import com.google.gson.annotations.SerializedName
import java.util.Date

data class GitHubIssue(
    val title: String,
    val number: Int,
    val url: String,
    @SerializedName("created_at")
    val createdDate: Date,
    @SerializedName("closed_at")
    val closedDate: Date?,
    val user: GitHubUser
)

data class GitHubUser(
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)

data class GitHubRepository(
    val name: String
)



