package com.example.feedforward.utils

import android.content.Context

object UserDataManager {
    private const val PREFS_NAME = "UserPrefs"
    private const val KEY_USER_ID = "LoggedInUserID"
    private const val KEY_COMPLETED_FEEDBACK = "CompletedFeedback_"

    /** Save logged-in user ID */
    fun setCurrentUserId(context: Context, userId: String) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    /** Retrieve logged-in user ID */
    fun getCurrentUserId(context: Context): String {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(KEY_USER_ID, "") ?: ""
    }

    /** Mark feedback as completed for a user */
    fun markFeedbackCompleted(context: Context, userId: String, feedbackId: String) {
        if (userId.isEmpty()) return

        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val key = "$KEY_COMPLETED_FEEDBACK$userId"
        val completedFeedback = getCompletedFeedback(context, userId).toMutableSet()

        completedFeedback.add(feedbackId)
        sharedPrefs.edit().putStringSet(key, completedFeedback).apply()
    }

    /** Retrieve completed feedback per user */
    fun getCompletedFeedback(context: Context, userId: String): Set<String> {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val key = "$KEY_COMPLETED_FEEDBACK$userId"
        return sharedPrefs.getStringSet(key, emptySet()) ?: emptySet()
    }

    /** Check if feedback is completed by the user */
    fun isFeedbackCompleted(context: Context, userId: String, feedbackId: String): Boolean {
        return getCompletedFeedback(context, userId).contains(feedbackId)
    }
}
