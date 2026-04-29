package com.example.eventschedule

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute : NavKey

@Serializable data class ModuleDetailRoute(val moduleId: String) : NavKey

@Serializable data object LibraryRoute : NavKey

@Serializable data class ToolDetailRoute(val toolId: String) : NavKey

@Serializable data object MigrationNotesDialogRoute : NavKey
