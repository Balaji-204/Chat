package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.MessageType
import com.example.ui.theme.*
import com.example.ui.viewmodel.SoulSyncUiState
import com.example.ui.viewmodel.SoulSyncViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    uiState: SoulSyncUiState,
    viewModel: SoulSyncViewModel
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showStickerSheet by remember { mutableStateOf(false) }

    // Auto-scroll to bottom on new message
    LaunchedEffect(uiState.messages.size, uiState.isPartnerTyping) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(NightBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Surface(
                color = NightSurface,
                tonalElevation = 4.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Partner Avatar with Heart Badge
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(RosePrimary, SunsetPeach)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.profile.partnerName.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(OnlineTeal)
                                .border(2.dp, NightSurface, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = uiState.profile.partnerName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "♥️",
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = if (uiState.isPartnerTyping) "typing something sweet..." else uiState.profile.partnerStatusText,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (uiState.isPartnerTyping) RosePrimaryDark else TextSecondary,
                            maxLines = 1
                        )
                    }

                    // Persona switcher toggle (User vs Partner) for interactive test
                    AssistChip(
                        onClick = { viewModel.toggleActiveSender() },
                        label = {
                            Text(
                                text = if (uiState.activeSenderId == "user") "Typing as You" else "Typing as ${uiState.profile.partnerName}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Persona",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = NightSurfaceVariant,
                            labelColor = SunsetPeach,
                            leadingIconContentColor = SunsetPeach
                        ),
                        modifier = Modifier.testTag("switch_persona_button")
                    )
                }
            }

            // Quick Prompt Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NightSurfaceCard)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickPromptChip(
                    text = "💓 Heartbeat",
                    onClick = { viewModel.sendHeartbeat(context) },
                    modifier = Modifier.testTag("quick_heartbeat_chip")
                )
                QuickPromptChip(
                    text = "🎙️ Voice Note",
                    onClick = { viewModel.sendVoiceNote(12) },
                    modifier = Modifier.testTag("quick_voice_chip")
                )
                QuickPromptChip(
                    text = "💌 Miss You",
                    onClick = { viewModel.sendMessage("Missing your sweet smile right now! 🥰") },
                    modifier = Modifier.testTag("quick_miss_you_chip")
                )
                QuickPromptChip(
                    text = "🍕 Date Idea",
                    onClick = { viewModel.sendMessage("What are you craving for our next date night? Let's plan something spontaneous! ✨") },
                    modifier = Modifier.testTag("quick_date_chip")
                )
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.messages, key = { it.id }) { msg ->
                    MessageBubble(
                        message = msg,
                        isFromActiveUser = (msg.senderId == uiState.activeSenderId),
                        onReactionSelected = { emoji ->
                            viewModel.addReaction(msg.id, emoji)
                        }
                    )
                }

                if (uiState.isPartnerTyping) {
                    item {
                        TypingIndicatorRow(partnerName = uiState.profile.partnerName)
                    }
                }
            }

            // Bottom Input Bar
            Surface(
                color = NightSurface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sticker / Love tap button
                        IconButton(
                            onClick = { showStickerSheet = true },
                            modifier = Modifier
                                .size(42.dp)
                                .testTag("sticker_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Love Stickers",
                                tint = RosePrimary
                            )
                        }

                        // Text Field
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    "Whisper to ${if (uiState.activeSenderId == "user") uiState.profile.partnerName else uiState.profile.userName}...",
                                    color = TextMuted,
                                    fontSize = 14.sp
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 6.dp)
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = NightSurfaceVariant,
                                unfocusedContainerColor = NightSurfaceVariant,
                                focusedBorderColor = RosePrimary,
                                unfocusedBorderColor = OutlineSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            maxLines = 4,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (inputText.isNotBlank()) {
                                        viewModel.sendMessage(inputText)
                                        inputText = ""
                                    }
                                }
                            )
                        )

                        // Send Button
                        FilledIconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendMessage(inputText)
                                    inputText = ""
                                } else {
                                    viewModel.sendHeartbeat(context)
                                }
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = RosePrimary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("send_message_button")
                        ) {
                            Icon(
                                imageVector = if (inputText.isNotBlank()) Icons.AutoMirrored.Filled.Send else Icons.Default.Favorite,
                                contentDescription = if (inputText.isNotBlank()) "Send Message" else "Send Heartbeat"
                            )
                        }
                    }
                }
            }
        }

        // Animated Floating Heart when Heartbeat is sent
        AnimatedVisibility(
            visible = uiState.showHeartAnimation,
            enter = scaleIn(animationSpec = spring(dampingRatio = 0.5f)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NightSurface.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(32.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(RosePrimary, GoldChampagne))),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "heart")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.9f,
                        targetValue = 1.3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(400, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale"
                    )

                    Text(
                        text = "💓",
                        fontSize = 56.sp,
                        modifier = Modifier.scale(scale)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Heartbeat Buzz Sent!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Your partner felt your loving pulse",
                        style = MaterialTheme.typography.bodySmall,
                        color = SunsetPeach
                    )
                }
            }
        }

        // Love Sticker Bottom Sheet
        if (showStickerSheet) {
            ModalBottomSheet(
                onDismissRequest = { showStickerSheet = false },
                containerColor = NightSurface,
                dragHandle = { BottomSheetDefaults.DragHandle(color = OutlineSubtle) }
            ) {
                LoveStickersContent(
                    onSelect = { emoji, label ->
                        viewModel.sendLoveSticker(emoji, label)
                        showStickerSheet = false
                    }
                )
            }
        }
    }
}

