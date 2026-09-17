package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIModel
import com.example.data.model.ChatConversation
import com.example.data.model.NavPage
import com.example.data.model.UserProfile
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentViolet
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    userProfile: UserProfile,
    recentChats: List<ChatConversation>,
    onSelectChat: (String) -> Unit,
    onStartNewChat: () -> Unit,
    onNavigate: (NavPage) -> Unit,
    onPromptSelected: (String, AIModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // HERO BANNER
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("home_hero_banner"),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    NatnaelPrimaryIndigo.copy(alpha = 0.3f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    NatnaelPrimaryIndigo.copy(alpha = 0.15f),
                                    NatnaelPrimaryCyan.copy(alpha = 0.08f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Welcome back, ${userProfile.name}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NatnaelAccentGold.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = userProfile.plan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NatnaelAccentGold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Natnael AI is ready. Converse with multi-tiered reasoning models, execute live interactive web code, research the live web, or generate high-fidelity media.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onStartNewChat,
                                colors = ButtonDefaults.buttonColors(containerColor = NatnaelPrimaryIndigo),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("hero_new_chat_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("New Chat", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onNavigate(NavPage.EXPLORE) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Explore Prompts", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }

        // STATS STRIP
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Conversations",
                    value = "${recentChats.size}",
                    icon = Icons.AutoMirrored.Filled.Chat,
                    accentColor = NatnaelPrimaryIndigo,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Images",
                    value = "${userProfile.imagesCreated}",
                    icon = Icons.Default.Image,
                    accentColor = NatnaelAccentViolet,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Documents",
                    value = "${userProfile.filesGenerated}",
                    icon = Icons.Default.Description,
                    accentColor = NatnaelAccentEmerald,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // QUICK ACTION PROMPT CARDS
        item {
            Text(
                text = "Quick AI Workflows",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WorkflowCard(
                    title = "Live Code Sandbox",
                    subtitle = "Generate responsive interactive web app with live preview",
                    icon = Icons.Default.Code,
                    color = NatnaelPrimaryCyan,
                    onClick = {
                        onPromptSelected(
                            "Generate an interactive HTML/JS financial dashboard component with live preview.",
                            AIModel.PRO
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.48f)
                )
                WorkflowCard(
                    title = "Deep Reasoning",
                    subtitle = "Solve complex algorithms & architecture problems",
                    icon = Icons.Default.Psychology,
                    color = NatnaelAccentViolet,
                    onClick = {
                        onPromptSelected(
                            "Provide a deep architectural breakdown and mathematical verification of distributed AI systems in Africa.",
                            AIModel.THINKING
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.48f)
                )
                WorkflowCard(
                    title = "Web Research",
                    subtitle = "Synthesize live web articles with verified citations",
                    icon = Icons.Default.Public,
                    color = NatnaelAccentGold,
                    onClick = {
                        onPromptSelected(
                            "Research latest developments in Ethiopian digital banking and AI integrations.",
                            AIModel.LITE
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.48f)
                )
                WorkflowCard(
                    title = "Document Creation",
                    subtitle = "Synthesize executive reports ready for download",
                    icon = Icons.Default.Description,
                    color = NatnaelAccentEmerald,
                    onClick = {
                        onPromptSelected(
                            "Generate a comprehensive PDF research report on enterprise cloud architecture.",
                            AIModel.PRO
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.48f)
                )
            }
        }

        // RECENT CONVERSATIONS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Conversations",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NatnaelPrimaryIndigo,
                    modifier = Modifier.clickable { onNavigate(NavPage.CHAT_HISTORY) }
                )
            }
        }

        items(recentChats.take(4)) { chat ->
            RecentChatCard(
                chat = chat,
                onClick = { onSelectChat(chat.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun WorkflowCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp, maxLines = 2)
        }
    }
}

@Composable
fun RecentChatCard(
    chat: ChatConversation,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("recent_chat_${chat.id}"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(NatnaelPrimaryIndigo.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = NatnaelPrimaryIndigo,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = chat.lastMessage,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = chat.model.displayName,
                    fontSize = 10.sp,
                    color = NatnaelPrimaryIndigo,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
