package com.example.eventschedule

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.eventschedule.ui.main.HomeScreen
import com.example.eventschedule.ui.main.LibraryScreen
import com.example.eventschedule.ui.main.MigrationNotesDialog
import com.example.eventschedule.ui.main.ModuleDetailScreen
import com.example.eventschedule.ui.main.ToolDetailScreen

@Composable
fun MainNavigation() {
  val topLevelItems =
    listOf(
      TopLevelDestination("Home", HomeRoute) { Icon(Icons.Filled.Home, contentDescription = null) },
      TopLevelDestination("Guide", LibraryRoute) { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
    )
  val navigationState =
    rememberNavigationState(
      startRoute = HomeRoute,
      topLevelRoutes = topLevelItems.map { it.route }.toSet(),
    )
  val navigator = remember(navigationState) { Navigator(navigationState) }
  val dialogSceneStrategy = remember { DialogSceneStrategy<NavKey>() }

  Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    bottomBar = {
      NavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
        topLevelItems.forEach { destination ->
          NavigationBarItem(
            selected = destination.route == navigationState.topLevelRoute,
            onClick = { navigator.navigate(destination.route) },
            icon = destination.icon,
            label = { Text(destination.label) },
          )
        }
      }
    },
  ) { innerPadding ->
    val entryProvider =
      entryProvider {
        entry<HomeRoute> {
          HomeScreen(
            contentPadding = innerPadding,
            onModuleClick = { moduleId -> navigator.navigate(ModuleDetailRoute(moduleId)) },
            onOpenNotes = { navigator.navigate(MigrationNotesDialogRoute) },
          )
        }
        entry<ModuleDetailRoute> { route ->
          ModuleDetailScreen(
            moduleId = route.moduleId,
            contentPadding = innerPadding,
            onBack = { navigator.goBack() },
            onOpenNotes = { navigator.navigate(MigrationNotesDialogRoute) },
          )
        }
        entry<LibraryRoute> {
          LibraryScreen(
            contentPadding = innerPadding,
            onToolClick = { toolId -> navigator.navigate(ToolDetailRoute(toolId)) },
            onOpenNotes = { navigator.navigate(MigrationNotesDialogRoute) },
          )
        }
        entry<ToolDetailRoute> { route ->
          ToolDetailScreen(
            toolId = route.toolId,
            contentPadding = innerPadding,
            onBack = { navigator.goBack() },
          )
        }
        entry<MigrationNotesDialogRoute>(
          metadata = DialogSceneStrategy.dialog(),
        ) {
          MigrationNotesDialog(onDismiss = { navigator.goBack() })
        }
      }

    NavDisplay(
      entries = navigationState.toEntries(entryProvider),
      onBack = { navigator.goBack() },
      sceneStrategy = dialogSceneStrategy,
    )
  }
}

private data class TopLevelDestination(
  val label: String,
  val route: NavKey,
  val icon: @Composable () -> Unit,
)