@Composable
fun QuickPromptChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = NightSurfaceVariant,
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(RosePrimary.copy(alpha = 0.5f), SunsetPeach.copy(alpha = 0.5f)))),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    isFromActiveUser: Boolean,
    onReactionSelected: (String) -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    var showReactionMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalAlignment = if (isFromActiveUser) Alignment.End else Alignment.Start
    ) {
        // Sender Name Header for clarity
        Text(
            text = message.senderName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isFromActiveUser) RosePrimaryDark else SunsetPeach,
            modifier = Modifier.padding(bottom = 2.dp, start = 8.dp, end = 8.dp)
        )

        // Bubble Content
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isFromActiveUser) 18.dp else 4.dp,
                        bottomEnd = if (isFromActiveUser) 4.dp else 18.dp
                    )
                )
                .background(
                    if (isFromActiveUser) {
                        Brush.linearGradient(listOf(RosePrimary, Color(0xFFD81B60)))
                    } else {
                        Brush.linearGradient(listOf(NightSurfaceVariant, NightSurfaceCard))
                    }
                )
                .clickable { showReactionMenu = !showReactionMenu }
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                when (message.type) {
                    MessageType.TEXT -> {
                        Text(
                            text = message.text,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                    MessageType.AUDIO_NOTE -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Play Voice Note",
                                tint = GoldChampagne,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Voice Note (${message.audioDurationSeconds}s)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "━━━━━●━━━━━",
                                    fontSize = 11.sp,
                                    color = GoldChampagne
                                )
                            }
                        }
                    }
                    MessageType.HEARTBEAT_TAP -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💓", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = message.text,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                    MessageType.PROMPT_ANSWER -> {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "✨", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Daily Spark Answer",
                                    fontSize = 11.sp,
                                    color = GoldChampagne,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = message.text,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    MessageType.LOVE_STICKER -> {
                        Text(
                            text = message.text,
                            fontSize = 28.sp
                        )
                    }
                    else -> {
                        Text(text = message.text, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Time and delivery status
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeFormat.format(Date(message.timestamp)),
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    if (isFromActiveUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Read",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        // Reaction Badge if present
        message.reactionEmoji?.let { emoji ->
            Surface(
                shape = CircleShape,
                color = NightSurface,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(RosePrimary, GoldChampagne))),
                modifier = Modifier
                    .offset(y = (-8).dp)
                    .clickable { onReactionSelected(emoji) }
            ) {
                Text(
                    text = emoji,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Quick Reaction Picker Menu
        AnimatedVisibility(visible = showReactionMenu) {
            Surface(
                color = NightSurfaceCard,
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(RosePrimary, SunsetPeach))),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("❤️", "🥰", "🔥", "🥺", "🥂", "💋").forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    onReactionSelected(emoji)
                                    showReactionMenu = false
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicatorRow(partnerName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = NightSurfaceVariant,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$partnerName is typing",
                    fontSize = 12.sp,
                    color = SunsetPeach,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                val infiniteTransition = rememberInfiniteTransition(label = "dots")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "alpha"
                )
                Text(
                    text = "•••",
                    fontSize = 14.sp,
                    color = RosePrimary.copy(alpha = alpha),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LoveStickersContent(
    onSelect: (String, String) -> Unit
) {
    val stickers = listOf(
        Pair("🧸", "Big Bear Hug"),
        Pair("🌹", "A Rose for You"),
        Pair("☕", "Morning Coffee Date"),
        Pair("🍕", "Pizza & Chill"),
        Pair("🍿", "Movie Night"),
        Pair("💫", "Make a Wish"),
        Pair("🥂", "Cheers to Us"),
        Pair("🏖️", "Getaway Dream"),
        Pair("💌", "Love Note"),
        Pair("💍", "Forever & Always"),
        Pair("✨", "You Sparkle"),
        Pair("🎶", "Our Special Song")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = "Send a Sweet Romantic Sticker",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            stickers.take(4).forEach { (emoji, label) ->
                StickerItem(emoji, label, onSelect)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            stickers.drop(4).take(4).forEach { (emoji, label) ->
                StickerItem(emoji, label, onSelect)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            stickers.drop(8).take(4).forEach { (emoji, label) ->
                StickerItem(emoji, label, onSelect)
            }
        }
    }
}

@Composable
fun StickerItem(
    emoji: String,
    label: String,
    onSelect: (String, String) -> Unit
) {
    Surface(
        onClick = { onSelect(emoji, label) },
        shape = RoundedCornerShape(16.dp),
        color = NightSurfaceVariant,
        modifier = Modifier.size(72.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Text(
                text = label,
                fontSize = 9.sp,
                color = TextSecondary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
