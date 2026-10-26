package com.example.data.model

data class PartnerMood(
    val emoji: String,
    val label: String,
    val description: String,
    val batteryPercent: Int
)

data class PartnerProfile(
    val userName: String = "Taylor",
    val partnerName: String = "Jordan",
    val relationshipStartDateDays: Int = 428,
    val anniversaryDateText: String = "Nov 18 (24 days left)",
    val partnerStatusText: String = "Listening to 'Our Song' 🎶",
    val isPartnerOnline: Boolean = true,
    val partnerMood: PartnerMood = PartnerMood("🥰", "In Love & Cozy", "Counting down until we meet tonight", 95),
    val userMood: PartnerMood = PartnerMood("💖", "Adoring You", "Just sent a miss-you heartbeat", 90),
    val connectionCode: String = "SOUL-7749",
    val isConnected: Boolean = true,
    val dailySparkPercent: Int = 96
)
