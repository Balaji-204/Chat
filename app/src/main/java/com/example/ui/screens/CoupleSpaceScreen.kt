package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DateStatus
import com.example.data.model.PartnerMood
import com.example.ui.theme.*
import com.example.ui.viewmodel.SoulSyncUiState
import com.example.ui.viewmodel.SoulSyncViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoupleSpaceScreen(
    uiState: SoulSyncUiState,
    viewModel: SoulSyncViewModel
) {
    var showAnswerDialog by remember { mutableStateOf(false) }
    var answerInput by remember { mutableStateOf("") }
    var showMoodDialog by remember { mutableStateOf(false) }
    var showAddDateDialog by remember { mutableStateOf(false) }
    var showAddMemoryDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBackground)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Banner with Generated Romantic Asset
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.couple_hero_banner_1792986821449),
                    contentDescription = "Romantic Couple Under Starry Sky",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient scrim for text legibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    NightBackground.copy(alpha = 0.5f),
                                    NightBackground
                                )
                            )
                        )
                )

                // Overlaid Greeting & Bond Header
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = RosePrimary,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OUR INTIMATE SPACE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = GoldChampagne
                        )
                    }
                    Text(
                        text = "${uiState.profile.userName} & ${uiState.profile.partnerName}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        // Relationship Ticker & Spark Metrics
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("anniversary_metric_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(RosePrimary.copy(alpha = 0.6f), SunsetPeach.copy(alpha = 0.6f)))
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${uiState.profile.relationshipStartDateDays}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RosePrimaryDark
                        )
                        Text(
                            text = "Days in Love 💕",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(OutlineSubtle)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${uiState.profile.dailySparkPercent}%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldChampagne
                        )
                        Text(
                            text = "Couple Spark 🔥",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(OutlineSubtle)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nov 18",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunsetPeach
                        )
                        Text(
                            text = "Anniversary 💍",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Mood Sync Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mood & Emotional Battery Sync",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    TextButton(onClick = { showMoodDialog = true }) {
                        Text("Update Yours", color = SunsetPeach, fontSize = 12.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Your Mood Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showMoodDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("You", fontSize = 12.sp, color = TextMuted)
                                Text(
                                    "${uiState.profile.userMood.batteryPercent}% 🔋",
                                    fontSize = 11.sp,
                                    color = OnlineTeal
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${uiState.profile.userMood.emoji} ${uiState.profile.userMood.label}",
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = uiState.profile.userMood.description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }
                    }

                    // Partner's Mood Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(RosePrimary.copy(alpha = 0.4f), SunsetPeach.copy(alpha = 0.4f))))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(uiState.profile.partnerName, fontSize = 12.sp, color = RosePrimaryDark)
                                Text(
                                    "${uiState.profile.partnerMood.batteryPercent}% 🔋",
                                    fontSize = 11.sp,
                                    color = OnlineTeal
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${uiState.profile.partnerMood.emoji} ${uiState.profile.partnerMood.label}",
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = uiState.profile.partnerMood.description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FilledTonalButton(
                                onClick = { viewModel.comfortPartner() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = RosePrimary.copy(alpha = 0.2f),
                                    contentColor = RosePrimaryDark
                                )
                            ) {
                                Text("Send Hug 🫂", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Daily Spark Question Card
        item {
            uiState.dailyPrompt?.let { prompt ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("daily_prompt_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(GoldChampagne.copy(alpha = 0.6f), RosePrimary.copy(alpha = 0.6f))))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✨", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TODAY'S DAILY SPARK",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = GoldChampagne
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = NightSurfaceVariant
                            ) {
                                Text(
                                    text = prompt.category,
                                    fontSize = 10.sp,
                                    color = SunsetPeach,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = prompt.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Answers view
                        if (prompt.isRevealed) {
                            // Both answered
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = NightSurfaceVariant
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("Your Answer:", fontSize = 11.sp, color = SunsetPeach, fontWeight = FontWeight.Bold)
                                        Text(prompt.userAnswer.orEmpty(), fontSize = 13.sp, color = TextPrimary)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = RoseContainer.copy(alpha = 0.5f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("${uiState.profile.partnerName}'s Answer:", fontSize = 11.sp, color = RosePrimaryDark, fontWeight = FontWeight.Bold)
                                        Text(prompt.partnerAnswer.orEmpty(), fontSize = 13.sp, color = TextPrimary)
                                    }
                                }
                            }
                        } else {
                            // Locked or waiting
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NightSurfaceVariant)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (prompt.isAnsweredByUser) "Waiting for ${uiState.profile.partnerName} to answer to reveal both! 🔒" else "Answer to unlock your partner's response! 🔐",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                if (!prompt.isAnsweredByUser) {
                                    Button(
                                        onClick = { showAnswerDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                                    ) {
                                        Text("Write Your Answer ✍️")
                                    }
                                } else {
                                    FilledTonalButton(
                                        onClick = { viewModel.simulatePartnerDailyAnswer() }
                                    ) {
                                        Text("Simulate Partner Answer ✨")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Date Night Planner Header & Filter
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Date Night Wishlist & Planner",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Curate & complete romantic adventures together",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(
                        onClick = { showAddDateDialog = true },
                        modifier = Modifier.testTag("add_date_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Date",
                            tint = RosePrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    val categories = listOf("All", "Romantic", "Foodie", "Adventure", "Cozy Home")
                    items(categories) { cat ->
                        FilterChip(
                            selected = (selectedCategoryFilter == cat),
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RosePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = NightSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Date Ideas Cards
        val filteredDates = if (selectedCategoryFilter == "All") {
            uiState.dateIdeas
        } else {
            uiState.dateIdeas.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
        }

        items(filteredDates, key = { it.id }) { dateItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateItem.iconEmoji,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = dateItem.title,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NightSurfaceCard
                            ) {
                                Text(
                                    text = dateItem.estimatedBudget,
                                    fontSize = 10.sp,
                                    color = GoldChampagne,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dateItem.description,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                        dateItem.scheduledDate?.let { sched ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🗓️ $sched",
                                fontSize = 11.sp,
                                color = SunsetPeach,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Status toggle chip
                    Surface(
                        onClick = { viewModel.toggleDateIdeaStatus(dateItem.id) },
                        shape = RoundedCornerShape(12.dp),
                        color = when (dateItem.status) {
                            DateStatus.WISHLIST -> NightSurfaceCard
                            DateStatus.PLANNED -> SunsetPeach.copy(alpha = 0.2f)
                            DateStatus.COMPLETED -> OnlineTeal.copy(alpha = 0.2f)
                        },
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                when (dateItem.status) {
                                    DateStatus.WISHLIST -> listOf(OutlineSubtle, OutlineSubtle)
                                    DateStatus.PLANNED -> listOf(SunsetPeach, GoldChampagne)
                                    DateStatus.COMPLETED -> listOf(OnlineTeal, OnlineTeal)
                                }
                            )
                        )
                    ) {
                        Text(
                            text = when (dateItem.status) {
                                DateStatus.WISHLIST -> "Wishlist 💭"
                                DateStatus.PLANNED -> "Planned 🗓️"
                                DateStatus.COMPLETED -> "Done 🎉"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (dateItem.status) {
                                DateStatus.WISHLIST -> TextSecondary
                                DateStatus.PLANNED -> SunsetPeach
                                DateStatus.COMPLETED -> OnlineTeal
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Memory Vault Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Love Capsule & Memories",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Milestones and locked anniversary messages",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = { showAddMemoryDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Add Memory",
                            tint = SunsetPeach
                        )
                    }
                }
            }
        }

        // Memories List
        items(uiState.memories, key = { it.id }) { mem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = mem.emoji,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = mem.title,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = mem.date,
                                fontSize = 11.sp,
                                color = GoldChampagne
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "📍 ${mem.location}",
                            fontSize = 11.sp,
                            color = SunsetPeach
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mem.description,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        if (mem.isLocked) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NightSurfaceCard
                            ) {
                                Text(
                                    text = "🔒 Locked until ${mem.lockUnlockDate}",
                                    fontSize = 10.sp,
                                    color = RosePrimaryDark,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Answer Dialog
    if (showAnswerDialog) {
        AlertDialog(
            onDismissRequest = { showAnswerDialog = false },
            title = { Text("Answer Daily Spark", color = TextPrimary) },
            text = {
                OutlinedTextField(
                    value = answerInput,
                    onValueChange = { answerInput = it },
                    placeholder = { Text("Write your honest, loving answer...", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NightSurfaceVariant,
                        unfocusedContainerColor = NightSurfaceVariant,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (answerInput.isNotBlank()) {
                            viewModel.answerDailyPrompt(answerInput)
                            answerInput = ""
                            showAnswerDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Save & Reveal")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAnswerDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = NightSurface
        )
    }

    // Mood Selector Dialog
    if (showMoodDialog) {
        AlertDialog(
            onDismissRequest = { showMoodDialog = false },
            title = { Text("Select Your Current Mood", color = TextPrimary) },
            text = {
                val moods = listOf(
                    PartnerMood("🥰", "In Love & Cozy", "Counting down until we meet tonight", 95),
                    PartnerMood("🛋️", "Need Cuddles", "Craving a warm blanket and your embrace", 60),
                    PartnerMood("⚡", "Excited & Joyful", "Having a wonderful day, thinking of you!", 100),
                    PartnerMood("🥺", "Stressed / Busy", "Need a sweet message to get through the day", 40),
                    PartnerMood("🥱", "Sleepy Panda", "Ready for a phone call before bed", 50)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    moods.forEach { mood ->
                        Surface(
                            onClick = {
                                viewModel.updateUserMood(mood)
                                showMoodDialog = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = NightSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(mood.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(mood.label, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                    Text(mood.description, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showMoodDialog = false }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = NightSurface
        )
    }

    // Add Date Idea Dialog
    if (showAddDateDialog) {
        var title by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Romantic") }
        var budget by remember { mutableStateOf("$$") }
        var emoji by remember { mutableStateOf("🍷") }

        AlertDialog(
            onDismissRequest = { showAddDateDialog = false },
            title = { Text("New Date Night Idea", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Date Title (e.g. Sunset Kayaking)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description & Vibe") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = emoji,
                            onValueChange = { emoji = it },
                            label = { Text("Emoji") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = budget,
                            onValueChange = { budget = it },
                            label = { Text("Cost ($, $$, $$$)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addDateIdea(title, desc, category, budget, emoji)
                            showAddDateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Add to Wishlist")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDateDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = NightSurface
        )
    }

    // Add Memory Dialog
    if (showAddMemoryDialog) {
        var title by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("Today") }
        var location by remember { mutableStateOf("Our Secret Spot") }
        var desc by remember { mutableStateOf("") }
        var emoji by remember { mutableStateOf("📸") }

        AlertDialog(
            onDismissRequest = { showAddMemoryDialog = false },
            title = { Text("Seal a New Memory", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Memory Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Cherished Note / Memory Details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addMemory(title, desc, date, location, emoji)
                            showAddMemoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Seal in Vault")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMemoryDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = NightSurface
        )
    }
}
