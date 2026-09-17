package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIModel
import com.example.data.model.AttachmentItem
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentRose
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NatnaelComposer(
    text: String,
    onTextChange: (String) -> Unit,
    selectedModel: AIModel,
    onSelectModel: (AIModel) -> Unit,
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
    modifier: Modifier = Modifier
) {
    var isModelMenuOpen by remember { mutableStateOf(false) }
    var isPlusMenuOpen by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val micScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListeningVoice) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Active Indicators (Web Research mode pill, etc.)
        if (isWebResearchMode) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(NatnaelPrimaryCyan.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = NatnaelPrimaryCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Web Research Mode Active • Real-time source synthesis",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NatnaelPrimaryCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Disable Web Research",
                    tint = NatnaelPrimaryCyan,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable { onToggleWebResearch() }
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Attachment chips
        if (attachments.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                attachments.forEach { item ->
                    AttachmentChip(
                        item = item,
                        onRemove = { onRemoveAttachment(item.id) }
                    )
                }
            }
        }

        // MAIN COMPOSER CARD
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    if (text.isNotEmpty()) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(20.dp)
                )
                .testTag("ai_composer_container"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // Multiline Input Text Field
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = {
                        Text(
                            text = if (isWebResearchMode) "Search the web with Natnael AI..." else "Ask Natnael AI anything...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp, max = 150.dp)
                        .testTag("composer_text_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = NatnaelPrimaryIndigo
                    ),
                    maxLines = 6
                )

                // BOTTOM ACTION BAR INSIDE COMPOSER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT SIDE: MODEL SELECTOR
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { isModelMenuOpen = true }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("composer_model_selector"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = selectedModel.icon,
                                contentDescription = null,
                                tint = NatnaelPrimaryIndigo,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedModel.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Model Selection Dropdown
                        DropdownMenu(
                            expanded = isModelMenuOpen,
                            onDismissRequest = { isModelMenuOpen = false },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .width(280.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                .padding(4.dp)
                        ) {
                            AIModel.entries.forEach { model ->
                                val isSelected = model == selectedModel
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = model.displayName,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(NatnaelPrimaryIndigo.copy(alpha = 0.12f))
                                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                                ) {
                                                    Text(
                                                        text = model.badge,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = NatnaelPrimaryIndigo
                                                    )
                                                }
                                            }
                                            Text(
                                                text = model.description,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = model.icon,
                                            contentDescription = null,
                                            tint = if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = {
                                        onSelectModel(model)
                                        isModelMenuOpen = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // PLUS (+) ATTACHMENT BUTTON
                    Box {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { isPlusMenuOpen = !isPlusMenuOpen }
                                .testTag("composer_plus_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add attachments and tools",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Floating Plus Menu
                        PlusAttachmentMenu(
                            isOpen = isPlusMenuOpen,
                            onDismiss = { isPlusMenuOpen = false },
                            onSelectWebResearch = onToggleWebResearch,
                            onSelectImageGenerator = onOpenImageGenerator,
                            onSelectUploadFile = onUploadSampleFile,
                            onSelectUploadImage = onUploadSampleImage
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // RIGHT SIDE: VOICE INPUT (MIC)
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .scale(if (isListeningVoice) micScale else 1f)
                            .clip(CircleShape)
                            .background(
                                if (isListeningVoice) NatnaelAccentRose.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onToggleVoice() }
                            .testTag("composer_voice_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = if (isListeningVoice) NatnaelAccentRose else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // SEND / STOP GENERATING BUTTON
                    if (isGenerating) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NatnaelAccentRose)
                                .clickable { onStopGenerating() }
                                .testTag("composer_stop_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop Generating",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        val canSend = text.isNotBlank() || attachments.isNotEmpty()
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (canSend) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable(enabled = canSend) { onSendMessage() }
                                .testTag("composer_send_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Message",
                                tint = if (canSend) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentChip(
    item: AttachmentItem,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = NatnaelAccentGold,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "${item.name} (${item.size})",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove attachment",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(14.dp)
                .clickable { onRemove() }
        )
    }
}
