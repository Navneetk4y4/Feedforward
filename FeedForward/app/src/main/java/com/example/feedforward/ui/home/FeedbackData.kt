package com.example.feedforward.ui.home

data class FeedbackData(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<String> // Add questions list
)
