package com.example.data.model

data class SeoAuditMetric(
    val title: String,
    val score: Int, // 0 - 100
    val status: String, // "Good", "Needs Attention", "Optimized"
    val description: String,
    val category: String
)

data class TargetKeyword(
    val keyword: String,
    val monthlySearchVolume: String,
    val keywordDifficulty: String, // "Easy", "Medium", "Competitive"
    val intent: String, // "Commercial", "Informational", "Transactional"
    val rankingPriority: String // "High", "Critical", "Secondary"
)

data class BoostingGuideItem(
    val id: String,
    val category: String,
    val title: String,
    val summary: String,
    val actionSteps: List<String>,
    val isCompleted: Boolean = false
)

data class WebMetaTagsConfig(
    val siteName: String = "SoulSync - The Private Space for Couples & Partners",
    val title: String = "SoulSync | Private Couple Chat, Daily Spark Prompts & Shared Memories",
    val description: String = "The intimate private messenger and relationship space for lovers. Chat in real-time, sync moods, answer daily couple questions, and plan dream date nights.",
    val targetUrl: String = "https://soulsync.love",
    val ogImageUrl: String = "https://soulsync.love/og-couple-preview.jpg",
    val twitterHandle: String = "@SoulSyncApp",
    val keywordsString: String = "couple chat, partner messenger, relationship app, long distance love, daily couple questions, date night planner, intimate chat, private couple space"
)
