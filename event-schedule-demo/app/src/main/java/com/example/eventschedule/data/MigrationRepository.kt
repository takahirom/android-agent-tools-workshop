package com.example.eventschedule.data

data class MigrationModule(
  val id: String,
  val title: String,
  val summary: String,
  val time: String,
  val details: List<String>,
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
        id = "keynote",
        title = "Opening Keynote",
        summary = "A quick tour of the day and the product themes behind the sessions.",
        time = "09:30",
        details =
          listOf(
            "Main hall",
            "Speaker: Maya Tanaka",
            "Good for product, design, and engineering attendees.",
          ),
      ),
      MigrationModule(
        id = "design-systems",
        title = "Design Systems Clinic",
        summary = "Practical patterns for keeping product screens consistent as teams grow.",
        time = "11:00",
        details =
          listOf(
            "Studio room",
            "Speaker: Jordan Lee",
            "Bring one UI consistency issue from your own product.",
          ),
      ),
      MigrationModule(
        id = "automation",
        title = "Automation Lab",
        summary = "Hands-on examples for using agents to make routine product work safer.",
        time = "14:00",
        details =
          listOf(
            "Workshop room B",
            "Speaker: Priya Shah",
            "Laptop recommended for the guided exercise.",
          ),
      ),
    )

  val tools =
    listOf(
      MigrationTool(
        id = "venue",
        title = "Venue Map",
        description = "Find the main hall, studio room, workshop rooms, restrooms, and quiet seating areas.",
      ),
      MigrationTool(
        id = "wifi",
        title = "Wi-Fi and Help Desk",
        description = "Network: EventGuest. Visit the help desk near registration for access issues or charger loans.",
      ),
    )

  fun module(id: String): MigrationModule = modules.firstOrNull { it.id == id } ?: modules.first()

  fun tool(id: String): MigrationTool = tools.firstOrNull { it.id == id } ?: tools.first()
}
