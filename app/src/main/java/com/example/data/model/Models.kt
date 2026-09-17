package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector

enum class AIModel(
    val id: String,
    val displayName: String,
    val amharicLabel: String,
    val badge: String,
    val description: String,
    val icon: ImageVector
) {
    LITE(
        id = "natnael_lite",
        displayName = "ናትናኤል LITE",
        amharicLabel = "ናትናኤል LITE",
        badge = "Ultra Fast",
        description = "A fast and lightweight AI model for everyday questions, quick answers, simple writing, and general assistance.",
        icon = Icons.Default.Speed
    ),
    THINKING(
        id = "natnael_thinking",
        displayName = "ናትናኤል THINKING",
        amharicLabel = "ናትናኤል THINKING",
        badge = "Deep Reasoning",
        description = "An advanced reasoning model for complex problems, mathematics, analysis, research, planning, and deep problem solving.",
        icon = Icons.Default.Psychology
    ),
    PRO(
        id = "natnael_pro",
        displayName = "ናትናኤል PRO",
        amharicLabel = "ናትናኤል PRO",
        badge = "Most Powerful",
        description = "A powerful professional model for advanced coding, creative work, professional writing, research, and complex AI tasks.",
        icon = Icons.Default.AutoAwesome
    )
}

enum class NavPage(
    val title: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    NEW_CHAT("New Chat", Icons.Default.AddComment),
    CHAT_HISTORY("Chat History", Icons.Default.History),
    EXPLORE("Explore", Icons.Default.Explore),
    IMAGES("Images", Icons.Default.Image),
    FILES("Files", Icons.Default.Description),
    PROJECTS("Projects", Icons.Default.Folder),
    SETTINGS("Settings", Icons.Default.Settings)
}

enum class MessageSender {
    USER, AI
}

data class CodeSnippet(
    val id: String,
    val title: String,
    val language: String,
    val code: String,
    val previewHtml: String? = null
)

data class ResearchSource(
    val title: String,
    val domain: String,
    val snippet: String,
    val url: String
)

data class ResearchResult(
    val query: String,
    val keyFindings: List<String>,
    val summary: String,
    val sources: List<ResearchSource>
)

data class AttachmentItem(
    val id: String,
    val name: String,
    val size: String,
    val type: String, // PDF, DOCX, CSV, TXT, IMAGE
    val previewUrl: String? = null
)

data class GeneratedFileItem(
    val id: String,
    val name: String,
    val format: String, // PDF, DOCX, XLSX, PPTX, TXT, CSV, MD
    val size: String,
    val previewContent: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class GeneratedImageItem(
    val id: String,
    val prompt: String,
    val imageUrl: String,
    val style: String,
    val aspectRatio: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

data class ChatMessage(
    val id: String,
    val chatId: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val model: AIModel = AIModel.LITE,
    val thinkingProcess: String? = null,
    val isThinkingExpanded: Boolean = false,
    val codeSnippets: List<CodeSnippet> = emptyList(),
    val researchResult: ResearchResult? = null,
    val attachments: List<AttachmentItem> = emptyList(),
    val generatedFile: GeneratedFileItem? = null,
    val generatedImages: List<String> = emptyList(),
    val isGenerating: Boolean = false
)

data class ChatConversation(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: Long,
    val model: AIModel,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val folder: String? = null,
    val messageCount: Int = 0
)

data class UserProfile(
    val name: String = "Natnael Amare",
    val email: String = "natnaelamareyemo@gmail.com",
    val role: String = "AI Developer",
    val plan: String = "Pro Enterprise",
    val isOnline: Boolean = true,
    val totalChats: Int = 128,
    val imagesCreated: Int = 42,
    val filesGenerated: Int = 19,
    val tokensUsed: String = "1.2M"
)
