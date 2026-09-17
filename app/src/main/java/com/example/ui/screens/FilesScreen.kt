package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeneratedFileItem
import com.example.ui.components.GeneratedFileCard
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentRose
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun FilesScreen(
    files: List<GeneratedFileItem>,
    onCreateFile: (name: String, format: String, content: String) -> Unit,
    onDeleteFile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("ALL") }
    var showCreateDialog by remember { mutableStateOf(false) }

    val filters = listOf("ALL", "PDF", "XLSX", "DOCX", "CSV")

    val filteredFiles = remember(files, selectedFilter) {
        if (selectedFilter == "ALL") files
        else files.filter { it.format.equals(selectedFilter, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Generated Files & Documents",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Downloadable PDFs, spreadsheets, reports, and data files",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = NatnaelPrimaryIndigo),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("create_document_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Doc", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FILTER CHIPS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FILES LIST
        if (filteredFiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No files found in $selectedFilter",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredFiles, key = { it.id }) { file ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            GeneratedFileCard(fileItem = file)
                        }
                        IconButton(
                            onClick = { onDeleteFile(file.id) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete File",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // CREATE DOCUMENT DIALOG
    if (showCreateDialog) {
        var docName by remember { mutableStateOf("Executive_Summary_2026") }
        var docFormat by remember { mutableStateOf("PDF") }
        var docContent by remember { mutableStateOf("NATNAEL AI REPORT\n\nTitle: Enterprise Cloud Deployment\nAuthor: Natnael AI Workspace\n\n1. Project Overview\n2. Implementation Milestones\n3. Scalability Roadmap") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create AI Document", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = docName,
                        onValueChange = { docName = it },
                        label = { Text("File Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("PDF", "DOCX", "XLSX", "CSV").forEach { fmt ->
                            val sel = fmt == docFormat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (sel) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { docFormat = fmt }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = fmt,
                                    fontSize = 11.sp,
                                    color = if (sel) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = docContent,
                        onValueChange = { docContent = it },
                        label = { Text("Content Outline") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val fullName = if (docName.contains(".")) docName else "$docName.${docFormat.lowercase()}"
                        onCreateFile(fullName, docFormat, docContent)
                        Toast.makeText(context, "Generated $fullName", Toast.LENGTH_SHORT).show()
                        showCreateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NatnaelPrimaryIndigo)
                ) {
                    Text("Generate Document")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
