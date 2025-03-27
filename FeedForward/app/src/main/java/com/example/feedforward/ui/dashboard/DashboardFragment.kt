package com.example.feedforward.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feedforward.databinding.FragmentDashboardBinding
import com.example.feedforward.ui.home.FeedbackAdapter
import com.example.feedforward.ui.home.FeedbackData
import com.example.feedforward.utils.UserDataManager
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val completedFeedbackList = mutableListOf<FeedbackData>()
    private val feedbackList = mutableListOf<FeedbackData>() // ✅ Fix: Declare feedbackList
    private lateinit var userId: String // ✅ Fix: Declare userId properly

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = UserDataManager.getCurrentUserId(requireContext()) // ✅ Fix: Properly initialize userId

        loadFeedbackData() // ✅ Fix: Ensure feedbackList is populated first
        loadCompletedFeedback()

        binding.recyclerViewDashboard.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDashboard.adapter = FeedbackAdapter(completedFeedbackList) { }
    }

    /** ✅ Fix: Load all feedback data from JSON */
    private fun loadFeedbackData() {
        feedbackList.clear()

        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** ✅ Fix: Load completed feedback per user */
    private fun loadCompletedFeedback() {
        completedFeedbackList.clear()

        val completedFeedbackIds = UserDataManager.getCompletedFeedback(requireContext(), userId)

        completedFeedbackIds.forEach { feedbackId ->
            val feedback = feedbackList.find { it.id == feedbackId }
            feedback?.let { completedFeedbackList.add(it) }
        }

        binding.recyclerViewDashboard.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
