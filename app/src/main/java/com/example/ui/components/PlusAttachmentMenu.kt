package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentViolet
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun PlusAttachmentMenu(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSelectWebResearch: () -> Unit,
    onSelectImageGenerator: () -> Unit,
    onSelectUploadFile: () -> Unit,
    onSelectUploadImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn() + scaleIn(initialScale = 0.9f),
        exit = fadeOut() + scaleOut(targetScale = 0.9f),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(16.dp)
                )
                .testTag("plus_attachment_menu"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // Header
                Text(
                    text = "Add to conversation",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // 1. Web Research
                AttachmentMenuItem(
                    icon = Icons.Default.Public,
                    iconColor = NatnaelPrimaryCyan,
                    title = "Web Research",
                    subtitle = "Search & synthesize live web sources",
                    onClick = {
                        onDismiss()
                        onSelectWebResearch()
                    },
                    testTag = "menu_web_research"
                )

                // 2. Image Generator
                AttachmentMenuItem(
                    icon = Icons.Default.AutoAwesome,
                    iconColor = NatnaelAccentViolet,
                    title = "Image Generator",
                    subtitle = "Create AI visuals from text prompts",
                    onClick = {
                        onDismiss()
                        onSelectImageGenerator()
                    },
                    testTag = "menu_image_generator"
                )

                // 3. Upload File
                AttachmentMenuItem(
                    icon = Icons.Default.Description,
                    iconColor = NatnaelAccentGold,
                    title = "Upload File",
                    subtitle = "PDF, DOCX, CSV, XLSX, TXT",
                    onClick = {
                        onDismiss()
                        onSelectUploadFile()
                    },
                    testTag = "menu_upload_file"
                )

                // 4. Upload Image
                AttachmentMenuItem(
                    icon = Icons.Default.AddPhotoAlternate,
                    iconColor = NatnaelAccentEmerald,
                    title = "Upload Image",
                    subtitle = "Vision analysis, OCR & diagrams",
                    onClick = {
                        onDismiss()
                        onSelectUploadImage()
                    },
                    testTag = "menu_upload_image"
                )
            }
        }
    }
}

@Composable
fun AttachmentMenuItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
