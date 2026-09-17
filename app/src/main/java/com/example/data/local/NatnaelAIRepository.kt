package com.example.data.local

import com.example.data.model.AIModel
import com.example.data.model.AttachmentItem
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.CodeSnippet
import com.example.data.model.GeneratedFileItem
import com.example.data.model.GeneratedImageItem
import com.example.data.model.MessageSender
import com.example.data.model.ResearchResult
import com.example.data.model.ResearchSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

class NatnaelAIRepository(
    private val database: AppDatabase
) {
    val allChats: Flow<List<ChatConversation>> = database.chatDao().getAllChats().map { entities ->
        entities.map { it.toDomain() }
    }

    val allImages: Flow<List<GeneratedImageItem>> = database.imageDao().getAllImages().map { entities ->
        entities.map { it.toDomain() }
    }

    val allFiles: Flow<List<GeneratedFileItem>> = database.fileDao().getAllFiles().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getMessagesForChat(chatId: String): Flow<List<ChatMessage>> =
        database.messageDao().getMessagesForChat(chatId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun insertChat(chat: ChatConversation) {
        database.chatDao().insertChat(chat.toEntity())
    }

    suspend fun insertMessage(msg: ChatMessage) {
        database.messageDao().insertMessage(msg.toEntity())
        // Update parent chat's last message
        val existing = database.chatDao().getChatById(msg.chatId)
        if (existing != null) {
            database.chatDao().updateChat(
                existing.copy(
                    lastMessage = msg.text.take(60),
                    timestamp = msg.timestamp,
                    messageCount = existing.messageCount + 1
                )
            )
        }
    }

    suspend fun updateChat(chat: ChatConversation) {
        database.chatDao().updateChat(chat.toEntity())
    }

    suspend fun deleteChat(chatId: String) {
        database.chatDao().deleteChatById(chatId)
        database.messageDao().deleteMessagesForChat(chatId)
    }

    suspend fun insertImage(image: GeneratedImageItem) {
        database.imageDao().insertImage(
            ImageEntity(
                id = image.id,
                prompt = image.prompt,
                imageUrl = image.imageUrl,
                style = image.style,
                aspectRatio = image.aspectRatio,
                createdAt = image.createdAt,
                isFavorite = image.isFavorite
            )
        )
    }

    suspend fun toggleImageFavorite(id: String, isFav: Boolean) {
        database.imageDao().updateFavorite(id, isFav)
    }

    suspend fun insertFile(file: GeneratedFileItem) {
        database.fileDao().insertFile(
            FileEntity(
                id = file.id,
                name = file.name,
                format = file.format,
                size = file.size,
                previewContent = file.previewContent,
                createdAt = file.createdAt
            )
        )
    }

    suspend fun deleteFile(fileId: String) {
        database.fileDao().deleteFileById(fileId)
    }

    suspend fun clearHistory() {
        database.chatDao().clearAllChats()
        database.messageDao().clearAllMessages()
    }

    // Converters
    private fun ChatEntity.toDomain() = ChatConversation(
        id = id,
        title = title,
        lastMessage = lastMessage,
        timestamp = timestamp,
        model = AIModel.entries.firstOrNull { it.id == modelId } ?: AIModel.LITE,
        isPinned = isPinned,
        isFavorite = isFavorite,
        folder = folder,
        messageCount = messageCount
    )

    private fun ChatConversation.toEntity() = ChatEntity(
        id = id,
        title = title,
        lastMessage = lastMessage,
        timestamp = timestamp,
        modelId = model.id,
        isPinned = isPinned,
        isFavorite = isFavorite,
        folder = folder,
        messageCount = messageCount
    )

    private fun ImageEntity.toDomain() = GeneratedImageItem(
        id = id,
        prompt = prompt,
        imageUrl = imageUrl,
        style = style,
        aspectRatio = aspectRatio,
        createdAt = createdAt,
        isFavorite = isFavorite
    )

    private fun FileEntity.toDomain() = GeneratedFileItem(
        id = id,
        name = name,
        format = format,
        size = size,
        previewContent = previewContent,
        createdAt = createdAt
    )

    private fun MessageEntity.toDomain(): ChatMessage {
        val modelEnum = AIModel.entries.firstOrNull { it.id == modelId } ?: AIModel.LITE
        val senderEnum = if (sender == "USER") MessageSender.USER else MessageSender.AI

        // Parse code snippets if present
        val codeList = mutableListOf<CodeSnippet>()
        if (!codeSnippetJson.isNullOrBlank()) {
            try {
                val array = JSONArray(codeSnippetJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    codeList.add(
                        CodeSnippet(
                            id = obj.optString("id", "$i"),
                            title = obj.optString("title", "Code"),
                            language = obj.optString("language", "text"),
                            code = obj.optString("code", ""),
                            previewHtml = obj.optString("previewHtml", null)
                        )
                    )
                }
            } catch (_: Exception) {}
        }

        // Parse research result
        var researchRes: ResearchResult? = null
        if (!researchResultJson.isNullOrBlank()) {
            try {
                val obj = JSONObject(researchResultJson)
                val findingsArray = obj.optJSONArray("findings")
                val findings = mutableListOf<String>()
                if (findingsArray != null) {
                    for (i in 0 until findingsArray.length()) {
                        findings.add(findingsArray.getString(i))
                    }
                }
                val sourcesArray = obj.optJSONArray("sources")
                val sources = mutableListOf<ResearchSource>()
                if (sourcesArray != null) {
                    for (i in 0 until sourcesArray.length()) {
                        val sObj = sourcesArray.getJSONObject(i)
                        sources.add(
                            ResearchSource(
                                title = sObj.optString("title"),
                                domain = sObj.optString("domain"),
                                snippet = sObj.optString("snippet"),
                                url = sObj.optString("url")
                            )
                        )
                    }
                }
                researchRes = ResearchResult(
                    query = obj.optString("query"),
                    keyFindings = findings,
                    summary = obj.optString("summary"),
                    sources = sources
                )
            } catch (_: Exception) {}
        }

        // Parse generated file
        var genFile: GeneratedFileItem? = null
        if (!generatedFileJson.isNullOrBlank()) {
            try {
                val obj = JSONObject(generatedFileJson)
                genFile = GeneratedFileItem(
                    id = obj.optString("id"),
                    name = obj.optString("name"),
                    format = obj.optString("format"),
                    size = obj.optString("size"),
                    previewContent = obj.optString("previewContent"),
                    createdAt = obj.optLong("createdAt", timestamp)
                )
            } catch (_: Exception) {}
        }

        // Parse attachments
        val attachList = mutableListOf<AttachmentItem>()
        if (!attachmentsJson.isNullOrBlank()) {
            try {
                val array = JSONArray(attachmentsJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    attachList.add(
                        AttachmentItem(
                            id = obj.optString("id", "$i"),
                            name = obj.optString("name"),
                            size = obj.optString("size"),
                            type = obj.optString("type")
                        )
                    )
                }
            } catch (_: Exception) {}
        }

        // Parse generated images
        val imgList = mutableListOf<String>()
        if (!generatedImagesJson.isNullOrBlank()) {
            try {
                val array = JSONArray(generatedImagesJson)
                for (i in 0 until array.length()) {
                    imgList.add(array.getString(i))
                }
            } catch (_: Exception) {}
        }

        return ChatMessage(
            id = id,
            chatId = chatId,
            sender = senderEnum,
            text = text,
            timestamp = timestamp,
            model = modelEnum,
            thinkingProcess = thinkingProcess,
            codeSnippets = codeList,
            researchResult = researchRes,
            attachments = attachList,
            generatedFile = genFile,
            generatedImages = imgList
        )
    }

    private fun ChatMessage.toEntity(): MessageEntity {
        var codeJson: String? = null
        if (codeSnippets.isNotEmpty()) {
            val array = JSONArray()
            for (code in codeSnippets) {
                val obj = JSONObject()
                obj.put("id", code.id)
                obj.put("title", code.title)
                obj.put("language", code.language)
                obj.put("code", code.code)
                obj.put("previewHtml", code.previewHtml)
                array.put(obj)
            }
            codeJson = array.toString()
        }

        var researchJson: String? = null
        if (researchResult != null) {
            val obj = JSONObject()
            obj.put("query", researchResult.query)
            obj.put("summary", researchResult.summary)
            val findingsArray = JSONArray()
            researchResult.keyFindings.forEach { findingsArray.put(it) }
            obj.put("findings", findingsArray)
            val sourcesArray = JSONArray()
            researchResult.sources.forEach {
                val sObj = JSONObject()
                sObj.put("title", it.title)
                sObj.put("domain", it.domain)
                sObj.put("snippet", it.snippet)
                sObj.put("url", it.url)
                sourcesArray.put(sObj)
            }
            obj.put("sources", sourcesArray)
            researchJson = obj.toString()
        }

        var fileJson: String? = null
        if (generatedFile != null) {
            val obj = JSONObject()
            obj.put("id", generatedFile.id)
            obj.put("name", generatedFile.name)
            obj.put("format", generatedFile.format)
            obj.put("size", generatedFile.size)
            obj.put("previewContent", generatedFile.previewContent)
            obj.put("createdAt", generatedFile.createdAt)
            fileJson = obj.toString()
        }

        var attachJson: String? = null
        if (attachments.isNotEmpty()) {
            val array = JSONArray()
            attachments.forEach {
                val obj = JSONObject()
                obj.put("id", it.id)
                obj.put("name", it.name)
                obj.put("size", it.size)
                obj.put("type", it.type)
                array.put(obj)
            }
            attachJson = array.toString()
        }

        var imagesJson: String? = null
        if (generatedImages.isNotEmpty()) {
            val array = JSONArray()
            generatedImages.forEach { array.put(it) }
            imagesJson = array.toString()
        }

        return MessageEntity(
            id = id,
            chatId = chatId,
            sender = if (sender == MessageSender.USER) "USER" else "AI",
            text = text,
            timestamp = timestamp,
            modelId = model.id,
            thinkingProcess = thinkingProcess,
            codeSnippetJson = codeJson,
            researchResultJson = researchJson,
            attachmentsJson = attachJson,
            generatedFileJson = fileJson,
            generatedImagesJson = imagesJson
        )
    }
}
