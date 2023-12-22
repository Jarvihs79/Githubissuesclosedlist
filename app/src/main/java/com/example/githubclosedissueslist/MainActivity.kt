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

        //getting info from repo
        lifecycleScope.launch {
            val repository = withContext(Dispatchers.IO) {
                gitHubService.getRepository("facebookresearch", "llama-recipes")
            }


            repositoryNameTextView.text = repository.name


            val issues = withContext(Dispatchers.IO) {
                gitHubService.getClosedIssues("facebookresearch", "llama-recipes")
            }


            initializeRecyclerView(issues)
        }


        Picasso.setSingletonInstance(Picasso.Builder(this).build())


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

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
