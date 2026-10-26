package com.example.data.model

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val userChoiceIndex: Int? = null,
    val partnerChoiceIndex: Int? = null
)

data class CoupleQuiz(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val questions: List<QuizQuestion>,
    val completed: Boolean = false,
    val matchScorePercent: Int? = null
)
