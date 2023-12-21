package com.example.githubclosedissueslist

// GitHubIssuesAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class GitHubIssuesAdapter(private var originalIssues: List<GitHubIssue>) :
    RecyclerView.Adapter<GitHubIssuesAdapter.ViewHolder>() {

    private var filteredIssues: List<GitHubIssue> = originalIssues

    init {
        setHasStableIds(true)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val issueTitle: TextView = itemView.findViewById(R.id.issueTitle)
        val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        val closedDate: TextView = itemView.findViewById(R.id.closedDate)
        val creatorName: TextView = itemView.findViewById(R.id.creatorName)
        val creatorImage: CircleImageView = itemView.findViewById(R.id.creatorImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_issue, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val issue = filteredIssues[position]

        // Display "Issue Name:" before the issue title
        holder.issueTitle.text = "Issue Name: ${issue.title}"

        holder.createdDate.text = "Created date: ${formatDate(issue.createdDate)}"
        holder.closedDate.text =
            if (issue.closedDate != null) "Closed date: ${formatDate(issue.closedDate)}"
            else "Not closed yet"

        // Display the creator's name
        holder.creatorName.text = "Created by: ${issue.user.login}"

        // Load the creator's image using a library like Picasso, Glide, etc.
        // Example using Picasso:
        Picasso.get().load(issue.user.avatarUrl).into(holder.creatorImage)
    }

    override fun getItemCount(): Int {
        return filteredIssues.size
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun filterIssues(query: String?) {
        val filtered = if (query.isNullOrBlank()) {
            // If the query is null or empty, show all original issues
            originalIssues
        } else {
            // Filter issues based on the query
            originalIssues.filter { it.title.contains(query, ignoreCase = true) }
        }

        updateIssues(filtered)
    }

    private fun updateIssues(newIssues: List<GitHubIssue>) {
        filteredIssues = newIssues
        notifyDataSetChanged()
    }
}
