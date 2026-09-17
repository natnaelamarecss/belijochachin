package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NavPage
import com.example.data.model.UserProfile
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.NatnaelAccentGold
import com.example.ui.theme.NatnaelAccentEmerald
import com.example.ui.theme.NatnaelPrimaryCyan
import com.example.ui.theme.NatnaelPrimaryIndigo

@Composable
fun NatnaelTopBar(
    currentPage: NavPage,
    themeMode: AppThemeMode,
    userProfile: UserProfile,
    onToggleSidebar: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sidebar toggle
        IconButton(
            onClick = onToggleSidebar,
            modifier = Modifier.testTag("topbar_sidebar_toggle")
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Toggle Sidebar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Page title & subtitle badge
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentPage.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NatnaelPrimaryIndigo.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Natnael AI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NatnaelPrimaryIndigo
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Global search button
        IconButton(
            onClick = onOpenSearch,
            modifier = Modifier.testTag("topbar_search_button")
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Workspace",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Notification Icon with Badge
        IconButton(
            onClick = onOpenNotifications,
            modifier = Modifier.testTag("topbar_notifications_button")
        ) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = NatnaelAccentGold,
                        contentColor = Color.Black
                    ) {
                        Text("3", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Dark / Light Theme Toggle
        IconButton(
            onClick = onToggleTheme,
            modifier = Modifier.testTag("topbar_theme_toggle")
        ) {
            Icon(
                imageVector = if (themeMode == AppThemeMode.DARK) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Toggle Theme",
                tint = if (themeMode == AppThemeMode.DARK) NatnaelAccentGold else NatnaelPrimaryIndigo
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // User Avatar with Online Indicator
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NatnaelPrimaryIndigo)
                .clickable { onProfileClick() }
                .testTag("topbar_user_avatar"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userProfile.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            // Green online dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(NatnaelAccentEmerald)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
