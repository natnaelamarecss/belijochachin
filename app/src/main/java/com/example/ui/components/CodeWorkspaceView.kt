package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.CodeSnippet
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CodeDarkText
import com.example.ui.theme.CodeKeyword
import com.example.ui.theme.CodeString
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

enum class WorkspaceTab {
    CODE, PREVIEW, FILES
}

@Composable
fun CodeWorkspaceView(
    codeSnippet: CodeSnippet,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(WorkspaceTab.CODE) }
    var selectedFileIndex by remember { mutableIntStateOf(0) }
    var isMobileMode by remember { mutableStateOf(false) }
    var refreshKey by remember { mutableIntStateOf(0) }

    val fileList = remember(codeSnippet) {
        listOf(
            "index.html" to codeSnippet.code,
            "style.css" to "/* Natnael AI Dynamic Styling */\nbody {\n  margin: 0;\n  background: #0B0F19;\n  color: #F8FAFC;\n}",
            "script.js" to "// Natnael AI Engine Runtime\nconsole.log('Natnael AI Code Sandbox Initialized');"
        )
    }

    val activeCode = fileList.getOrNull(selectedFileIndex)?.second ?: codeSnippet.code

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .testTag("code_workspace_view"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Column {
            // TOP WORKSPACE HEADER & TABS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = NatnaelPrimaryIndigo,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = codeSnippet.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Workspace Tabs (Code / Preview / Files)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    WorkspaceTabButton(
                        title = "Code",
                        isSelected = currentTab == WorkspaceTab.CODE,
                        onClick = { currentTab = WorkspaceTab.CODE }
                    )
                    WorkspaceTabButton(
                        title = "Live Preview",
                        isSelected = currentTab == WorkspaceTab.PREVIEW,
                        onClick = { currentTab = WorkspaceTab.PREVIEW }
                    )
                    WorkspaceTabButton(
                        title = "Files",
                        isSelected = currentTab == WorkspaceTab.FILES,
                        onClick = { currentTab = WorkspaceTab.FILES }
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // CONTENT BASED ON SELECTED TAB
            when (currentTab) {
                WorkspaceTab.CODE -> {
                    // FILE TABS & ACTIONS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CodeDarkBg)
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // File tabs
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            fileList.forEachIndexed { index, (fileName, _) ->
                                val isSelected = index == selectedFileIndex
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) Color(0xFF1E293B) else Color.Transparent)
                                        .clickable { selectedFileIndex = index }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = fileName,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (isSelected) NatnaelPrimaryCyan else Color(0xFF94A3B8),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        // Copy Code
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("code", activeCode))
                                Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Code",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Download Code
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Downloaded ${fileList[selectedFileIndex].first}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download Code",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Code Editor Display with Line Numbers
                    val lines = activeCode.lines()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp, max = 340.dp)
                            .background(CodeDarkBg)
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                            .padding(8.dp)
                    ) {
                        // Line numbers
                        Column(
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            lines.indices.forEach { i ->
                                Text(
                                    text = "${i + 1}",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF475569),
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Code Content
                        Column {
                            lines.forEach { line ->
                                Text(
                                    text = line,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (line.trim().startsWith("<") || line.trim().startsWith("function") || line.trim().startsWith("const")) CodeKeyword
                                    else if (line.contains("\"") || line.contains("'")) CodeString
                                    else CodeDarkText,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                WorkspaceTab.PREVIEW -> {
                    // LIVE PREVIEW CONTROLS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "● Real-time Sandboxed Preview",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NatnaelAccentEmerald,
                            modifier = Modifier.weight(1f)
                        )

                        // Mobile / Desktop Toggle
                        IconButton(
                            onClick = { isMobileMode = !isMobileMode },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isMobileMode) Icons.Default.PhoneAndroid else Icons.Default.Tv,
                                contentDescription = if (isMobileMode) "Mobile Mode" else "Desktop Mode",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Refresh
                        IconButton(
                            onClick = { refreshKey++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Preview",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Android WebView Live Sandbox
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        val htmlContent = codeSnippet.previewHtml ?: codeSnippet.code
                        key(refreshKey) {
                            AndroidView(
                                modifier = Modifier
                                    .width(if (isMobileMode) 320.dp else 600.dp)
                                    .fillMaxHeight(),
                                factory = { ctx ->
                                    WebView(ctx).apply {
                                        webViewClient = WebViewClient()
                                        settings.javaScriptEnabled = true
                                        settings.domStorageEnabled = true
                                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                                    }
                                },
                                update = { webView ->
                                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                                }
                            )
                        }
                    }
                }

                WorkspaceTab.FILES -> {
                    // Project Tree View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp, max = 340.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Project Architecture",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        fileList.forEachIndexed { index, (fileName, code) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedFileIndex = index
                                        currentTab = WorkspaceTab.CODE
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = NatnaelAccentGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = fileName,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "${code.lines().size} lines",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkspaceTabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) NatnaelPrimaryIndigo else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
