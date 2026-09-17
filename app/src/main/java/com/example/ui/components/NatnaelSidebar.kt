package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.NavPage
import com.example.data.model.UserProfile
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun NatnaelSidebar(
    currentPage: NavPage,
    isExpanded: Boolean,
    isProfileMenuOpen: Boolean,
    userProfile: UserProfile,
    onSelectPage: (NavPage) -> Unit,
    onToggleCollapse: () -> Unit,
    onToggleProfileMenu: () -> Unit,
    onDismissProfileMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sidebarWidth by animateDpAsState(
        targetValue = if (isExpanded) 260.dp else 76.dp,
        label = "sidebar_width"
    )

    Surface(
        modifier = modifier
            .width(sidebarWidth)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp)
        ) {
            // BRANDING HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectPage(NavPage.HOME) }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NatnaelPrimaryIndigo),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_natnael_ai_logo),
                        contentDescription = "Natnael AI Logo",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Natnael",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NatnaelPrimaryIndigo
                            )
                        }
                        Text(
                            text = "Intelligent Workspace",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // NAVIGATION MENU ITEMS
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                NavPage.entries.forEach { page ->
                    val isSelected = page == currentPage
                    SidebarNavItem(
                        title = page.title,
                        icon = page.icon,
                        isSelected = isSelected,
                        isExpanded = isExpanded,
                        onClick = { onSelectPage(page) }
                    )
                }
            }

            // COLLAPSE / EXPAND TOGGLE
            IconButton(
                onClick = onToggleCollapse,
                modifier = Modifier
                    .align(if (isExpanded) Alignment.End else Alignment.CenterHorizontally)
                    .testTag("sidebar_collapse_toggle")
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = if (isExpanded) "Collapse Sidebar" else "Expand Sidebar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // USER PROFILE SECTION WITH DROPDOWN
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isProfileMenuOpen) MaterialTheme.colorScheme.surfaceVariant
                            else Color.Transparent
                        )
                        .clickable { onToggleProfileMenu() }
                        .padding(8.dp)
                        .testTag("sidebar_profile_section"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular Avatar with online indicator
                    Box(
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NatnaelPrimaryIndigo),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NA",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(NatnaelAccentEmerald)
                                .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = userProfile.role,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                        // Plan badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NatnaelAccentGold.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PRO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = NatnaelAccentGold
                            )
                        }
                    }
                }

                // Profile Dropdown Menu
                DropdownMenu(
                    expanded = isProfileMenuOpen,
                    onDismissRequest = onDismissProfileMenu,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("My Profile", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                            onSelectPage(NavPage.SETTINGS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Account Settings", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                            onSelectPage(NavPage.SETTINGS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Billing & Plans", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                            onSelectPage(NavPage.SETTINGS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Notifications", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                            onSelectPage(NavPage.SETTINGS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Privacy & Security", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                            onSelectPage(NavPage.SETTINGS)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Help & Support", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.Help, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                            onSelectPage(NavPage.EXPLORE)
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    DropdownMenuItem(
                        text = { Text("Log Out", fontSize = 13.sp, color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            onDismissProfileMenu()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SidebarNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 10.dp)
            .testTag("nav_item_${title.lowercase().replace(" ", "_")}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Accent vertical bar if selected
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(NatnaelPrimaryIndigo)
            )
            Spacer(modifier = Modifier.width(7.dp))
        }

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )

        if (isExpanded) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = fontWeight,
                color = tint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
