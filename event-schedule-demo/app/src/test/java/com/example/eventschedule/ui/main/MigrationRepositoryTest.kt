package com.example.eventschedule.ui.main

import com.example.eventschedule.data.MigrationRepository
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MigrationRepositoryTest {
  @Test
  fun module_unknownId_fallsBackToFirstModule() {
    assertEquals(MigrationRepository.modules.first(), MigrationRepository.module("missing"))
  }

  @Test
  fun tool_unknownId_fallsBackToFirstTool() {
    assertEquals(MigrationRepository.tools.first(), MigrationRepository.tool("missing"))
  }
}
