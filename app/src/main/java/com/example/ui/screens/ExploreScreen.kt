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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIModel
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentRose
import com.example.ui.theme.NatnaelAccentViolet
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

data class PromptTemplate(
    val category: String,
    val title: String,
    val prompt: String,
    val recommendedModel: AIModel,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    onSelectPrompt: (prompt: String, model: AIModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val templates = remember {
        listOf(
            PromptTemplate(
                category = "Coding",
                title = "Interactive Web Dashboard",
                prompt = "Build an interactive, modern HTML/CSS/JS dashboard component with real-time KPI counters and live preview.",
                recommendedModel = AIModel.PRO,
                icon = Icons.Default.Code,
                color = NatnaelPrimaryCyan
            ),
            PromptTemplate(
                category = "Reasoning",
                title = "Distributed Architecture Analysis",
                prompt = "Provide a deep mathematical analysis and architectural comparison of high-throughput microservices vs event-driven pipelines.",
                recommendedModel = AIModel.THINKING,
                icon = Icons.Default.Psychology,
                color = NatnaelAccentViolet
            ),
            PromptTemplate(
                category = "Research",
                title = "Ethiopian Tech Ecosystem 2026",
                prompt = "Synthesize recent research on fintech adoption, digital payments (Telebirr/CBE Birr), and AI innovation hubs in Addis Ababa.",
                recommendedModel = AIModel.LITE,
                icon = Icons.Default.Public,
                color = NatnaelAccentGold
            ),
            PromptTemplate(
                category = "Business",
                title = "Executive Investment Memo",
                prompt = "Generate a professional PDF report detailing financial projections, market TAM, and unit economics for an AI SaaS startup.",
                recommendedModel = AIModel.PRO,
                icon = Icons.Default.Business,
                color = NatnaelAccentEmerald
            ),
            PromptTemplate(
                category = "Coding",
                title = "REST API Architecture with Kotlin",
                prompt = "Write a complete clean architecture repository and service layer in Kotlin with Flow and Coroutines handling network state.",
                recommendedModel = AIModel.PRO,
                icon = Icons.Default.Code,
                color = NatnaelPrimaryIndigo
            ),
            PromptTemplate(
                category = "Research",
                title = "Quantum Machine Learning Overview",
                prompt = "Perform web research on recent quantum tensor network algorithms and error mitigation techniques with primary citations.",
                recommendedModel = AIModel.THINKING,
                icon = Icons.Default.Public,
                color = NatnaelAccentRose
            )
        )
    }

    val categories = listOf("All", "Coding", "Reasoning", "Research", "Business")

    val filteredTemplates = remember(templates, searchQuery, selectedCategory) {
        templates.filter { item ->
            (selectedCategory == "All" || item.category == selectedCategory) &&
                    (searchQuery.isBlank() || item.title.contains(searchQuery, ignoreCase = true) || item.prompt.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Explore AI Capabilities & Prompts",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Curated workflows engineered for Natnael AI multi-model reasoning",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search templates, code, research...", fontSize = 13.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Templates List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredTemplates) { template ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                        .clickable { onSelectPrompt(template.prompt, template.recommendedModel) }
                        .testTag("explore_item_${template.title.lowercase().replace(" ", "_")}"),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(template.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = template.icon,
                                contentDescription = null,
                                tint = template.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = template.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(template.color.copy(alpha = 0.12f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = template.recommendedModel.displayName,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = template.color
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = template.prompt,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Use Prompt",
                            tint = NatnaelPrimaryIndigo,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
