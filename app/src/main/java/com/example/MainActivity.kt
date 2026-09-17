package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AIModel
import com.example.data.model.AttachmentItem
import com.example.data.model.NavPage
import com.example.ui.components.NatnaelSidebar
import com.example.ui.components.NatnaelTopBar
import com.example.ui.screens.ChatWorkspaceScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FilesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ImagesGalleryScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NatnaelAITheme
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo
import com.example.ui.viewmodel.NatnaelAIViewModel
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NatnaelAIViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()

            NatnaelAITheme(themeMode = themeMode) {
                NatnaelAIApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NatnaelAIApp(viewModel: NatnaelAIViewModel) {
    val context = LocalContext.current
    val currentPage by viewModel.currentNavPage.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val isSidebarExpanded by viewModel.isSidebarExpanded.collectAsState()
    val isProfileMenuOpen by viewModel.isProfileMenuOpen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()

    val allChats by viewModel.allChats.collectAsState()
    val activeChatId by viewModel.activeChatId.collectAsState()
    val currentMessages by viewModel.currentMessages.collectAsState()
    val composerText by viewModel.composerText.collectAsState()
    val isWebResearchMode by viewModel.isWebResearchMode.collectAsState()
    val attachments by viewModel.attachments.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val isListeningVoice by viewModel.isListeningVoice.collectAsState()

    val allImages by viewModel.allImages.collectAsState()
    val allFiles by viewModel.allFiles.collectAsState()

    val isGlobalSearchOpen by viewModel.isGlobalSearchOpen.collectAsState()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsState()

    val activeChat = allChats.firstOrNull { it.id == activeChatId }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ANIMATED LEFT SIDEBAR
            NatnaelSidebar(
                currentPage = currentPage,
                isExpanded = isSidebarExpanded,
                isProfileMenuOpen = isProfileMenuOpen,
                userProfile = userProfile,
                onSelectPage = { viewModel.selectNavPage(it) },
                onToggleCollapse = { viewModel.toggleSidebar() },
                onToggleProfileMenu = { viewModel.toggleProfileMenu() },
                onDismissProfileMenu = { viewModel.dismissProfileMenu() }
            )

            // MAIN WORKSPACE CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // TOP STICKY BAR
                NatnaelTopBar(
                    currentPage = currentPage,
                    themeMode = themeMode,
                    userProfile = userProfile,
                    onToggleSidebar = { viewModel.toggleSidebar() },
                    onToggleTheme = { viewModel.toggleTheme() },
                    onOpenSearch = { viewModel.setGlobalSearchOpen(true) },
                    onOpenNotifications = { viewModel.setNotificationsOpen(true) },
                    onProfileClick = { viewModel.selectNavPage(NavPage.SETTINGS) }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // SCREEN ROUTING
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (currentPage) {
                        NavPage.HOME -> {
                            HomeScreen(
                                userProfile = userProfile,
                                recentChats = allChats,
                                onSelectChat = { viewModel.selectChat(it) },
                                onStartNewChat = { viewModel.startNewChat() },
                                onNavigate = { viewModel.selectNavPage(it) },
                                onPromptSelected = { prompt, model ->
                                    viewModel.setSelectedModel(model)
                                    viewModel.startNewChat()
                                    viewModel.sendMessage(prompt)
                                }
                            )
                        }

                        NavPage.NEW_CHAT, NavPage.CHAT_HISTORY -> {
                            ChatWorkspaceScreen(
                                chatTitle = activeChat?.title ?: "Chat with Natnael AI",
                                messages = currentMessages,
                                selectedModel = selectedModel,
                                onSelectModel = { viewModel.setSelectedModel(it) },
                                composerText = composerText,
                                onComposerTextChange = { viewModel.setComposerText(it) },
                                isWebResearchMode = isWebResearchMode,
                                onToggleWebResearch = { viewModel.toggleWebResearchMode() },
                                attachments = attachments,
                                onRemoveAttachment = { viewModel.removeAttachment(it) },
                                isGenerating = isGenerating,
                                isListeningVoice = isListeningVoice,
                                onToggleVoice = { viewModel.toggleVoiceInput() },
                                onSendMessage = { viewModel.sendMessage() },
                                onStopGenerating = { viewModel.stopGenerating() },
                                onOpenImageGenerator = { viewModel.selectNavPage(NavPage.IMAGES) },
                                onUploadSampleFile = {
                                    viewModel.addAttachment(
                                        AttachmentItem(
                                            id = UUID.randomUUID().toString(),
                                            name = "Ethiopian_Tech_Overview_2026.pdf",
                                            size = "1.8 MB",
                                            type = "PDF"
                                        )
                                    )
                                    Toast.makeText(context, "Attached document for AI analysis", Toast.LENGTH_SHORT).show()
                                },
                                onUploadSampleImage = {
                                    viewModel.addAttachment(
                                        AttachmentItem(
                                            id = UUID.randomUUID().toString(),
                                            name = "Diagram_Architecture.png",
                                            size = "940 KB",
                                            type = "IMAGE"
                                        )
                                    )
                                    Toast.makeText(context, "Attached image for vision analysis", Toast.LENGTH_SHORT).show()
                                },
                                onClearChat = { viewModel.clearHistory() },
                                onDeleteChat = {
                                    if (activeChatId.isNotBlank()) {
                                        viewModel.deleteChat(activeChatId)
                                    }
                                },
                                onStartNewChat = { viewModel.startNewChat() }
                            )
                        }

                        NavPage.IMAGES -> {
                            ImagesGalleryScreen(
                                images = allImages,
                                onGenerateImage = { prompt, style, ratio ->
                                    viewModel.generateImage(prompt, style, ratio)
                                },
                                onToggleFavorite = { viewModel.toggleImageFavorite(it) }
                            )
                        }

                        NavPage.FILES -> {
                            FilesScreen(
                                files = allFiles,
                                onCreateFile = { name, format, content ->
                                    viewModel.createGeneratedFile(name, format, content)
                                },
                                onDeleteFile = { viewModel.deleteFile(it) }
                            )
                        }

                        NavPage.EXPLORE -> {
                            ExploreScreen(
                                onSelectPrompt = { prompt, model ->
                                    viewModel.setSelectedModel(model)
                                    viewModel.startNewChat()
                                    viewModel.sendMessage(prompt)
                                }
                            )
                        }

                        NavPage.PROJECTS -> {
                            ProjectsScreen(
                                onSelectProject = { proj ->
                                    viewModel.startNewChat()
                                    viewModel.sendMessage("Open project workspace: ${proj.title}. Show project code files and live sandbox.")
                                }
                            )
                        }

                        NavPage.SETTINGS -> {
                            SettingsScreen(
                                userProfile = userProfile,
                                themeMode = themeMode,
                                onSelectTheme = { viewModel.setThemeMode(it) },
                                selectedModel = selectedModel,
                                onSelectDefaultModel = { viewModel.setSelectedModel(it) },
                                onClearHistory = { viewModel.clearHistory() }
                            )
                        }
                    }
                }
            }
        }
    }

    // GLOBAL SEARCH MODAL DIALOG
    if (isGlobalSearchOpen) {
        var searchQuery by remember { mutableStateOf("") }
        val filteredChats = remember(searchQuery, allChats) {
            if (searchQuery.isBlank()) allChats
            else allChats.filter { it.title.contains(searchQuery, ignoreCase = true) || it.lastMessage.contains(searchQuery, ignoreCase = true) }
        }

        AlertDialog(
            onDismissRequest = { viewModel.setGlobalSearchOpen(false) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = NatnaelPrimaryIndigo)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Search Natnael AI Workspace", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Type to search chats, documents, prompts...", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        modifier = Modifier.height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredChats) { chat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        viewModel.setGlobalSearchOpen(false)
                                        viewModel.selectChat(chat.id)
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = NatnaelPrimaryIndigo, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(chat.title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.setGlobalSearchOpen(false) }) {
                    Text("Close")
                }
            }
        )
    }

    // NOTIFICATIONS DIALOG
    if (isNotificationsOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.setNotificationsOpen(false) },
            title = {
                Text("Workspace Notifications", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NotificationItem(
                        title = "Live Code Sandbox Ready",
                        subtitle = "Interactive preview compiled with real-time DOM updates.",
                        time = "5m ago"
                    )
                    NotificationItem(
                        title = "Web Research Index Updated",
                        subtitle = "Synthesized 6 academic and global technology sources.",
                        time = "1h ago"
                    )
                    NotificationItem(
                        title = "AI Document Generated",
                        subtitle = "Executive Research Report exported successfully.",
                        time = "3h ago"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.setNotificationsOpen(false) }) {
                    Text("Dismiss All")
                }
            }
        )
    }
}

@Composable
fun NotificationItem(
    title: String,
    subtitle: String,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(NatnaelAccentGold)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(text = time, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
