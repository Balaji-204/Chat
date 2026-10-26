package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.SoulSyncUiState
import com.example.ui.viewmodel.SoulSyncViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    uiState: SoulSyncUiState,
    viewModel: SoulSyncViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var currentTruthOrDare by remember { mutableStateOf<Pair<String, String>?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBackground)
            .statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Header
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "BOND & COUPLE GAMES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = GoldChampagne
                )
                Text(
                    text = "Quizzes & Love Spark",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = "Test your compatibility, discover love languages, and spark joy",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Sub Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NightSurfaceCard,
                contentColor = RosePrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = (selectedTab == 0),
                    onClick = { selectedTab = 0 },
                    text = { Text("Couple Quiz 💘", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = (selectedTab == 1),
                    onClick = { selectedTab = 1 },
                    text = { Text("Love Languages 💌", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = (selectedTab == 2),
                    onClick = { selectedTab = 2 },
                    text = { Text("Truth or Dare 🔥", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Quiz Content
                uiState.coupleQuiz?.let { quiz ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quiz_header_card"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne))
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = quiz.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = quiz.subtitle,
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    Text(quiz.iconEmoji, fontSize = 32.sp)
                                }

                                if (quiz.completed && quiz.matchScorePercent != null) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = NightSurfaceVariant,
                                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne)))
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Synchrony Score: ${quiz.matchScorePercent}%",
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = GoldChampagne,
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = if (quiz.matchScorePercent >= 90) "Twin Flames! Soulmates in harmony 🔥" else "Great minds love alike! 💕",
                                                    fontSize = 12.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                            TextButton(onClick = { viewModel.resetQuiz() }) {
                                                Text("Retake", color = SunsetPeach, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Questions List
                    items(quiz.questions.size) { qIdx ->
                        val q = quiz.questions[qIdx]
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Question ${q.id} of ${quiz.questions.size}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SunsetPeach
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = q.question,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                q.options.forEachIndexed { optIdx, optionText ->
                                    val isSelectedByUser = (q.userChoiceIndex == optIdx)
                                    val isSelectedByPartner = (q.partnerChoiceIndex == optIdx)

                                    Surface(
                                        onClick = { viewModel.selectQuizOption(qIdx, optIdx) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelectedByUser) RosePrimary.copy(alpha = 0.25f) else NightSurfaceCard,
                                        border = if (isSelectedByUser) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne))) else null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelectedByUser,
                                                onClick = { viewModel.selectQuizOption(qIdx, optIdx) },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = RosePrimary,
                                                    unselectedColor = TextMuted
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = optionText,
                                                fontSize = 13.sp,
                                                color = if (isSelectedByUser) Color.White else TextSecondary,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (isSelectedByPartner) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = SunsetPeach.copy(alpha = 0.2f)
                                                ) {
                                                    Text(
                                                        text = "Jordan's pick ✨",
                                                        fontSize = 9.sp,
                                                        color = SunsetPeach,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Love Languages Deep Dive
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "The 5 Love Languages for Couples",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampagne
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Understanding how each other gives and receives love brings you exponentially closer.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                val languages = listOf(
                    Triple("Words of Affirmation 💌", "Compliments, loving texts, and verbal appreciation.", "Send unexpected heartfelt texts during the day reminding them why you appreciate them."),
                    Triple("Quality Time ⏳", "Undivided attention without phones or distractions.", "Set up a 30-minute uninterrupted phone-free evening stroll or kitchen dance."),
                    Triple("Physical Touch 🫂", "Hugs, holding hands, cuddles, and loving proximity.", "Wrap your partner in a gentle hug that lasts for 20 seconds to release oxytocin."),
                    Triple("Acts of Service 🍵", "Actions that ease their burden and show care.", "Brew their morning espresso or clean up without being asked."),
                    Triple("Receiving Gifts 🎁", "Thoughtful tokens representing visual symbols of love.", "Surprise them with their favorite pastry or a handwritten secret note.")
                )

                items(languages.size) { idx ->
                    val (title, summary, tip) = languages[idx]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(summary, fontSize = 12.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NightSurfaceCard
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("💡", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tip,
                                        fontSize = 11.sp,
                                        color = SunsetPeach
                                    )
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Truth or Dare
                item {
                    val truths = listOf(
                        "What was the exact moment you realized you had fallen head-over-heels for me?",
                        "What is one secret daydream or romantic scenario you've never confessed to me?",
                        "If we could drop everything and live anywhere on Earth for a year, where would you take us?",
                        "What is the sweetest thing I've ever done that made your stomach flutter?"
                    )
                    val dares = listOf(
                        "Give me a gentle 3-minute neck and shoulder massage with no speaking allowed! 💆",
                        "Look deeply into my eyes for 60 seconds without laughing or looking away. 💕",
                        "Sing the chorus of our favorite couple song into a voice note right now! 🎶",
                        "Feed me a bite of your favorite snack like royalty. 🍓"
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(RosePrimary, SunsetPeach)))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Couple Truth or Dare 🔥",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Intimate, flirty, and playful challenges for two.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { currentTruthOrDare = Pair("TRUTH 💭", truths.random()) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SunsetPeach),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Draw Truth")
                                }
                                Button(
                                    onClick = { currentTruthOrDare = Pair("DARE 🔥", dares.random()) },
                                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Draw Dare")
                                }
                            }

                            currentTruthOrDare?.let { (type, prompt) ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = NightSurfaceVariant,
                                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne))),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = type,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = GoldChampagne,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = prompt,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
