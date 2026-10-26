package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SoulSyncRepository {

    private val _messages = MutableStateFlow<List<ChatMessage>>(initialMessages())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _profile = MutableStateFlow(PartnerProfile())
    val profile: StateFlow<PartnerProfile> = _profile.asStateFlow()

    private val _dailyPrompt = MutableStateFlow(initialDailyPrompt())
    val dailyPrompt: StateFlow<DailyPrompt> = _dailyPrompt.asStateFlow()

    private val _dateIdeas = MutableStateFlow<List<DateIdea>>(initialDateIdeas())
    val dateIdeas: StateFlow<List<DateIdea>> = _dateIdeas.asStateFlow()

    private val _memories = MutableStateFlow<List<MemoryVaultItem>>(initialMemories())
    val memories: StateFlow<List<MemoryVaultItem>> = _memories.asStateFlow()

    private val _coupleQuiz = MutableStateFlow(initialCoupleQuiz())
    val coupleQuiz: StateFlow<CoupleQuiz> = _coupleQuiz.asStateFlow()

    private val _metaConfig = MutableStateFlow(WebMetaTagsConfig())
    val metaConfig: StateFlow<WebMetaTagsConfig> = _metaConfig.asStateFlow()

    private val _seoMetrics = MutableStateFlow(initialSeoMetrics())
    val seoMetrics: StateFlow<List<SeoAuditMetric>> = _seoMetrics.asStateFlow()

    private val _targetKeywords = MutableStateFlow(initialKeywords())
    val targetKeywords: StateFlow<List<TargetKeyword>> = _targetKeywords.asStateFlow()

    private val _boostingGuides = MutableStateFlow(initialBoostingGuides())
    val boostingGuides: StateFlow<List<BoostingGuideItem>> = _boostingGuides.asStateFlow()

    fun sendMessage(
        text: String,
        senderId: String = "user",
        senderName: String = "Taylor",
        type: MessageType = MessageType.TEXT,
        mediaUrl: String? = null,
        duration: Int = 0
    ) {
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}_${(100..999).random()}",
            senderId = senderId,
            senderName = senderName,
            text = text,
            timestamp = System.currentTimeMillis(),
            type = type,
            mediaUrl = mediaUrl,
            audioDurationSeconds = duration
        )
        _messages.update { it + newMsg }
    }

    fun addReaction(messageId: String, emoji: String) {
        _messages.update { list ->
            list.map { msg ->
                if (msg.id == messageId) {
                    msg.copy(reactionEmoji = if (msg.reactionEmoji == emoji) null else emoji)
                } else msg
            }
        }
    }

    fun answerDailyPrompt(userAnswer: String) {
        _dailyPrompt.update { prompt ->
            val updated = prompt.copy(
                userAnswer = userAnswer,
                isAnsweredByUser = true,
                isRevealed = prompt.isAnsweredByPartner
            )
            updated
        }
        // Also post into chat as prompt milestone
        sendMessage(
            text = "Answered today's Spark Question: \"$userAnswer\"",
            senderId = "user",
            senderName = _profile.value.userName,
            type = MessageType.PROMPT_ANSWER
        )
    }

    fun simulatePartnerAnswerPrompt(partnerAnswer: String = "Watching the sunset with you in Positano while eating gelato!") {
        _dailyPrompt.update { prompt ->
            prompt.copy(
                partnerAnswer = partnerAnswer,
                isAnsweredByPartner = true,
                isRevealed = prompt.isAnsweredByUser
            )
        }
        sendMessage(
            text = "Answered today's Spark Question: \"$partnerAnswer\"",
            senderId = "partner",
            senderName = _profile.value.partnerName,
            type = MessageType.PROMPT_ANSWER
        )
    }

    fun updateUserMood(mood: PartnerMood) {
        _profile.update { it.copy(userMood = mood) }
    }

    fun updatePartnerMood(mood: PartnerMood) {
        _profile.update { it.copy(partnerMood = mood) }
    }

    fun updateProfile(userName: String, partnerName: String, anniversaryText: String) {
        _profile.update {
            it.copy(
                userName = userName,
                partnerName = partnerName,
                anniversaryDateText = anniversaryText
            )
        }
    }

    fun toggleDateIdeaStatus(id: String) {
        _dateIdeas.update { list ->
            list.map { idea ->
                if (idea.id == id) {
                    val nextStatus = when (idea.status) {
                        DateStatus.WISHLIST -> DateStatus.PLANNED
                        DateStatus.PLANNED -> DateStatus.COMPLETED
                        DateStatus.COMPLETED -> DateStatus.WISHLIST
                    }
                    idea.copy(status = nextStatus)
                } else idea
            }
        }
    }

    fun addDateIdea(title: String, description: String, category: String, budget: String, emoji: String) {
        val newIdea = DateIdea(
            id = "date_${System.currentTimeMillis()}",
            title = title,
            description = description,
            category = category,
            estimatedBudget = budget,
            iconEmoji = emoji,
            status = DateStatus.WISHLIST
        )
        _dateIdeas.update { it + newIdea }
    }

    fun addMemory(title: String, description: String, date: String, location: String, emoji: String) {
        val newMem = MemoryVaultItem(
            id = "mem_${System.currentTimeMillis()}",
            title = title,
            date = date,
            description = description,
            emoji = emoji,
            location = location
        )
        _memories.update { listOf(newMem) + it }
    }

    fun selectQuizOption(questionIndex: Int, optionIndex: Int) {
        val current = _coupleQuiz.value
        val updatedQuestions = current.questions.mapIndexed { idx, q ->
            if (idx == questionIndex) {
                q.copy(userChoiceIndex = optionIndex)
            } else q
        }
        val allAnswered = updatedQuestions.all { it.userChoiceIndex != null }
        var score: Int? = null
        if (allAnswered) {
            // calculate match percentage against partner's pre-set answers
            val matches = updatedQuestions.count { it.userChoiceIndex == it.partnerChoiceIndex }
            score = ((matches.toDouble() / updatedQuestions.size) * 100).toInt()
        }
        _coupleQuiz.update {
            it.copy(
                questions = updatedQuestions,
                completed = allAnswered,
                matchScorePercent = score
            )
        }
    }

    fun resetQuiz() {
        _coupleQuiz.update { initialCoupleQuiz() }
    }

    fun toggleBoostingGuideDone(id: String) {
        _boostingGuides.update { list ->
            list.map { if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it }
        }
    }

    fun updateMetaConfig(config: WebMetaTagsConfig) {
        _metaConfig.value = config
    }

    private fun initialMessages(): List<ChatMessage> {
        val now = System.currentTimeMillis()
        val m = 60 * 1000L
        return listOf(
            ChatMessage(
                id = "m1",
                senderId = "partner",
                senderName = "Jordan",
                text = "Good morning my love! ☀️ Did you sleep well?",
                timestamp = now - (65 * m),
                reactionEmoji = "❤️"
            ),
            ChatMessage(
                id = "m2",
                senderId = "user",
                senderName = "Taylor",
                text = "Morning gorgeous! Yes, dreamt of our weekend getaway 🥰",
                timestamp = now - (58 * m),
                reactionEmoji = "✨"
            ),
            ChatMessage(
                id = "m3",
                senderId = "partner",
                senderName = "Jordan",
                text = "💓 Sent a Heartbeat Buzz to you",
                timestamp = now - (40 * m),
                type = MessageType.HEARTBEAT_TAP
            ),
            ChatMessage(
                id = "m4",
                senderId = "partner",
                senderName = "Jordan",
                text = "Listen to this note I recorded on my walk home 🍂",
                timestamp = now - (32 * m),
                type = MessageType.AUDIO_NOTE,
                audioDurationSeconds = 14
            ),
            ChatMessage(
                id = "m5",
                senderId = "user",
                senderName = "Taylor",
                text = "Your voice always makes my whole day better 🥺 Don't forget our date night reservation at 7:30 PM!",
                timestamp = now - (15 * m)
            ),
            ChatMessage(
                id = "m6",
                senderId = "partner",
                senderName = "Jordan",
                text = "Never! Already picked out the outfit you love. Check out today's Spark Question in Couple Space! 💌",
                timestamp = now - (5 * m),
                reactionEmoji = "🥂"
            )
        )
    }

    private fun initialDailyPrompt(): DailyPrompt {
        return DailyPrompt(
            id = "prompt_today",
            question = "What is the little quirk about me that always makes you smile without fail?",
            category = "Intimacy & Appreciation",
            userAnswer = "The way your eyes crinkle when you're laughing so hard you can't breathe! ❤️",
            partnerAnswer = "How you celebrate with a tiny victory dance whenever you open a stubborn jar or find our favorite snacks in the pantry! 🥰",
            isAnsweredByUser = true,
            isAnsweredByPartner = true,
            isRevealed = true
        )
    }

    private fun initialDateIdeas(): List<DateIdea> {
        return listOf(
            DateIdea(
                id = "d1",
                title = "Stargazing Blanket Picnic",
                description = "Pack warm cocoa in a thermos, grab thick quilts, and drive out to the scenic hilltop to watch shooting stars.",
                category = "Romantic",
                estimatedBudget = "$",
                iconEmoji = "✨",
                status = DateStatus.PLANNED,
                scheduledDate = "This Friday, 8:00 PM"
            ),
            DateIdea(
                id = "d2",
                title = "DIY Neapolitan Pizza Bake-Off",
                description = "Make homemade pizza dough from scratch, compete for the crispiest crust, and enjoy with vintage Chianti.",
                category = "Foodie",
                estimatedBudget = "$$",
                iconEmoji = "🍕",
                status = DateStatus.WISHLIST
            ),
            DateIdea(
                id = "d3",
                title = "Speakeasy Jazz & Vinyl Night",
                description = "Dress up in retro chic, find a hidden basement jazz bar, and sip smoked old fashioneds together.",
                category = "Adventure",
                estimatedBudget = "$$$",
                iconEmoji = "🎷",
                status = DateStatus.COMPLETED,
                scheduledDate = "Completed last month"
            ),
            DateIdea(
                id = "d4",
                title = "Living Room Blanket Fort & Ghibli Marathon",
                description = "Build an enormous fortress of chairs and fairy lights, order midnight ramen, and watch spirited away.",
                category = "Cozy Home",
                estimatedBudget = "$",
                iconEmoji = "🏰",
                status = DateStatus.WISHLIST
            )
        )
    }

    private fun initialMemories(): List<MemoryVaultItem> {
        return listOf(
            MemoryVaultItem(
                id = "mem1",
                title = "First Seaside Sunrise",
                date = "June 14, 2025",
                description = "We drove at 4:30 AM in the freezing fog. As soon as the golden sun broke over the waves, you leaned on my shoulder.",
                emoji = "🌅",
                location = "Big Sur Coast"
            ),
            MemoryVaultItem(
                id = "mem2",
                title = "The Rainy Day Bookshop Sanctuary",
                date = "October 22, 2025",
                description = "Got completely drenched in an autumn thunderstorm. Spent 3 hours tucked away in the philosophy aisle sharing a pastry.",
                emoji = "📚",
                location = "Cornerstone Books"
            ),
            MemoryVaultItem(
                id = "mem3",
                title = "Secret Anniversary Time Capsule",
                date = "Next Anniversary",
                description = "Contains our recorded voice messages and 10 handwritten promises. Locked until anniversary date!",
                emoji = "🔒",
                location = "Vault Encrypted",
                isLocked = true,
                lockUnlockDate = "Nov 18"
            )
        )
    }

    private fun initialCoupleQuiz(): CoupleQuiz {
        return CoupleQuiz(
            id = "quiz_couple_sync",
            title = "The Ultimate Couple Synchrony Test",
            subtitle = "See how deeply your preferences and memories align!",
            iconEmoji = "💘",
            questions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Where is your dream couples' sanctuary destination?",
                    options = listOf("Cozy Alpine Swiss Cabin 🏔️", "Sun-Drenched Amalfi Cliffside 🍋", "Kyoto Bamboo Forest Ryokan 🎋", "Bora Bora Overwater Bungalow 🏝️"),
                    userChoiceIndex = 0,
                    partnerChoiceIndex = 0
                ),
                QuizQuestion(
                    id = 2,
                    question = "What is our primary shared Love Language?",
                    options = listOf("Words of Affirmation 💌", "Deep Quality Time ⏳", "Comforting Physical Touch 🫂", "Thoughtful Acts of Care 🍵"),
                    userChoiceIndex = 1,
                    partnerChoiceIndex = 1
                ),
                QuizQuestion(
                    id = 3,
                    question = "If we have a completely free Sunday morning, we:",
                    options = listOf("Sleep in until noon tangled in bed 🛌", "Cook gourmet buttermilk pancakes together 🥞", "Go on an early sunrise coffee walk ☕", "Spontaneous day-trip road trip 🚗"),
                    userChoiceIndex = 1,
                    partnerChoiceIndex = 1
                ),
                QuizQuestion(
                    id = 4,
                    question = "Who is more likely to propose an impromptu midnight snack run?",
                    options = listOf("Definitely Jordan 🙋‍♂️", "Definitely Taylor 🙋‍♀️", "Equally culpable guilty partners! 🍕", "Neither, we always have snacks stocked 🥨"),
                    userChoiceIndex = 2,
                    partnerChoiceIndex = 2
                )
            ),
            completed = true,
            matchScorePercent = 100
        )
    }

    private fun initialSeoMetrics(): List<SeoAuditMetric> {
        return listOf(
            SeoAuditMetric(
                title = "Mobile PageSpeed & Core Web Vitals",
                score = 98,
                status = "Optimized",
                description = "LCP under 1.2s, CLS 0.00, FID < 25ms. Outstanding responsive rendering.",
                category = "Performance"
            ),
            SeoAuditMetric(
                title = "Meta Tags & OpenGraph Rich Cards",
                score = 100,
                status = "Optimized",
                description = "Complete OG title, description, high-res preview banners, Twitter cards configured.",
                category = "Meta & Social"
            ),
            SeoAuditMetric(
                title = "Schema.org App & Product JSON-LD",
                score = 95,
                status = "Optimized",
                description = "Structured data embedded for Google Knowledge Graph & Rich Snippet Stars.",
                category = "Structured Data"
            ),
            SeoAuditMetric(
                title = "Keyword Density & Search Intent Match",
                score = 92,
                status = "Good",
                description = "Top rankings targeted for 'couples chat app', 'partner messaging', 'relationship questions'.",
                category = "ASO & Search"
            )
        )
    }

    private fun initialKeywords(): List<TargetKeyword> {
        return listOf(
            TargetKeyword("best couple chat app", "49,500/mo", "Medium", "Commercial", "Critical"),
            TargetKeyword("partner messenger private", "27,100/mo", "Easy", "Transactional", "High"),
            TargetKeyword("relationship question of the day", "33,800/mo", "Easy", "Informational", "High"),
            TargetKeyword("long distance relationship space", "18,400/mo", "Medium", "Commercial", "High"),
            TargetKeyword("shared date night planner app", "12,200/mo", "Easy", "Transactional", "High"),
            TargetKeyword("private couple diary and chat", "9,800/mo", "Easy", "Commercial", "Secondary")
        )
    }

    private fun initialBoostingGuides(): List<BoostingGuideItem> {
        return listOf(
            BoostingGuideItem(
                id = "b1",
                category = "Search Engine Optimization (SEO)",
                title = "Google Search Indexing & Rich FAQ Schema",
                summary = "Deploy JSON-LD Structured Data with Question/Answer schema for 'Daily Couple Questions' to capture Google Rich Snippets in position #0.",
                actionSteps = listOf(
                    "Embed Schema.org 'SoftwareApplication' & 'FAQPage' inside index.html",
                    "Add canonical URL tag `<link rel='canonical' href='https://soulsync.love' />`",
                    "Submit sitemap.xml to Google Search Console and Bing Webmaster Tools",
                    "Add automated dynamic robots.txt allowing all major web crawlers"
                ),
                isCompleted = true
            ),
            BoostingGuideItem(
                id = "b2",
                category = "App Store Optimization (ASO)",
                title = "Google Play & App Store Organic Ranking",
                summary = "Optimize subtitle, keyword field, and screenshot graphic callouts to rank #1 for couples & relationship queries.",
                actionSteps = listOf(
                    "Include primary keywords in Title: 'SoulSync: Partner Chat & Space'",
                    "Front-load high-converting benefit bullet points in the first 160 characters",
                    "A/B test romantic couple screenshot banners with high contrast text badges",
                    "Encourage satisfied couple reviews after completing their first shared quiz"
                ),
                isCompleted = true
            ),
            BoostingGuideItem(
                id = "b3",
                category = "Viral & Social Boosting",
                title = "Couple Viral Referral Loop & TikTok Trend",
                summary = "Create frictionless two-way invites where pairing unlocks custom anniversary badges and quiz scores for both partners.",
                actionSteps = listOf(
                    "One-tap smart pairing link with automated clipboard detection",
                    "Auto-generate Instagram Story summary cards for anniversary milestones",
                    "Promote on Reddit communities (r/LongDistance, r/Couples, r/DatingOverThirty)",
                    "Launch Product Hunt and Indie Hackers showcase for relationship tech"
                ),
                isCompleted = false
            ),
            BoostingGuideItem(
                id = "b4",
                category = "Technical Web Speed Boosting",
                title = "Edge Caching & PWA Offline Service Worker",
                summary = "Ensure your chatting landing page and web client achieve sub-second load times worldwide via Cloudflare CDN.",
                actionSteps = listOf(
                    "Convert all media and preview cards to WebP and AVIF format",
                    "Enable HTTP/3 and brotli compression on web server",
                    "Set up Service Worker for offline instant page caching",
                    "Pre-connect to web font and websocket origins"
                ),
                isCompleted = false
            )
        )
    }
}
