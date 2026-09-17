package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.NavPage
import com.example.data.model.UserProfile
import com.example.ui.components.NatnaelTopBar
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.NatnaelAITheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      NatnaelAITheme {
        NatnaelTopBar(
          currentPage = NavPage.HOME,
          themeMode = AppThemeMode.DARK,
          userProfile = UserProfile(),
          onToggleSidebar = {},
          onToggleTheme = {},
          onOpenSearch = {},
          onOpenNotifications = {},
          onProfileClick = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
