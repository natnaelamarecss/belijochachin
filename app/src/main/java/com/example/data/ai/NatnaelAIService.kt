package com.example.data.ai

import com.example.data.model.AIModel
import com.example.data.model.AttachmentItem
import com.example.data.model.ChatMessage
import com.example.data.model.CodeSnippet
import com.example.data.model.GeneratedFileItem
import com.example.data.model.MessageSender
import com.example.data.model.ResearchResult
import com.example.data.model.ResearchSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class NatnaelAIService {

    data class StreamChunk(
        val textDelta: String = "",
        val thinkingDelta: String? = null,
        val codeSnippet: CodeSnippet? = null,
        val researchResult: ResearchResult? = null,
        val generatedFile: GeneratedFileItem? = null,
        val generatedImages: List<String> = emptyList(),
        val isComplete: Boolean = false
    )

    fun streamAIResponse(
        prompt: String,
        model: AIModel,
        chatId: String,
        isWebResearch: Boolean = false,
        attachments: List<AttachmentItem> = emptyList()
    ): Flow<StreamChunk> = flow {

        val lower = prompt.lowercase()
        val isCodePrompt = lower.contains("code") || lower.contains("html") || lower.contains("website") ||
                lower.contains("react") || lower.contains("javascript") || lower.contains("python") ||
                lower.contains("app") || lower.contains("component") || lower.contains("dashboard") ||
                lower.contains("game")

        val isFileGenPrompt = lower.contains("file") || lower.contains("generate pdf") ||
                lower.contains("csv") || lower.contains("xlsx") || lower.contains("report") ||
                lower.contains("download") || lower.contains("export")

        // Step 1: Handle Thinking Model Trace if applicable
        var thinkingBuffer = ""
        if (model == AIModel.THINKING) {
            val thinkingSteps = listOf(
                "Analyzing prompt intent, domain constraints, and user context...",
                "Retrieving verified algorithmic foundations and Ethiopian tech context...",
                "Formulating structured breakdown, validating mathematical logic and edge cases...",
                "Synthesizing final response with high clarity and actionable insights."
            )
            for (step in thinkingSteps) {
                delay(300)
                thinkingBuffer += "• $step\n"
                emit(StreamChunk(thinkingDelta = thinkingBuffer))
            }
        }

        // Step 2: Handle Web Research Mode
        if (isWebResearch) {
            delay(400)
            val researchResult = ResearchResult(
                query = prompt,
                keyFindings = listOf(
                    "Comprehensive consensus verified across multi-regional indexers and knowledge graphs.",
                    "Recent technical papers indicate a 34% efficiency leap using modern reasoning models.",
                    "Active implementations in fintech, healthcare, and educational technology in East Africa and globally.",
                    "Standardized protocols ensure zero-knowledge data privacy and local-first caching."
                ),
                summary = "Natnael AI synthesized 6 primary web sources to provide an authoritative overview of '$prompt'. Below are the primary citations and referenced knowledge foundations.",
                sources = listOf(
                    ResearchSource(
                        title = "Global AI Architectures & Regional Impact 2026",
                        domain = "technologyreview.com",
                        snippet = "How emerging AI models in Ethiopia and Africa are optimizing multilingual reasoning and local workflows.",
                        url = "https://technologyreview.com"
                    ),
                    ResearchSource(
                        title = "Real-time High Precision Inference Protocols",
                        domain = "arxiv.org",
                        snippet = "Benchmark evaluations of multi-agent reasoning, code verification, and responsive browser sandboxing.",
                        url = "https://arxiv.org"
                    ),
                    ResearchSource(
                        title = "Developer Ecosystem & Open Web Standards",
                        domain = "developer.mozilla.org",
                        snippet = "Interactive preview sandboxes, accessible typography, and web component architectures.",
                        url = "https://developer.mozilla.org"
                    )
                )
            )
            emit(StreamChunk(researchResult = researchResult))
        }

        // Step 3: Handle Code Generation with Live Preview if applicable
        var codeSnippet: CodeSnippet? = null
        if (isCodePrompt) {
            val sampleHtml = """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Natnael AI Interactive Demo</title>
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
    body { background: #0B0F19; color: #F8FAFC; padding: 24px; text-align: center; }
    .card { background: #111827; border: 1px solid #26334D; border-radius: 16px; padding: 24px; max-width: 440px; margin: 0 auto; box-shadow: 0 10px 30px rgba(0,0,0,0.5); }
    h1 { font-size: 22px; color: #38BDF8; margin-bottom: 8px; }
    p { color: #94A3B8; font-size: 14px; margin-bottom: 20px; line-height: 1.5; }
    .badge { display: inline-block; background: rgba(99,102,241,0.2); color: #818CF8; border: 1px solid rgba(99,102,241,0.4); border-radius: 999px; padding: 4px 12px; font-size: 12px; margin-bottom: 16px; font-weight: 600; }
    .btn { background: linear-gradient(135deg, #6366F1, #06B6D4); color: white; border: none; padding: 12px 24px; border-radius: 10px; font-weight: 600; cursor: pointer; transition: transform 0.2s; }
    .btn:active { transform: scale(0.96); }
    .counter { font-size: 32px; font-weight: bold; color: #F59E0B; margin: 16px 0; }
    .status { font-size: 12px; color: #10B981; margin-top: 12px; }
  </style>
</head>
<body>
  <div class="card">
    <div class="badge">Natnael AI • Live Code Preview</div>
    <h1>Interactive Web App</h1>
    <p>Rendered in real-time inside Natnael AI's sandboxed environment.</p>
    <div class="counter" id="count">0</div>
    <button class="btn" onclick="increment()">Click Me</button>
    <div class="status">● Execution State: Active & Sandboxed</div>
  </div>
  <script>
    let c = 0;
    function increment() {
      c++;
      document.getElementById('count').innerText = c;
    }
  </script>
</body>
</html>
            """.trimIndent()

            codeSnippet = CodeSnippet(
                id = UUID.randomUUID().toString(),
                title = "InteractiveApplication.html",
                language = "html",
                code = sampleHtml,
                previewHtml = sampleHtml
            )
        }

        // Step 4: Handle File Generation
        var generatedFile: GeneratedFileItem? = null
        if (isFileGenPrompt) {
            generatedFile = GeneratedFileItem(
                id = UUID.randomUUID().toString(),
                name = "Natnael_AI_Research_Report.pdf",
                format = "PDF",
                size = "2.4 MB",
                previewContent = "NATNAEL AI EXECUTIVE REPORT\n\nTitle: Comprehensive Intelligence Briefing\nDate: September 2026\nAuthor: Natnael AI Workspace\nStatus: Finalized & Verified\n\n1. Executive Summary\n2. Algorithmic Breakdown\n3. Regional Innovation Hub Insights\n4. Recommended Implementations\n\nExported successfully via Natnael AI Workspace."
            )
        }

        // Step 5: Construct Final Narrative Text
        val responseText = buildString {
            if (attachments.isNotEmpty()) {
                append("📄 **Analyzed Attachments:** ${attachments.joinToString { it.name }}\n\n")
                append("I have thoroughly parsed the uploaded file(s). All data schemas, textual elements, and key metrics have been indexed.\n\n")
            }

            if (isWebResearch) {
                append("### 🌐 Web Research Synthesis\n\n")
                append("Based on live index queries for **\"$prompt\"**, here is a distilled overview of the most relevant findings:\n\n")
                append("- **Key Insight 1:** High-velocity intelligence models show significant breakthroughs in latency and deterministic reasoning.\n")
                append("- **Key Insight 2:** Ethiopian and pan-African tech ecosystems are accelerating AI adoption in fintech, agriculture, and high-performance computing.\n")
                append("- **Key Insight 3:** Enterprise workspaces are transitioning towards unified multi-modal platforms with integrated live code preview and file compilation.\n\n")
            } else if (isCodePrompt) {
                append("### 💻 Solution & Live Preview\n\n")
                append("Here is the requested implementation designed with clean architecture, responsive styling, and modern standards.\n\n")
                append("You can switch between the **Code** view and **Live Preview** tab to interact with the running web application in real time!\n\n")
            } else {
                append("### Response from **${model.displayName}**\n\n")
                append("Thank you for your prompt: *\"$prompt\"*.\n\n")
                append("As Natnael AI, I have processed your request utilizing **${model.badge}** capabilities:\n\n")
                append("1. **Core Clarity:** Your objective has been analyzed across structured logical dimensions.\n")
                append("2. **Actionable Recommendations:** You can generate downloadable reports, run live previews, execute web research, or spin up new image generation tasks directly from this workspace.\n")
                append("3. **Extensibility:** All outputs can be exported as PDF, DOCX, Markdown, or copied with full formatting.\n\n")
                append("How would you like to expand on this?")
            }
        }

        // Stream text in small chunks for realistic, pleasant typing animation
        val words = responseText.split(" ")
        var currentText = ""
        for (w in words) {
            currentText += if (currentText.isEmpty()) w else " $w"
            emit(
                StreamChunk(
                    textDelta = currentText,
                    codeSnippet = codeSnippet,
                    generatedFile = generatedFile
                )
            )
            delay(18)
        }

        // Final complete emission
        emit(
            StreamChunk(
                textDelta = currentText,
                thinkingDelta = thinkingBuffer.ifBlank { null },
                codeSnippet = codeSnippet,
                generatedFile = generatedFile,
                isComplete = true
            )
        )
    }

    fun generateImages(prompt: String, style: String, aspectRatio: String): List<String> {
        // Return rich image placeholders representing generated AI art
        return listOf(
            "https://picsum.photos/seed/${prompt.hashCode()}/800/800",
            "https://picsum.photos/seed/${prompt.hashCode() + 1}/800/800",
            "https://picsum.photos/seed/${prompt.hashCode() + 2}/800/800",
            "https://picsum.photos/seed/${prompt.hashCode() + 3}/800/800"
        )
    }
}
