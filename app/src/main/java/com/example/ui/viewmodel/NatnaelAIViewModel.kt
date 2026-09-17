package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.NatnaelAIService
import com.example.data.local.AppDatabase
import com.example.data.local.NatnaelAIRepository
import com.example.data.model.AIModel
import com.example.data.model.AttachmentItem
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.CodeSnippet
import com.example.data.model.GeneratedFileItem
import com.example.data.model.GeneratedImageItem
import com.example.data.model.MessageSender
import com.example.data.model.NavPage
import com.example.data.model.UserProfile
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class NatnaelAIViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NatnaelAIRepository
    private val aiService = NatnaelAIService()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = NatnaelAIRepository(db)
        seedInitialDataIfEmpty()
    }

    // UI Navigation & Shell State
    private val _currentNavPage = MutableStateFlow(NavPage.HOME)
    val currentNavPage: StateFlow<NavPage> = _currentNavPage.asStateFlow()

    private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _isSidebarExpanded = MutableStateFlow(true)
    val isSidebarExpanded: StateFlow<Boolean> = _isSidebarExpanded.asStateFlow()

    private val _isProfileMenuOpen = MutableStateFlow(false)
    val isProfileMenuOpen: StateFlow<Boolean> = _isProfileMenuOpen.asStateFlow()

    private val _isGlobalSearchOpen = MutableStateFlow(false)
    val isGlobalSearchOpen: StateFlow<Boolean> = _isGlobalSearchOpen.asStateFlow()

    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    // Model & Composer State
    private val _selectedModel = MutableStateFlow(AIModel.LITE)
    val selectedModel: StateFlow<AIModel> = _selectedModel.asStateFlow()

    private val _composerText = MutableStateFlow("")
    val composerText: StateFlow<String> = _composerText.asStateFlow()

    private val _isWebResearchMode = MutableStateFlow(false)
    val isWebResearchMode: StateFlow<Boolean> = _isWebResearchMode.asStateFlow()

    private val _attachments = MutableStateFlow<List<AttachmentItem>>(emptyList())
    val attachments: StateFlow<List<AttachmentItem>> = _attachments.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _isListeningVoice = MutableStateFlow(false)
    val isListeningVoice: StateFlow<Boolean> = _isListeningVoice.asStateFlow()

    // Active Chat & Database flows
    private val _activeChatId = MutableStateFlow<String>("")
    val activeChatId: StateFlow<String> = _activeChatId.asStateFlow()

    val allChats: StateFlow<List<ChatConversation>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allImages: StateFlow<List<GeneratedImageItem>> = repository.allImages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFiles: StateFlow<List<GeneratedFileItem>> = repository.allFiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val currentMessages: StateFlow<List<ChatMessage>> = _currentMessages.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Modals / Fullscreen overlays
    private val _selectedCodeSnippetForPreview = MutableStateFlow<CodeSnippet?>(null)
    val selectedCodeSnippetForPreview: StateFlow<CodeSnippet?> = _selectedCodeSnippetForPreview.asStateFlow()

    private val _selectedImageForViewer = MutableStateFlow<GeneratedImageItem?>(null)
    val selectedImageForViewer: StateFlow<GeneratedImageItem?> = _selectedImageForViewer.asStateFlow()

    private var activeStreamJob: Job? = null

    fun selectNavPage(page: NavPage) {
        _currentNavPage.value = page
        if (page == NavPage.NEW_CHAT) {
            startNewChat()
        }
    }

    fun toggleSidebar() {
        _isSidebarExpanded.value = !_isSidebarExpanded.value
    }

    fun setSidebarExpanded(expanded: Boolean) {
        _isSidebarExpanded.value = expanded
    }

    fun toggleProfileMenu() {
        _isProfileMenuOpen.value = !_isProfileMenuOpen.value
    }

    fun dismissProfileMenu() {
        _isProfileMenuOpen.value = false
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun toggleTheme() {
        _themeMode.value = when (_themeMode.value) {
            AppThemeMode.DARK -> AppThemeMode.LIGHT
            AppThemeMode.LIGHT -> AppThemeMode.DARK
            AppThemeMode.SYSTEM -> AppThemeMode.DARK
        }
    }

    fun setGlobalSearchOpen(open: Boolean) {
        _isGlobalSearchOpen.value = open
    }

    fun setNotificationsOpen(open: Boolean) {
        _isNotificationsOpen.value = open
    }

    fun setSelectedModel(model: AIModel) {
        _selectedModel.value = model
    }

    fun setComposerText(text: String) {
        _composerText.value = text
    }

    fun toggleWebResearchMode() {
        _isWebResearchMode.value = !_isWebResearchMode.value
    }

    fun addAttachment(item: AttachmentItem) {
        _attachments.value = _attachments.value + item
    }

    fun removeAttachment(id: String) {
        _attachments.value = _attachments.value.filter { it.id != id }
    }

    fun toggleVoiceInput() {
        _isListeningVoice.value = !_isListeningVoice.value
        if (_isListeningVoice.value) {
            _composerText.value = "Create a modern responsive dashboard for an Ethiopian fintech application with live preview."
        }
    }

    fun selectChat(chatId: String) {
        _activeChatId.value = chatId
        _currentNavPage.value = NavPage.CHAT_HISTORY
        viewModelScope.launch {
            repository.getMessagesForChat(chatId).collect { msgs ->
                _currentMessages.value = msgs
            }
        }
    }

    fun startNewChat() {
        val newId = UUID.randomUUID().toString()
        _activeChatId.value = newId
        _currentMessages.value = emptyList()
        _currentNavPage.value = NavPage.CHAT_HISTORY
        val newChat = ChatConversation(
            id = newId,
            title = "New Chat",
            lastMessage = "Ask Natnael AI anything...",
            timestamp = System.currentTimeMillis(),
            model = _selectedModel.value
        )
        viewModelScope.launch {
            repository.insertChat(newChat)
            repository.getMessagesForChat(newId).collect { msgs ->
                _currentMessages.value = msgs
            }
        }
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            repository.deleteChat(chatId)
            if (_activeChatId.value == chatId) {
                _activeChatId.value = ""
                _currentMessages.value = emptyList()
                _currentNavPage.value = NavPage.HOME
            }
        }
    }

    fun togglePinChat(chat: ChatConversation) {
        viewModelScope.launch {
            repository.updateChat(chat.copy(isPinned = !chat.isPinned))
        }
    }

    fun toggleFavoriteChat(chat: ChatConversation) {
        viewModelScope.launch {
            repository.updateChat(chat.copy(isFavorite = !chat.isFavorite))
        }
    }

    fun renameChat(chat: ChatConversation, newTitle: String) {
        viewModelScope.launch {
            repository.updateChat(chat.copy(title = newTitle))
        }
    }

    fun openCodePreview(codeSnippet: CodeSnippet) {
        _selectedCodeSnippetForPreview.value = codeSnippet
    }

    fun closeCodePreview() {
        _selectedCodeSnippetForPreview.value = null
    }

    fun openImageViewer(image: GeneratedImageItem) {
        _selectedImageForViewer.value = image
    }

    fun closeImageViewer() {
        _selectedImageForViewer.value = null
    }

    fun sendMessage(explicitPrompt: String? = null) {
        val prompt = (explicitPrompt ?: _composerText.value).trim()
        if (prompt.isBlank() && _attachments.value.isEmpty()) return

        var targetChatId = _activeChatId.value
        if (targetChatId.isBlank()) {
            targetChatId = UUID.randomUUID().toString()
            _activeChatId.value = targetChatId
            val title = if (prompt.length > 30) prompt.take(30) + "..." else prompt.ifBlank { "Analysis Task" }
            val newChat = ChatConversation(
                id = targetChatId,
                title = title,
                lastMessage = prompt.take(40),
                timestamp = System.currentTimeMillis(),
                model = _selectedModel.value
            )
            viewModelScope.launch {
                repository.insertChat(newChat)
            }
        }

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            chatId = targetChatId,
            sender = MessageSender.USER,
            text = prompt,
            timestamp = System.currentTimeMillis(),
            model = _selectedModel.value,
            attachments = _attachments.value
        )

        val aiMessageId = UUID.randomUUID().toString()
        val initialAiMessage = ChatMessage(
            id = aiMessageId,
            chatId = targetChatId,
            sender = MessageSender.AI,
            text = "",
            timestamp = System.currentTimeMillis() + 1,
            model = _selectedModel.value,
            isGenerating = true
        )

        // Clear composer
        _composerText.value = ""
        val attached = _attachments.value
        _attachments.value = emptyList()
        val isWebResearch = _isWebResearchMode.value

        viewModelScope.launch {
            repository.insertMessage(userMessage)
            _currentMessages.value = _currentMessages.value + userMessage + initialAiMessage
            _isGenerating.value = true

            activeStreamJob?.cancel()
            activeStreamJob = launch {
                var currentAiMessage = initialAiMessage
                aiService.streamAIResponse(
                    prompt = prompt,
                    model = _selectedModel.value,
                    chatId = targetChatId,
                    isWebResearch = isWebResearch,
                    attachments = attached
                ).collect { chunk ->
                    currentAiMessage = currentAiMessage.copy(
                        text = chunk.textDelta.ifEmpty { currentAiMessage.text },
                        thinkingProcess = chunk.thinkingDelta ?: currentAiMessage.thinkingProcess,
                        codeSnippets = if (chunk.codeSnippet != null) listOf(chunk.codeSnippet) else currentAiMessage.codeSnippets,
                        researchResult = chunk.researchResult ?: currentAiMessage.researchResult,
                        generatedFile = chunk.generatedFile ?: currentAiMessage.generatedFile,
                        isGenerating = !chunk.isComplete
                    )

                    // Update in-memory list for instant animation
                    _currentMessages.value = _currentMessages.value.map {
                        if (it.id == aiMessageId) currentAiMessage else it
                    }

                    if (chunk.isComplete) {
                        repository.insertMessage(currentAiMessage)
                        if (chunk.generatedFile != null) {
                            repository.insertFile(chunk.generatedFile)
                        }
                    }
                }
                _isGenerating.value = false
            }
        }
    }

    fun stopGenerating() {
        activeStreamJob?.cancel()
        _isGenerating.value = false
        _currentMessages.value = _currentMessages.value.map {
            if (it.isGenerating) it.copy(isGenerating = false) else it
        }
    }

    fun generateImage(prompt: String, style: String = "Futuristic", aspectRatio: String = "1:1") {
        viewModelScope.launch {
            val img = GeneratedImageItem(
                id = UUID.randomUUID().toString(),
                prompt = prompt,
                imageUrl = "https://picsum.photos/seed/${prompt.hashCode()}/800/800",
                style = style,
                aspectRatio = aspectRatio,
                createdAt = System.currentTimeMillis(),
                isFavorite = false
            )
            repository.insertImage(img)
            _userProfile.value = _userProfile.value.copy(
                imagesCreated = _userProfile.value.imagesCreated + 1
            )
        }
    }

    fun toggleImageFavorite(image: GeneratedImageItem) {
        viewModelScope.launch {
            repository.toggleImageFavorite(image.id, !image.isFavorite)
        }
    }

    fun createGeneratedFile(name: String, format: String, content: String) {
        viewModelScope.launch {
            val file = GeneratedFileItem(
                id = UUID.randomUUID().toString(),
                name = name,
                format = format,
                size = "1.8 MB",
                previewContent = content,
                createdAt = System.currentTimeMillis()
            )
            repository.insertFile(file)
            _userProfile.value = _userProfile.value.copy(
                filesGenerated = _userProfile.value.filesGenerated + 1
            )
        }
    }

    fun deleteFile(fileId: String) {
        viewModelScope.launch {
            repository.deleteFile(fileId)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _activeChatId.value = ""
            _currentMessages.value = emptyList()
        }
    }

    private fun seedInitialDataIfEmpty() {
        viewModelScope.launch {
            // Seed a welcome chat with rich features demonstrating Natnael AI
            val welcomeChatId = "welcome_chat_01"
            val welcomeChat = ChatConversation(
                id = welcomeChatId,
                title = "Natnael AI Platform Overview",
                lastMessage = "Welcome to Natnael AI - Intelligent Workspace",
                timestamp = System.currentTimeMillis(),
                model = AIModel.PRO,
                isPinned = true,
                folder = "Getting Started",
                messageCount = 2
            )
            repository.insertChat(welcomeChat)

            val welcomeHtml = """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Natnael AI Welcome Portal</title>
  <style>
    body { background: #0B0F19; color: #F8FAFC; font-family: sans-serif; display: flex; align-items: center; justify-content: center; height: 100vh; margin: 0; }
    .box { background: #111827; border: 1px solid #38BDF8; border-radius: 16px; padding: 24px; text-align: center; max-width: 380px; }
    h2 { color: #38BDF8; font-size: 20px; margin-bottom: 8px; }
    p { color: #94A3B8; font-size: 13px; line-height: 1.5; }
    .tag { background: #312E81; color: #818CF8; padding: 4px 10px; border-radius: 6px; font-size: 11px; margin-top: 12px; display: inline-block; }
  </style>
</head>
<body>
  <div class="box">
    <h2>Natnael AI Workspace</h2>
    <p>Live Code Sandbox Active. Full preview support with real-time DOM updates & interactive components.</p>
    <div class="tag">Production Ready • Verified</div>
  </div>
</body>
</html>
            """.trimIndent()

            val msg1 = ChatMessage(
                id = "m1",
                chatId = welcomeChatId,
                sender = MessageSender.USER,
                text = "Show me the core capabilities of Natnael AI including code preview and deep thinking.",
                timestamp = System.currentTimeMillis() - 60000,
                model = AIModel.PRO
            )

            val msg2 = ChatMessage(
                id = "m2",
                chatId = welcomeChatId,
                sender = MessageSender.AI,
                text = "Welcome to **Natnael AI**! I am an all-in-one AI platform engineered for seamless conversations, real-time code execution with live preview, deep reasoning with **ናትናኤል THINKING**, web research synthesis, document parsing, and file generation.\n\n### Highlights:\n- **Multi-Model Support:** Switch effortlessly between **ናትናኤል LITE**, **ናትናኤል THINKING**, and **ናትናኤል PRO**.\n- **Live Code Sandbox:** Preview websites and interactive web applications in the split-screen tab.\n- **Full Document & Image Generation:** Download generated PDFs, CSVs, or browse AI-generated imagery in the dedicated galleries.",
                timestamp = System.currentTimeMillis() - 30000,
                model = AIModel.PRO,
                thinkingProcess = "• Context initialized with verified SaaS architecture\n• Highlighting Ethiopian tech brand synergy & multi-modal capabilities\n• Preparing interactive HTML/CSS live sandbox demo.",
                codeSnippets = listOf(
                    CodeSnippet(
                        id = "welcome_code",
                        title = "WelcomePortal.html",
                        language = "html",
                        code = welcomeHtml,
                        previewHtml = welcomeHtml
                    )
                ),
                generatedFile = GeneratedFileItem(
                    id = "welcome_file",
                    name = "Natnael_AI_Platform_Specs.pdf",
                    format = "PDF",
                    size = "1.2 MB",
                    previewContent = "NATNAEL AI PLATFORM SPECIFICATION\n\nVersion: 2.0 Production\nArchitect: Natnael Amare\nCapabilities: Multi-Model Reasoning, Live Sandbox, Document Synthesis"
                )
            )

            repository.insertMessage(msg1)
            repository.insertMessage(msg2)

            // Seed sample files
            repository.insertFile(
                GeneratedFileItem(
                    id = "f1",
                    name = "Fintech_Market_Analysis_2026.pdf",
                    format = "PDF",
                    size = "3.2 MB",
                    previewContent = "ETHIOPIAN FINTECH EXPANSION ANALYSIS\nTelebirr, CBE Birr, and digital banking integration dynamics."
                )
            )
            repository.insertFile(
                GeneratedFileItem(
                    id = "f2",
                    name = "Quarterly_AI_Tokens_Usage.xlsx",
                    format = "XLSX",
                    size = "450 KB",
                    previewContent = "Month,Model,Calls,Tokens\nJan,LITE,420,540000\nFeb,THINKING,180,920000\nMar,PRO,310,1400000"
                )
            )

            // Seed sample images
            repository.insertImage(
                GeneratedImageItem(
                    id = "img1",
                    prompt = "Futuristic Addis Ababa skyline with glowing quantum data streams and solar architecture at dusk",
                    imageUrl = "https://picsum.photos/seed/addis_future/800/800",
                    style = "Cyberpunk Neo-Ethiopia",
                    aspectRatio = "1:1",
                    isFavorite = true
                )
            )
            repository.insertImage(
                GeneratedImageItem(
                    id = "img2",
                    prompt = "Minimalist holographic neural network core with Ethiopian geometric motifs in royal indigo and gold",
                    imageUrl = "https://picsum.photos/seed/neural_network/800/800",
                    style = "Minimalist 3D Vector",
                    aspectRatio = "16:9",
                    isFavorite = false
                )
            )

            // Set welcome chat active by default
            _activeChatId.value = welcomeChatId
            _currentMessages.value = listOf(msg1, msg2)
        }
    }
}
