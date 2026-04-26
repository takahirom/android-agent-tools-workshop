package com.example.nav2workshopdemo

import kotlinx.serialization.Serializable

@Serializable data object HomeGraph

@Serializable data object LibraryGraph

@Serializable data object HomeRoute

@Serializable data class ModuleDetailRoute(val moduleId: String)

@Serializable data object LibraryRoute

@Serializable data class ToolDetailRoute(val toolId: String)

@Serializable data object MigrationNotesDialogRoute
