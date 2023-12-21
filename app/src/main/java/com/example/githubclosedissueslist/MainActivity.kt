package com.example.githubclosedissueslist

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val gitHubService = retrofit.create(GitHubService::class.java)
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GitHubIssuesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repositoryNameTextView: TextView = findViewById(R.id.repositoryName)
        recyclerView = findViewById(R.id.recyclerView)
        val searchView: SearchView = findViewById(R.id.searchView)

        // Fetch repository information
        lifecycleScope.launch {
            val repository = withContext(Dispatchers.IO) {
                gitHubService.getRepository("facebookresearch", "llama-recipes")
            }

            // Display the repository name
            repositoryNameTextView.text = repository.name

            // Fetch closed issues
            val issues = withContext(Dispatchers.IO) {
                gitHubService.getClosedIssues("facebookresearch", "llama-recipes")
            }

            // Initialize RecyclerView and Adapter
            initializeRecyclerView(issues)
        }

        // Initialize Picasso (if not initialized already)
        Picasso.setSingletonInstance(Picasso.Builder(this).build())

        // Set up SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search when submit button is pressed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search as the user types
                adapter.filterIssues(newText)
                return true
            }
        })
    }

    private fun initializeRecyclerView(issues: List<GitHubIssue>) {
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = GitHubIssuesAdapter(issues)
        recyclerView.adapter = adapter
    }
}
