package com.example.nav2workshopdemo.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.nav2workshopdemo.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent {
      HomeScreen(contentPadding = PaddingValues(), onModuleClick = {}, onOpenNotes = {})
    }
  }

  @Test
  fun modules_areDisplayed() {
    composeTestRule.onNodeWithText("Typed route inventory").assertExists()
    composeTestRule.onNodeWithText("NavController replacement").assertExists()
    composeTestRule.onNodeWithText("Nested graph flattening").assertExists()
  }
}
