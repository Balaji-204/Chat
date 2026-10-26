package com.example.ui.viewmodel

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.SoulSyncRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavTab {
    CHAT,
    COUPLE_SPACE,
    QUIZ,
    SEO_BOOSTER,
    SETTINGS
}

data class SoulSyncUiState(
    val currentTab: AppNavTab = AppNavTab.CHAT,
    val activeSenderId: String = "user", // "user" or "partner" for live 2-way testing
    val messages: List<ChatMessage> = emptyList(),
    val profile: PartnerProfile = PartnerProfile(),
    val dailyPrompt: DailyPrompt? = null,
    val dateIdeas: List<DateIdea> = emptyList(),
    val memories: List<MemoryVaultItem> = emptyList(),
    val coupleQuiz: CoupleQuiz? = null,
    val metaConfig: WebMetaTagsConfig = WebMetaTagsConfig(),
    val seoMetrics: List<SeoAuditMetric> = emptyList(),
    val targetKeywords: List<TargetKeyword> = emptyList(),
    val boostingGuides: List<BoostingGuideItem> = emptyList(),
    val isPartnerTyping: Boolean = false,
    val showHeartAnimation: Boolean = false,
    val userNotificationMessage: String? = null
)

class SoulSyncViewModel(
    private val repository: SoulSyncRepository = SoulSyncRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SoulSyncUiState(
            messages = repository.messages.value,
            profile = repository.profile.value,
            dailyPrompt = repository.dailyPrompt.value,
            dateIdeas = repository.dateIdeas.value,
            memories = repository.memories.value,
            coupleQuiz = repository.coupleQuiz.value,
            metaConfig = repository.metaConfig.value,
            seoMetrics = repository.seoMetrics.value,
            targetKeywords = repository.targetKeywords.value,
            boostingGuides = repository.boostingGuides.value
        )
    )
    val uiState: StateFlow<SoulSyncUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.messages.collect { msgs ->
                _uiState.update { it.copy(messages = msgs) }
            }
        }
        viewModelScope.launch {
            repository.profile.collect { prof ->
                _uiState.update { it.copy(profile = prof) }
            }
        }
        viewModelScope.launch {
            repository.dailyPrompt.collect { prompt ->
                _uiState.update { it.copy(dailyPrompt = prompt) }
            }
        }
        viewModelScope.launch {
            repository.dateIdeas.collect { dates ->
                _uiState.update { it.copy(dateIdeas = dates) }
            }
        }
        viewModelScope.launch {
            repository.memories.collect { mems ->
                _uiState.update { it.copy(memories = mems) }
            }
        }
        viewModelScope.launch {
            repository.coupleQuiz.collect { quiz ->
                _uiState.update { it.copy(coupleQuiz = quiz) }
            }
        }
        viewModelScope.launch {
            repository.boostingGuides.collect { guides ->
                _uiState.update { it.copy(boostingGuides = guides) }
            }
        }
        viewModelScope.launch {
            repository.metaConfig.collect { meta ->
                _uiState.update { it.copy(metaConfig = meta) }
            }
        }
    }

    fun selectTab(tab: AppNavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun toggleActiveSender() {
        val nextSender = if (_uiState.value.activeSenderId == "user") "partner" else "user"
        _uiState.update { it.copy(activeSenderId = nextSender) }
        val name = if (nextSender == "user") repository.profile.value.userName else repository.profile.value.partnerName
        showNotification("Switched view to test as $name 💬")
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val senderId = _uiState.value.activeSenderId
        val senderName = if (senderId == "user") repository.profile.value.userName else repository.profile.value.partnerName
        repository.sendMessage(text = text.trim(), senderId = senderId, senderName = senderName)

        // If user sent it, occasionally trigger a playful realistic reply from partner
        if (senderId == "user") {
            triggerAutomatedPartnerReply(text)
        }
    }

    fun sendVoiceNote(durationSeconds: Int = 8) {
        val senderId = _uiState.value.activeSenderId
        val senderName = if (senderId == "user") repository.profile.value.userName else repository.profile.value.partnerName
        repository.sendMessage(
            text = "Voice message ($durationSeconds sec) 🎙️ 'Thinking about you, my love!'",
            senderId = senderId,
            senderName = senderName,
            type = MessageType.AUDIO_NOTE,
            duration = durationSeconds
        )
        showNotification("Voice note sent ❤️")
    }

    fun sendHeartbeat(context: Context? = null) {
        val senderId = _uiState.value.activeSenderId
        val senderName = if (senderId == "user") repository.profile.value.userName else repository.profile.value.partnerName
        repository.sendMessage(
            text = "💓 Heartbeat Buzz: Thinking of you right now!",
            senderId = senderId,
            senderName = senderName,
            type = MessageType.HEARTBEAT_TAP
        )

        // Trigger vibration
        context?.let { ctx ->
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val manager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    manager?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 150, 100, 250), -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(300)
                }
            } catch (_: Exception) {}
        }

        _uiState.update { it.copy(showHeartAnimation = true) }
        viewModelScope.launch {
            delay(1500)
            _uiState.update { it.copy(showHeartAnimation = false) }
        }
        showNotification("Heartbeat buzz sent to your partner! 💓")
    }

    fun sendLoveSticker(stickerEmoji: String, title: String) {
        val senderId = _uiState.value.activeSenderId
        val senderName = if (senderId == "user") repository.profile.value.userName else repository.profile.value.partnerName
        repository.sendMessage(
            text = "$stickerEmoji $title",
            senderId = senderId,
            senderName = senderName,
            type = MessageType.LOVE_STICKER
        )
    }

    fun addReaction(messageId: String, emoji: String) {
        repository.addReaction(messageId, emoji)
    }

    fun answerDailyPrompt(answer: String) {
        repository.answerDailyPrompt(answer)
        showNotification("Answer saved! Partner answer revealed 🎉")
    }

    fun simulatePartnerDailyAnswer() {
        repository.simulatePartnerAnswerPrompt()
        showNotification("Jordan shared today's answer! ✨")
    }

    fun updateUserMood(mood: PartnerMood) {
        repository.updateUserMood(mood)
        showNotification("Your mood updated to: ${mood.emoji} ${mood.label}")
    }

    fun comfortPartner() {
        val partnerName = repository.profile.value.partnerName
        repository.sendMessage(
            text = "Wrapped you in a giant warm hug & sent warm lavender tea 🍵 Hugging you tight!",
            senderId = "user",
            senderName = repository.profile.value.userName,
            type = MessageType.TEXT
        )
        showNotification("Sent comfort message to $partnerName 🫂")
    }

    fun toggleDateIdeaStatus(id: String) {
        repository.toggleDateIdeaStatus(id)
    }

    fun addDateIdea(title: String, description: String, category: String, budget: String, emoji: String) {
        repository.addDateIdea(title, description, category, budget, emoji)
        showNotification("Date idea added to your wishlist! 🥂")
    }

    fun addMemory(title: String, description: String, date: String, location: String, emoji: String) {
        repository.addMemory(title, description, date, location, emoji)
        showNotification("Memory sealed in your love vault! 📸")
    }

    fun selectQuizOption(questionIndex: Int, optionIndex: Int) {
        repository.selectQuizOption(questionIndex, optionIndex)
    }

    fun resetQuiz() {
        repository.resetQuiz()
    }

    fun toggleBoostingGuideDone(id: String) {
        repository.toggleBoostingGuideDone(id)
    }

    fun updateProfile(userName: String, partnerName: String, anniversaryText: String) {
        repository.updateProfile(userName, partnerName, anniversaryText)
        showNotification("Couple profile updated! 💍")
    }

    fun generateFullHtmlMetaBlock(): String {
        val cfg = repository.metaConfig.value
        return """
<!-- Primary SoulSync SEO Meta Tags -->
<title>${cfg.title}</title>
<meta name="title" content="${cfg.title}" />
<meta name="description" content="${cfg.description}" />
<meta name="keywords" content="${cfg.keywordsString}" />
<meta name="robots" content="index, follow" />
<meta name="theme-color" content="#FF3B69" />
<link rel="canonical" href="${cfg.targetUrl}" />

<!-- Open Graph / Facebook / WhatsApp Preview -->
<meta property="og:type" content="website" />
<meta property="og:url" content="${cfg.targetUrl}" />
<meta property="og:site_name" content="${cfg.siteName}" />
<meta property="og:title" content="${cfg.title}" />
<meta property="og:description" content="${cfg.description}" />
<meta property="og:image" content="${cfg.ogImageUrl}" />

<!-- Twitter / X Card -->
<meta name="twitter:card" content="summary_large_image" />
<meta name="twitter:site" content="${cfg.twitterHandle}" />
<meta name="twitter:title" content="${cfg.title}" />
<meta name="twitter:description" content="${cfg.description}" />
<meta name="twitter:image" content="${cfg.ogImageUrl}" />

<!-- Schema.org JSON-LD Structured Data -->
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "WebApplication",
  "name": "SoulSync",
  "applicationCategory": "SocialNetworkingApplication",
  "operatingSystem": "Android, Web, iOS",
  "description": "${cfg.description}",
  "offers": {
    "@type": "Offer",
    "price": "0",
    "priceCurrency": "USD"
  },
  "aggregateRating": {
    "@type": "AggregateRating",
    "ratingValue": "4.9",
    "ratingCount": "12840"
  }
}
</script>
        """.trimIndent()
    }

    private fun triggerAutomatedPartnerReply(userText: String) {
        viewModelScope.launch {
            delay(1200)
            _uiState.update { it.copy(isPartnerTyping = true) }
            delay(2200)
            _uiState.update { it.copy(isPartnerTyping = false) }

            val reply = when {
                userText.contains("love", ignoreCase = true) -> "I love you more than words can say, sweetie! Forever and always ❤️"
                userText.contains("date", ignoreCase = true) || userText.contains("dinner", ignoreCase = true) -> "I can't wait for our date! Getting butterflies just thinking about seeing your smile tonight 🥰"
                userText.contains("miss", ignoreCase = true) -> "Miss you crazy much too! Counting down every second until I hold you 🫂"
                userText.contains("?", ignoreCase = true) -> "Whatever makes you happiest, I'm all in! You know I trust your taste completely ✨"
                else -> listOf(
                    "You just made my heart skip a beat 🥰",
                    "Sending you the biggest warmest kiss right now! 💋",
                    "Can't stop smiling at my phone reading this! Love you!",
                    "You are my absolute favorite human in the entire world 🌎❤️"
                ).random()
            }

            repository.sendMessage(
                text = reply,
                senderId = "partner",
                senderName = repository.profile.value.partnerName
            )
        }
    }

    fun showNotification(message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(userNotificationMessage = message) }
            delay(2800)
            if (_uiState.value.userNotificationMessage == message) {
                _uiState.update { it.copy(userNotificationMessage = null) }
            }
        }
    }

    fun clearNotification() {
        _uiState.update { it.copy(userNotificationMessage = null) }
    }
}
