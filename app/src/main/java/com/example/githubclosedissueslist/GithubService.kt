package com.example.githubclosedissueslist

// GitHubService.kt
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("repos/{owner}/{repo}/issues")
    suspend fun getClosedIssues(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String = "closed"
    ): List<GitHubIssue>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): GitHubRepository
}
