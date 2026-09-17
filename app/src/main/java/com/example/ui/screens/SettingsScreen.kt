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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIModel
import com.example.data.model.UserProfile
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentRose
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    themeMode: AppThemeMode,
    onSelectTheme: (AppThemeMode) -> Unit,
    selectedModel: AIModel,
    onSelectDefaultModel: (AIModel) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var temperature by remember { mutableFloatStateOf(0.7f) }
    var streamResponses by remember { mutableStateOf(true) }
    var deepReasoningTrace by remember { mutableStateOf(true) }
    var customInstructions by remember {
        mutableStateOf("You are Natnael AI, an authoritative, intelligent engineering assistant with Ethiopian-inspired professional design and multi-modal problem solving.")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Preferences & Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Configure Natnael AI workspaces, theme styles, and AI reasoning parameters",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 1. ACCOUNT PROFILE CARD
        item {
            SettingsCard(title = "Account & Profile", icon = Icons.Default.Person) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(NatnaelPrimaryIndigo),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = userProfile.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = userProfile.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "${userProfile.role} • ${userProfile.plan}", fontSize = 11.sp, color = NatnaelAccentGold, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 2. APPEARANCE & THEME
        item {
            SettingsCard(title = "Appearance & Theme", icon = Icons.Default.BrightnessMedium) {
                Text(
                    text = "Select your preferred color scheme",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOptionButton(
                        title = "Dark",
                        icon = Icons.Default.DarkMode,
                        isSelected = themeMode == AppThemeMode.DARK,
                        onClick = { onSelectTheme(AppThemeMode.DARK) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionButton(
                        title = "Light",
                        icon = Icons.Default.LightMode,
                        isSelected = themeMode == AppThemeMode.LIGHT,
                        onClick = { onSelectTheme(AppThemeMode.LIGHT) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionButton(
                        title = "System",
                        icon = Icons.Default.BrightnessMedium,
                        isSelected = themeMode == AppThemeMode.SYSTEM,
                        onClick = { onSelectTheme(AppThemeMode.SYSTEM) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. AI REASONING PREFERENCES
        item {
            SettingsCard(title = "AI Model & Reasoning Preferences", icon = Icons.Default.Psychology) {
                Text(
                    text = "Default Model",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AIModel.entries.forEach { model ->
                        val isSelected = model == selectedModel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onSelectDefaultModel(model) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = model.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reasoning depth toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Deep Reasoning Traces", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Show step-by-step thinking breakdown accordion", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = deepReasoningTrace,
                        onCheckedChange = { deepReasoningTrace = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NatnaelPrimaryIndigo)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Creativity Temperature Slider
                Text(
                    text = "Creativity & Temperature: ${String.format("%.1f", temperature)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Slider(
                    value = temperature,
                    onValueChange = { temperature = it },
                    valueRange = 0.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = NatnaelPrimaryIndigo,
                        activeTrackColor = NatnaelPrimaryIndigo
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Instructions
                Text(
                    text = "Custom System Instructions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = customInstructions,
                    onValueChange = { customInstructions = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 3
                )
            }
        }

        // 4. DATA & PRIVACY
        item {
            SettingsCard(title = "Security & Data Storage", icon = Icons.Default.Shield) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = NatnaelAccentEmerald, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "API Key Integration", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Managed via Secrets / BuildConfig", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NatnaelAccentEmerald.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "Active", fontSize = 10.sp, color = NatnaelAccentEmerald, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Exported workspace backup JSON", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Data", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Button(
                        onClick = {
                            onClearHistory()
                            Toast.makeText(context, "Cleared conversation history", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NatnaelAccentRose.copy(alpha = 0.15f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = NatnaelAccentRose, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Clear History", fontSize = 12.sp, color = NatnaelAccentRose)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun SettingsCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = NatnaelPrimaryIndigo, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun ThemeOptionButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                1.dp,
                if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        color = if (isSelected) NatnaelPrimaryIndigo.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) NatnaelPrimaryIndigo else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
