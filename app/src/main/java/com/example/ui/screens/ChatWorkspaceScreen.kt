package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AIModel
import com.example.data.model.AttachmentItem
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.CodeSnippet
import com.example.data.model.MessageSender
import com.example.ui.components.CodeWorkspaceView
import com.example.ui.components.GeneratedFileCard
import com.example.ui.components.NatnaelComposer
import com.example.ui.components.ResearchResultsCard
import com.example.ui.components.ResponseActionToolbar
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentViolet
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun ChatWorkspaceScreen(
    chatTitle: String,
    messages: List<ChatMessage>,
    selectedModel: AIModel,
    onSelectModel: (AIModel) -> Unit,
    composerText: String,
    onComposerTextChange: (String) -> Unit,
    isWebResearchMode: Boolean,
    onToggleWebResearch: () -> Unit,
    attachments: List<AttachmentItem>,
    onRemoveAttachment: (String) -> Unit,
    isGenerating: Boolean,
    isListeningVoice: Boolean,
    onToggleVoice: () -> Unit,
    onSendMessage: () -> Unit,
    onStopGenerating: () -> Unit,
    onOpenImageGenerator: () -> Unit,
    onUploadSampleFile: () -> Unit,
    onUploadSampleImage: () -> Unit,
    onClearChat: () -> Unit,
    onDeleteChat: () -> Unit,
    onStartNewChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var isHeaderMenuOpen by remember { mutableStateOf(false) }

    // Scroll to bottom when messages update
    LaunchedEffect(messages.size, messages.lastOrNull()?.text?.length) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // CONVERSATION SUB-HEADER
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chatTitle.ifBlank { "New Conversation" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NatnaelAccentEmerald)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${selectedModel.displayName} • Active",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // New Chat shortcut
                IconButton(onClick = onStartNewChat) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Chat",
                        tint = NatnaelPrimaryIndigo
                    )
                }

                // Menu Options
                Box {
                    IconButton(onClick = { isHeaderMenuOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Chat Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = isHeaderMenuOpen,
                        onDismissRequest = { isHeaderMenuOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Clear Chat") },
                            onClick = {
                                isHeaderMenuOpen = false
                                onClearChat()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Conversation", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                isHeaderMenuOpen = false
                                onDeleteChat()
                            }
                        )
                    }
                }
            }
        }

        // MESSAGES STREAM
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NatnaelPrimaryIndigo.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = NatnaelPrimaryIndigo,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "How can Natnael AI help you today?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ask a question, generate interactive web code with live preview, research web sources, or upload a document.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        ChatMessageItem(message = msg)
                    }

                    // Typing / Generating indicator
                    if (isGenerating && messages.lastOrNull()?.sender == MessageSender.USER) {
                        item {
                            AIGeneratingIndicator()
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }

        // ADVANCED COMPOSER AT BOTTOM
        NatnaelComposer(
            text = composerText,
            onTextChange = onComposerTextChange,
            selectedModel = selectedModel,
            onSelectModel = onSelectModel,
            isWebResearchMode = isWebResearchMode,
            onToggleWebResearch = onToggleWebResearch,
            attachments = attachments,
            onRemoveAttachment = onRemoveAttachment,
            isGenerating = isGenerating,
            isListeningVoice = isListeningVoice,
            onToggleVoice = onToggleVoice,
            onSendMessage = onSendMessage,
            onStopGenerating = onStopGenerating,
            onOpenImageGenerator = onOpenImageGenerator,
            onUploadSampleFile = onUploadSampleFile,
            onUploadSampleImage = onUploadSampleImage
        )
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    if (message.sender == MessageSender.USER) {
        // USER MESSAGE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp))
                    .testTag("user_message_${message.id}"),
                color = NatnaelPrimaryIndigo,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Attachment chips if any
                    if (message.attachments.isNotEmpty()) {
                        message.attachments.forEach { attach ->
                            Text(
                                text = "📎 ${attach.name}",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    } else {
        // AI RESPONSE MESSAGE
        var isThinkingExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .testTag("ai_message_${message.id}"),
            horizontalArrangement = Arrangement.Start
        ) {
            // Natnael AI Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(NatnaelPrimaryIndigo),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_natnael_ai_logo),
                    contentDescription = "Natnael AI",
                    modifier = Modifier.size(26.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Header (Model Name & Badge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Natnael AI",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NatnaelPrimaryIndigo.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = message.model.displayName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NatnaelPrimaryIndigo
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // THINKING PROCESS ACCORDION (For ናትናኤል THINKING)
                if (!message.thinkingProcess.isNullOrBlank()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, NatnaelAccentViolet.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .clickable { isThinkingExpanded = !isThinkingExpanded },
                        color = NatnaelAccentViolet.copy(alpha = 0.06f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = NatnaelAccentViolet,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reasoning Process",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NatnaelAccentViolet
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = if (isThinkingExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = NatnaelAccentViolet,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            AnimatedVisibility(
                                visible = isThinkingExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(modifier = Modifier.padding(top = 8.dp)) {
                                    Text(
                                        text = message.thinkingProcess,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // AI Response Body Text
                if (message.text.isNotEmpty()) {
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }

                // Code Workspace (if code snippet present)
                if (message.codeSnippets.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    message.codeSnippets.forEach { snippet ->
                        CodeWorkspaceView(codeSnippet = snippet)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Web Research results card (if web research present)
                if (message.researchResult != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ResearchResultsCard(result = message.researchResult)
                }

                // Downloadable Generated File card (if generated file present)
                if (message.generatedFile != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    GeneratedFileCard(fileItem = message.generatedFile)
                }

                // Response Action Toolbar
                if (!message.isGenerating) {
                    ResponseActionToolbar(
                        responseText = message.text,
                        hasCode = message.codeSnippets.isNotEmpty()
                    )
                }
            }
        }
    }
}

@Composable
fun AIGeneratingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(NatnaelPrimaryIndigo.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = NatnaelPrimaryIndigo,
                strokeWidth = 2.dp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Natnael AI is synthesizing response...",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
