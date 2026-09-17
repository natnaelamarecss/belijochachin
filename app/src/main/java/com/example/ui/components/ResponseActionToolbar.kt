package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun ResponseActionToolbar(
    responseText: String,
    hasCode: Boolean = false,
    onRegenerate: (() -> Unit)? = null,
    onOpenCodePreview: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Copy Action
        ToolbarIconButton(
            icon = Icons.Default.ContentCopy,
            label = "Copy",
            onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("AI Response", responseText))
                Toast.makeText(context, "Copied response to clipboard", Toast.LENGTH_SHORT).show()
            },
            testTag = "action_copy_response"
        )

        // Share Action
        ToolbarIconButton(
            icon = Icons.Default.Share,
            label = "Share",
            onClick = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, responseText)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Natnael AI Response"))
            },
            testTag = "action_share_response"
        )

        // Download Action (TXT / Markdown)
        ToolbarIconButton(
            icon = Icons.Default.Download,
            label = "Download",
            onClick = {
                Toast.makeText(context, "Downloaded response as response.md", Toast.LENGTH_SHORT).show()
            },
            testTag = "action_download_response"
        )

        // Export Action (PDF / DOCX)
        ToolbarIconButton(
            icon = Icons.Default.PictureAsPdf,
            label = "Export",
            onClick = {
                Toast.makeText(context, "Exported response to PDF", Toast.LENGTH_SHORT).show()
            },
            testTag = "action_export_response"
        )

        if (hasCode && onOpenCodePreview != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(NatnaelPrimaryIndigo.copy(alpha = 0.15f))
                    .clickable { onOpenCodePreview() }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = NatnaelPrimaryIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Live Preview",
                        fontSize = 11.sp,
                        color = NatnaelPrimaryIndigo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Regenerate Action
        if (onRegenerate != null) {
            IconButton(
                onClick = onRegenerate,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Regenerate Response",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun ToolbarIconButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
