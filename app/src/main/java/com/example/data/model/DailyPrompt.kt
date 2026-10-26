package com.example.data.model

data class DailyPrompt(
    val id: String,
    val question: String,
    val category: String,
    val userAnswer: String? = null,
    val partnerAnswer: String? = null,
    val isAnsweredByUser: Boolean = false,
    val isAnsweredByPartner: Boolean = false,
    val isRevealed: Boolean = false
)

data class MemoryVaultItem(
    val id: String,
    val title: String,
    val date: String,
    val description: String,
    val emoji: String,
    val location: String,
    val isLocked: Boolean = false,
    val lockUnlockDate: String? = null
)
