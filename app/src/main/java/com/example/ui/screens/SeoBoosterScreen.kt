package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SeoAuditMetric
import com.example.data.model.TargetKeyword
import com.example.ui.theme.*
import com.example.ui.viewmodel.SoulSyncUiState
import com.example.ui.viewmodel.SoulSyncViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeoBoosterScreen(
    uiState: SoulSyncUiState,
    viewModel: SoulSyncViewModel
) {
    val context = LocalContext.current
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var showMetaCodeDialog by remember { mutableStateOf(false) }

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        viewModel.showNotification("Copied $label to clipboard! 📋")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NightBackground)
            .statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = OnlineTeal,
                        modifier = Modifier.size(8.dp)
                    ) {}
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "GROWTH & DISCOVERY ENGINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = GoldChampagne
                    )
                }
                Text(
                    text = "SEO & Website Boosting Suite",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = "Optimize search rankings, generate high-converting meta tags, and amplify partner viral loops.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Section Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = NightSurfaceCard,
                contentColor = RosePrimary,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = (selectedCategoryIndex == 0),
                    onClick = { selectedCategoryIndex = 0 },
                    text = { Text("Audit & Scorecard", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = (selectedCategoryIndex == 1),
                    onClick = { selectedCategoryIndex = 1 },
                    text = { Text("Meta Tags & Schema", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = (selectedCategoryIndex == 2),
                    onClick = { selectedCategoryIndex = 2 },
                    text = { Text("High-Yield Keywords", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = (selectedCategoryIndex == 3),
                    onClick = { selectedCategoryIndex = 3 },
                    text = { Text("Viral Invite Booster", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = (selectedCategoryIndex == 4),
                    onClick = { selectedCategoryIndex = 4 },
                    text = { Text("Boosting Playbook", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        when (selectedCategoryIndex) {
            0 -> {
                // Audit Scorecard
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("seo_audit_summary_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(OnlineTeal, GoldChampagne))
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Overall SEO & Web Health",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Based on Core Web Vitals, Schema, & ASO",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = OnlineTeal.copy(alpha = 0.2f),
                                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(OnlineTeal, OnlineTeal)))
                                ) {
                                    Text(
                                        text = "96 / 100 🔥",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OnlineTeal,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            LinearProgressIndicator(
                                progress = { 0.96f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = OnlineTeal,
                                trackColor = NightSurfaceVariant,
                            )
                        }
                    }
                }

                items(uiState.seoMetrics) { metric ->
                    SeoMetricCard(metric = metric)
                }
            }

            1 -> {
                // Meta Tags & Schema Generator
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("meta_generator_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(RosePrimary, SunsetPeach))
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
                                        text = "Production HTML Meta Block",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "OpenGraph, Twitter Cards, Schema.org JSON-LD",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        copyToClipboard(viewModel.generateFullHtmlMetaBlock(), "HTML Meta Tags")
                                    },
                                    modifier = Modifier.testTag("copy_meta_tags_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Tags",
                                        tint = GoldChampagne
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Preview Card
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = NightSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Search Engine Snippet Preview:",
                                        fontSize = 11.sp,
                                        color = GoldChampagne,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = uiState.metaConfig.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SunsetPeach
                                    )
                                    Text(
                                        text = uiState.metaConfig.targetUrl,
                                        fontSize = 11.sp,
                                        color = OnlineTeal
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = uiState.metaConfig.description,
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { showMetaCodeDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("View & Copy Full HTML Embed Code")
                            }
                        }
                    }
                }
            }

            2 -> {
                // High-Yield Keywords
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Target Keywords & Search Volume",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampagne
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "High-intent queries from couples actively seeking private messaging and bonding apps.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                items(uiState.targetKeywords) { kw ->
                    KeywordRowCard(keyword = kw)
                }
            }

            3 -> {
                // Viral Invite & Link Booster
                item {
                    val inviteLink = "https://soulsync.love/pair?code=${uiState.profile.connectionCode}"
                    val inviteText = "Hey my love! I created our private romantic couple space on SoulSync. Click to connect our chat, sync moods, and answer our daily questions 💕: $inviteLink"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("viral_invite_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne))
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🚀", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Viral Invite & Deep Link Engine",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Seamless one-tap couple pairing link with instant onboarding",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Your Unique Pair Link:",
                                            fontSize = 11.sp,
                                            color = SunsetPeach,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = inviteLink,
                                            fontSize = 13.sp,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    IconButton(
                                        onClick = { copyToClipboard(inviteLink, "Pairing Link") }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy Link",
                                            tint = RosePrimaryDark
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Pre-Crafted High-Conversion Message:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampagne
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = NightSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = inviteText,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { copyToClipboard(inviteText, "Invite Message") },
                                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Copy WhatsApp Copy 💬")
                                }
                            }
                        }
                    }
                }
            }

            4 -> {
                // Boosting Playbook
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceCard)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Website & App Boosting Playbook",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampagne
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Actionable roadmap to scale to 100k+ couple users.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                items(uiState.boostingGuides, key = { it.id }) { guide ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = guide.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SunsetPeach
                                )
                                Checkbox(
                                    checked = guide.isCompleted,
                                    onCheckedChange = { viewModel.toggleBoostingGuideDone(guide.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = OnlineTeal,
                                        uncheckedColor = TextMuted
                                    )
                                )
                            }
                            Text(
                                text = guide.title,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = guide.summary,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                guide.actionSteps.forEach { step ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Text("✓ ", color = OnlineTeal, fontSize = 12.sp)
                                        Text(step, fontSize = 11.sp, color = TextPrimary)
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

    // Full HTML Code Modal Dialog
    if (showMetaCodeDialog) {
        val fullHtml = viewModel.generateFullHtmlMetaBlock()
        AlertDialog(
            onDismissRequest = { showMetaCodeDialog = false },
            title = {
                Text(
                    text = "Production HTML Meta Block",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NightBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        item {
                            Text(
                                text = fullHtml,
                                color = GoldChampagne,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        copyToClipboard(fullHtml, "Complete HTML Meta Block")
                        showMetaCodeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Copy Code 📋")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMetaCodeDialog = false }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = NightSurface
        )
    }
}

@Composable
fun SeoMetricCard(metric: SeoAuditMetric) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            metric.score >= 95 -> OnlineTeal.copy(alpha = 0.2f)
                            metric.score >= 90 -> GoldChampagne.copy(alpha = 0.2f)
                            else -> SunsetPeach.copy(alpha = 0.2f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${metric.score}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                        metric.score >= 95 -> OnlineTeal
                        metric.score >= 90 -> GoldChampagne
                        else -> SunsetPeach
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = metric.title,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = metric.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (metric.status == "Optimized") OnlineTeal else GoldChampagne
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = metric.description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun KeywordRowCard(keyword: TargetKeyword) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NightSurfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = keyword.keyword,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Intent: ${keyword.intent}",
                        fontSize = 10.sp,
                        color = SunsetPeach
                    )
                    Text("•", fontSize = 10.sp, color = TextMuted)
                    Text(
                        text = "Diff: ${keyword.keywordDifficulty}",
                        fontSize = 10.sp,
                        color = if (keyword.keywordDifficulty == "Easy") OnlineTeal else GoldChampagne
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = keyword.monthlySearchVolume,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldChampagne,
                    fontSize = 13.sp
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (keyword.rankingPriority == "Critical") RosePrimary.copy(alpha = 0.2f) else NightSurfaceCard
                ) {
                    Text(
                        text = keyword.rankingPriority,
                        fontSize = 9.sp,
                        color = if (keyword.rankingPriority == "Critical") RosePrimaryDark else TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
