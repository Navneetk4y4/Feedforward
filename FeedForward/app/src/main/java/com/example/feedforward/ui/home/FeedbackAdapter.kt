package com.example.feedforward.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.feedforward.databinding.ItemFeedbackBinding

class FeedbackAdapter(
    private val feedbackList: List<FeedbackData>,
    private val onItemClick: (FeedbackData) -> Unit
) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    inner class FeedbackViewHolder(private val binding: ItemFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feedback: FeedbackData) {
            binding.feedbackTitle.text = feedback.title
            binding.feedbackDescription.text = feedback.description
            binding.root.setOnClickListener { onItemClick(feedback) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        holder.bind(feedbackList[position])
    }

    override fun getItemCount(): Int = feedbackList.size
}
