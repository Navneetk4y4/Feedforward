package com.example.feedforward.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedforward.databinding.ActivityFeedbackDetailBinding
import com.example.feedforward.utils.UserDataManager

class FeedbackDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackDetailBinding
    private lateinit var userId: String
    private lateinit var feedbackId: String
    private val answerInputs = mutableListOf<EditText>() // Store answer input fields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = UserDataManager.getCurrentUserId(this)
        feedbackId = intent.getStringExtra("FEEDBACK_ID") ?: return
        val feedbackTitle = intent.getStringExtra("FEEDBACK_TITLE") ?: "Feedback"
        val questions = intent.getStringArrayListExtra("FEEDBACK_QUESTIONS") ?: emptyList()

        binding.feedbackTitle.text = feedbackTitle

        // Dynamically create and add TextViews & EditTexts for each question
        questions.forEach { question ->
            val textView = createQuestionTextView(question)
            val editText = createAnswerEditText()

            binding.questionContainer.addView(textView)
            binding.questionContainer.addView(editText)
            answerInputs.add(editText) // Store for retrieving answers
        }

        binding.submitButton.setOnClickListener {
            // Save answers if needed (not implemented yet)
            UserDataManager.markFeedbackCompleted(this, userId, feedbackId)

            Toast.makeText(this, "Feedback Submitted!", Toast.LENGTH_SHORT).show()
            finish() // Close activity
        }
    }

    private fun createQuestionTextView(question: String): TextView {
        return TextView(this).apply {
            text = question
            textSize = 18f
            setPadding(10, 10, 10, 10)
        }
    }

    private fun createAnswerEditText(): EditText {
        return EditText(this).apply {
            hint = "Enter your response"
            textSize = 16f
            setPadding(10, 10, 10, 20)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
