package com.example.nav2workshopdemo.data

data class MigrationModule(
  val id: String,
  val title: String,
  val summary: String,
  val migrationFocus: String,
  val checkpoints: List<String>,
)

data class MigrationTool(
  val id: String,
  val title: String,
  val description: String,
)

object MigrationRepository {
  val modules =
    listOf(
      MigrationModule(
        id = "routes",
        title = "Typed route inventory",
        summary = "Nav2 destinations are already expressed as serializable route types.",
        migrationFocus = "Step 2: make each route implement NavKey.",
        checkpoints =
          listOf(
            "HomeGraph and LibraryGraph are graph routes.",
            "ModuleDetailRoute carries the selected module id.",
            "MigrationNotesDialogRoute is a dialog destination.",
          ),
      ),
      MigrationModule(
        id = "controller",
        title = "NavController replacement",
        summary = "The app uses rememberNavController and passes event lambdas to screens.",
        migrationFocus = "Steps 3 and 4: introduce NavigationState and Navigator.",
        checkpoints =
          listOf(
            "Screens do not receive NavController directly.",
            "Bottom navigation reads currentBackStackEntryAsState.",
            "Back handling currently delegates to NavController.popBackStack().",
          ),
      ),
      MigrationModule(
        id = "graph",
        title = "Nested graph flattening",
        summary = "Two one-level nested graphs model independent top-level sections.",
        migrationFocus = "Steps 5 and 6: move composable and dialog destinations into entryProvider.",
        checkpoints =
          listOf(
            "HomeGraph starts at HomeRoute.",
            "LibraryGraph starts at LibraryRoute.",
            "The demo avoids deep links and unsupported destination types.",
          ),
      ),
    )

  val tools =
    listOf(
      MigrationTool(
        id = "skill",
        title = "Android navigation-3 skill",
        description = "Use android skills add navigation-3, then ask the agent to migrate this Nav2 baseline.",
      ),
      MigrationTool(
        id = "tests",
        title = "Navigation tests",
        description = "Run the supplied NavHost test before and after migration to confirm the visible behavior stays stable.",
      ),
    )

  fun module(id: String): MigrationModule = modules.firstOrNull { it.id == id } ?: modules.first()

  fun tool(id: String): MigrationTool = tools.firstOrNull { it.id == id } ?: tools.first()
}
