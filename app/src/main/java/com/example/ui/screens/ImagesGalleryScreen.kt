package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.GeneratedImageItem
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentRose
import com.example.ui.theme.NatnaelAccentViolet
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun ImagesGalleryScreen(
    images: List<GeneratedImageItem>,
    onGenerateImage: (prompt: String, style: String, aspectRatio: String) -> Unit,
    onToggleFavorite: (GeneratedImageItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var promptText by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Futuristic") }
    var selectedRatio by remember { mutableStateOf("1:1") }
    var isGenerating by remember { mutableStateOf(false) }
    var previewImage by remember { mutableStateOf<GeneratedImageItem?>(null) }

    val styles = listOf("Futuristic", "Cyberpunk Neo-Ethiopia", "Photorealistic", "Minimalist 3D Vector", "Anime Art")
    val ratios = listOf("1:1", "16:9", "9:16", "4:3")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // GENERATION PROMPT CARD
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, NatnaelAccentViolet.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .testTag("image_generator_studio"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NatnaelAccentViolet.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NatnaelAccentViolet,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Image Synthesis Studio",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = {
                        Text(
                            text = "Describe an image in detail (e.g. Addis Ababa skyline 2050 with golden quantum solar towers)...",
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("image_prompt_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Style Selector Chips
                Text(
                    text = "Aesthetic Style",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    styles.forEach { style ->
                        val isSelected = style == selectedStyle
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NatnaelAccentViolet else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedStyle = style }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = style,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Aspect Ratio Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ratio: ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    ratios.forEach { ratio ->
                        val isSelected = ratio == selectedRatio
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedRatio = ratio }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = ratio,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (promptText.isNotBlank()) {
                                isGenerating = true
                                onGenerateImage(promptText, selectedStyle, selectedRatio)
                                Toast.makeText(context, "Generating AI artwork...", Toast.LENGTH_SHORT).show()
                                promptText = ""
                                isGenerating = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NatnaelAccentViolet),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("generate_image_button")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                        } else {
                            Text("Generate", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // GALLERY TITLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Generated Artwork Gallery (${images.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // IMAGE GRID
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(images, key = { it.id }) { img ->
                ImageGalleryCard(
                    item = img,
                    onToggleFavorite = { onToggleFavorite(img) },
                    onClick = { previewImage = img }
                )
            }
        }
    }

    // Fullscreen Image Preview Dialog
    if (previewImage != null) {
        val img = previewImage!!
        AlertDialog(
            onDismissRequest = { previewImage = null },
            title = {
                Text(text = img.style, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(img.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = img.prompt,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = img.prompt, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Downloaded high-res image!", Toast.LENGTH_SHORT).show()
                        previewImage = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NatnaelAccentViolet)
                ) {
                    Text("Download")
                }
            },
            dismissButton = {
                TextButton(onClick = { previewImage = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun ImageGalleryCard(
    item: GeneratedImageItem,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("image_card_${item.id}"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.prompt,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Favorite button in top right
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onToggleFavorite() }
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (item.isFavorite) NatnaelAccentRose else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.prompt,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NatnaelAccentViolet.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.style,
                            fontSize = 9.sp,
                            color = NatnaelAccentViolet,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Generated with Natnael AI: ${item.prompt}\n${item.imageUrl}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Image"))
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
