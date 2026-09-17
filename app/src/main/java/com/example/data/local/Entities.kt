package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: Long,
    val modelId: String,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val folder: String? = null,
    val messageCount: Int = 0
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val sender: String,
    val text: String,
    val timestamp: Long,
    val modelId: String,
    val thinkingProcess: String? = null,
    val codeSnippetJson: String? = null,
    val researchResultJson: String? = null,
    val attachmentsJson: String? = null,
    val generatedFileJson: String? = null,
    val generatedImagesJson: String? = null
)

@Entity(tableName = "saved_images")
data class ImageEntity(
    @PrimaryKey val id: String,
    val prompt: String,
    val imageUrl: String,
    val style: String,
    val aspectRatio: String,
    val createdAt: Long,
    val isFavorite: Boolean = false
)

@Entity(tableName = "saved_files")
data class FileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val format: String,
    val size: String,
    val previewContent: String,
    val createdAt: Long
)
