package com.example.feedforward.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feedforward.databinding.FragmentHomeBinding
import com.example.feedforward.utils.UserDataManager
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val feedbackList = mutableListOf<FeedbackData>()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = UserDataManager.getCurrentUserId(requireContext())

        loadFeedbackData()
        updateFeedbackList()
    }

    private fun updateFeedbackList() {
        val availableFeedback = feedbackList.filter {
            !UserDataManager.isFeedbackCompleted(requireContext(), userId, it.id)
        }

        val adapter = FeedbackAdapter(availableFeedback) { feedback ->
            val intent = Intent(requireContext(), FeedbackDetailActivity::class.java)
            intent.putExtra("FEEDBACK_ID", feedback.id)
            intent.putExtra("FEEDBACK_TITLE", feedback.title)
            intent.putStringArrayListExtra("FEEDBACK_QUESTIONS", ArrayList(feedback.questions))
            startActivity(intent)
        }

        binding.recyclerViewFeedback.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFeedback.adapter = adapter
    }

    private fun loadFeedbackData() {
        feedbackList.clear()
        val inputStream = requireContext().assets.open("feedback_data.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = bufferedReader.use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val questionsArray = obj.getJSONArray("questions")
            val questionsList = mutableListOf<String>()

            for (j in 0 until questionsArray.length()) {
                questionsList.add(questionsArray.getString(j))
            }

            feedbackList.add(
                FeedbackData(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    description = obj.getString("description"),
                    questions = questionsList
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        updateFeedbackList() // Refresh list when returning
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
