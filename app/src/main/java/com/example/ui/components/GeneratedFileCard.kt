package com.example.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeneratedFileItem
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentRose
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun GeneratedFileCard(
    fileItem: GeneratedFileItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPreviewModal by remember { mutableStateOf(false) }

    val formatColor = when (fileItem.format.uppercase()) {
        "PDF" -> NatnaelAccentRose
        "XLSX", "CSV" -> NatnaelAccentEmerald
        "DOCX" -> NatnaelPrimaryCyan
        else -> NatnaelAccentGold
    }

    val formatIcon = when (fileItem.format.uppercase()) {
        "PDF" -> Icons.Default.PictureAsPdf
        "XLSX", "CSV" -> Icons.Default.TableChart
        else -> Icons.Default.Description
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .testTag("generated_file_card_${fileItem.id}"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with Format Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(formatColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = formatIcon,
                    contentDescription = fileItem.format,
                    tint = formatColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // File Name & Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fileItem.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(formatColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = fileItem.format.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = formatColor
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = fileItem.size,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Preview Action
            IconButton(
                onClick = { showPreviewModal = true },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Preview File",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Download Action
            IconButton(
                onClick = {
                    Toast.makeText(context, "Downloaded ${fileItem.name} successfully!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download File",
                    tint = NatnaelPrimaryIndigo,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Share Action
            IconButton(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "File: ${fileItem.name}\n\n${fileItem.previewContent}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share ${fileItem.name}"))
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share File",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    // File Preview Modal Dialog
    if (showPreviewModal) {
        AlertDialog(
            onDismissRequest = { showPreviewModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = formatIcon,
                        contentDescription = null,
                        tint = formatColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = fileItem.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Preview generated document format (${fileItem.format}):",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp)),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = fileItem.previewContent,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Exported ${fileItem.name}", Toast.LENGTH_SHORT).show()
                        showPreviewModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NatnaelPrimaryIndigo)
                ) {
                    Text("Download File")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPreviewModal = false }) {
                    Text("Close")
                }
            }
        )
    }
}
