package com.example.eventschedule.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.eventschedule.MainActivity
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
  fun sessions_areDisplayed() {
    composeTestRule.onNodeWithText("Opening Keynote").assertExists()
    composeTestRule.onNodeWithText("Design Systems Clinic").assertExists()
    composeTestRule.onNodeWithText("Automation Lab").assertExists()
  }
}
