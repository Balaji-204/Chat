package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.SoulSyncUiState
import com.example.ui.viewmodel.SoulSyncViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SoulSyncUiState,
    viewModel: SoulSyncViewModel
) {
    val context = LocalContext.current
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var userNameInput by remember { mutableStateOf(uiState.profile.userName) }
    var partnerNameInput by remember { mutableStateOf(uiState.profile.partnerName) }
    var anniversaryInput by remember { mutableStateOf(uiState.profile.anniversaryDateText) }

    var heartbeatsEnabled by remember { mutableStateOf(true) }
    var dailySparkPushEnabled by remember { mutableStateOf(true) }
    var soundEffectsEnabled by remember { mutableStateOf(true) }

    fun copyCode(code: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Pairing Code", code)
        clipboard.setPrimaryClip(clip)
        viewModel.showNotification("Partner code copied to clipboard! 📋")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBackground)
            .statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen Title
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "PARTNER SETTINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = GoldChampagne
                )
                Text(
                    text = "Connection & Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = "Manage your paired couple bond, nicknames, and privacy settings",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Connection Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pair_connection_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne))
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(RosePrimary, SunsetPeach))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("♥️", fontSize = 22.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(OnlineTeal)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Paired with ${uiState.profile.partnerName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Connected & Synced in Real-Time",
                                    fontSize = 12.sp,
                                    color = OnlineTeal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pair Code Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NightSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
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
                                    text = "Your Private Couple Pairing Code:",
                                    fontSize = 11.sp,
                                    color = SunsetPeach
                                )
                                Text(
                                    text = uiState.profile.connectionCode,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GoldChampagne,
                                    letterSpacing = 2.sp
                                )
                            }
                            IconButton(onClick = { copyCode(uiState.profile.connectionCode) }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Code",
                                    tint = GoldChampagne
                                )
                            }
                        }
                    }
                }
            }
        }

        // Profile Details Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Couple Identity",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        TextButton(onClick = { showEditProfileDialog = true }) {
                            Text("Edit", color = SunsetPeach)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileRow("Your Display Name", uiState.profile.userName)
                    HorizontalDivider(color = OutlineSubtle.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))
                    ProfileRow("Partner's Display Name", uiState.profile.partnerName)
                    HorizontalDivider(color = OutlineSubtle.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))
                    ProfileRow("Anniversary Reminder", uiState.profile.anniversaryDateText)
                }
            }
        }

        // Preferences & Notification Toggles
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bond Experience & Notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    PreferenceSwitchRow(
                        title = "Heartbeat Haptic Buzz",
                        subtitle = "Gentle physical vibration when your partner taps you",
                        checked = heartbeatsEnabled,
                        onCheckedChange = { heartbeatsEnabled = it }
                    )
                    HorizontalDivider(color = OutlineSubtle.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))
                    PreferenceSwitchRow(
                        title = "Daily Spark Reminders",
                        subtitle = "Prompt to answer the couple question together",
                        checked = dailySparkPushEnabled,
                        onCheckedChange = { dailySparkPushEnabled = it }
                    )
                    HorizontalDivider(color = OutlineSubtle.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))
                    PreferenceSwitchRow(
                        title = "Sweet Sound Chimes",
                        subtitle = "Play gentle bell sound on partner messages",
                        checked = soundEffectsEnabled,
                        onCheckedChange = { soundEffectsEnabled = it }
                    )
                }
            }
        }

        // Privacy & Encryption Note
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NightSurfaceCard)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔒", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "End-to-End Private Space",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Only you and your partner have the keys to your intimate chats, photos, and answers.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Couple Details", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = userNameInput,
                        onValueChange = { userNameInput = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = partnerNameInput,
                        onValueChange = { partnerNameInput = it },
                        label = { Text("Partner Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = anniversaryInput,
                        onValueChange = { anniversaryInput = it },
                        label = { Text("Anniversary Note") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(userNameInput, partnerNameInput, anniversaryInput)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = NightSurface
        )
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

@Composable
fun PreferenceSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = RosePrimary,
                uncheckedTrackColor = NightSurfaceCard
            )
        )
    }
}
