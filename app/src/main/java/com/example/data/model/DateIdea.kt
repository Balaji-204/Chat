package com.example.data.model

enum class DateStatus {
    WISHLIST,
    PLANNED,
    COMPLETED
}

data class DateIdea(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "Romantic", "Cozy Home", "Adventure", "Foodie"
    val estimatedBudget: String, // "$", "$$", "$$$"
    val iconEmoji: String,
    val status: DateStatus = DateStatus.WISHLIST,
    val scheduledDate: String? = null
)
