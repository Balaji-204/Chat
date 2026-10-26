package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.SoulSyncViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                val viewModel: SoulSyncViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                // Handle system back navigation
                if (uiState.currentTab != AppNavTab.CHAT) {
                    BackHandler {
                        viewModel.selectTab(AppNavTab.CHAT)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NightBackground)
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = NightBackground,
                        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        bottomBar = {
                            NavigationBar(
                                containerColor = NightSurface,
                                tonalElevation = 8.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("main_bottom_nav_bar"),
                                windowInsets = WindowInsets.navigationBars
                            ) {
                                NavItem(
                                    tab = AppNavTab.CHAT,
                                    currentTab = uiState.currentTab,
                                    label = "Chat",
                                    selectedIcon = Icons.Filled.ChatBubble,
                                    unselectedIcon = Icons.Outlined.ChatBubbleOutline,
                                    badgeCount = if (uiState.isPartnerTyping) 1 else null,
                                    onSelect = { viewModel.selectTab(AppNavTab.CHAT) },
                                    testTag = "nav_tab_chat"
                                )
                                NavItem(
                                    tab = AppNavTab.COUPLE_SPACE,
                                    currentTab = uiState.currentTab,
                                    label = "Space",
                                    selectedIcon = Icons.Filled.Favorite,
                                    unselectedIcon = Icons.Outlined.FavoriteBorder,
                                    onSelect = { viewModel.selectTab(AppNavTab.COUPLE_SPACE) },
                                    testTag = "nav_tab_space"
                                )
                                NavItem(
                                    tab = AppNavTab.QUIZ,
                                    currentTab = uiState.currentTab,
                                    label = "Games",
                                    selectedIcon = Icons.Filled.Gamepad,
                                    unselectedIcon = Icons.Outlined.SportsEsports,
                                    onSelect = { viewModel.selectTab(AppNavTab.QUIZ) },
                                    testTag = "nav_tab_games"
                                )
                                NavItem(
                                    tab = AppNavTab.SEO_BOOSTER,
                                    currentTab = uiState.currentTab,
                                    label = "SEO & Boost",
                                    selectedIcon = Icons.Filled.RocketLaunch,
                                    unselectedIcon = Icons.Outlined.RocketLaunch,
                                    onSelect = { viewModel.selectTab(AppNavTab.SEO_BOOSTER) },
                                    testTag = "nav_tab_seo"
                                )
                                NavItem(
                                    tab = AppNavTab.SETTINGS,
                                    currentTab = uiState.currentTab,
                                    label = "Settings",
                                    selectedIcon = Icons.Filled.Settings,
                                    unselectedIcon = Icons.Outlined.Settings,
                                    onSelect = { viewModel.selectTab(AppNavTab.SETTINGS) },
                                    testTag = "nav_tab_settings"
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = innerPadding.calculateBottomPadding())
                        ) {
                            AnimatedContent(
                                targetState = uiState.currentTab,
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut()
                                },
                                label = "ScreenTransition"
                            ) { tab ->
                                when (tab) {
                                    AppNavTab.CHAT -> ChatScreen(uiState = uiState, viewModel = viewModel)
                                    AppNavTab.COUPLE_SPACE -> CoupleSpaceScreen(uiState = uiState, viewModel = viewModel)
                                    AppNavTab.QUIZ -> QuizScreen(uiState = uiState, viewModel = viewModel)
                                    AppNavTab.SEO_BOOSTER -> SeoBoosterScreen(uiState = uiState, viewModel = viewModel)
                                    AppNavTab.SETTINGS -> SettingsScreen(uiState = uiState, viewModel = viewModel)
                                }
                            }
                        }
                    }

                    // Floating Notification Pill
                    AnimatedVisibility(
                        visible = uiState.userNotificationMessage != null,
                        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .statusBarsPadding()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        uiState.userNotificationMessage?.let { msg ->
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = NightSurfaceCard,
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.horizontalGradient(listOf(RosePrimary, GoldChampagne))
                                ),
                                shadowElevation = 12.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = msg,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
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

@Composable
fun RowScope.NavItem(
    tab: AppNavTab,
    currentTab: AppNavTab,
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    badgeCount: Int? = null,
    onSelect: () -> Unit,
    testTag: String
) {
    val isSelected = (tab == currentTab)
    NavigationBarItem(
        selected = isSelected,
        onClick = onSelect,
        icon = {
            BadgedBox(
                badge = {
                    if (badgeCount != null && badgeCount > 0) {
                        Badge(
                            containerColor = RosePrimary,
                            contentColor = Color.White
                        ) {
                            Text("$badgeCount")
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                    contentDescription = label
                )
            }
        },
        label = {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = RosePrimary,
            selectedTextColor = RosePrimary,
            indicatorColor = RoseContainer.copy(alpha = 0.5f),
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted
        ),
        modifier = Modifier.testTag(testTag)
    )
}
