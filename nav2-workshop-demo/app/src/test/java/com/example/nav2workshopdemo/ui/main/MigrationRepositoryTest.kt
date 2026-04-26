package com.example.nav2workshopdemo.ui.main

import com.example.nav2workshopdemo.data.MigrationRepository
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
