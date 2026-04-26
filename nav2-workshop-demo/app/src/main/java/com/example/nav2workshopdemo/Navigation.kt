package com.example.nav2workshopdemo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.nav2workshopdemo.ui.main.HomeScreen
import com.example.nav2workshopdemo.ui.main.LibraryScreen
import com.example.nav2workshopdemo.ui.main.MigrationNotesDialog
import com.example.nav2workshopdemo.ui.main.ModuleDetailScreen
import com.example.nav2workshopdemo.ui.main.ToolDetailScreen
import kotlin.reflect.KClass

@Composable
fun MainNavigation() {
  val navController = rememberNavController()
  val topLevelItems =
    listOf(
      TopLevelDestination("Home", HomeGraph, HomeGraph::class) { Icon(Icons.Filled.Home, contentDescription = null) },
      TopLevelDestination("Guide", LibraryGraph, LibraryGraph::class) { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
    )
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = backStackEntry?.destination

  Scaffold(
    bottomBar = {
      NavigationBar {
        topLevelItems.forEach { destination ->
          NavigationBarItem(
            selected = currentDestination.isRouteInHierarchy(destination.routeClass),
            onClick = {
              navController.navigate(destination.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                  saveState = true
                }
                launchSingleTop = true
                restoreState = true
              }
            },
            icon = destination.icon,
            label = { Text(destination.label) },
          )
        }
      }
    },
  ) { innerPadding ->
    NavHost(navController = navController, startDestination = HomeGraph) {
      navigation<HomeGraph>(startDestination = HomeRoute) {
        composable<HomeRoute> {
          HomeScreen(
            contentPadding = innerPadding,
            onModuleClick = { moduleId -> navController.navigate(ModuleDetailRoute(moduleId)) },
            onOpenNotes = { navController.navigate(MigrationNotesDialogRoute) },
          )
        }
        composable<ModuleDetailRoute> { entry ->
          val route = entry.toRoute<ModuleDetailRoute>()
          ModuleDetailScreen(
            moduleId = route.moduleId,
            contentPadding = innerPadding,
            onBack = { navController.popBackStack() },
            onOpenNotes = { navController.navigate(MigrationNotesDialogRoute) },
          )
        }
        dialog<MigrationNotesDialogRoute> {
          MigrationNotesDialog(onDismiss = { navController.popBackStack() })
        }
      }

      navigation<LibraryGraph>(startDestination = LibraryRoute) {
        composable<LibraryRoute> {
          LibraryScreen(
            contentPadding = innerPadding,
            onToolClick = { toolId -> navController.navigate(ToolDetailRoute(toolId)) },
            onOpenNotes = { navController.navigate(MigrationNotesDialogRoute) },
          )
        }
        composable<ToolDetailRoute> { entry ->
          val route = entry.toRoute<ToolDetailRoute>()
          ToolDetailScreen(
            toolId = route.toolId,
            contentPadding = innerPadding,
            onBack = { navController.popBackStack() },
          )
        }
        dialog<MigrationNotesDialogRoute> {
          MigrationNotesDialog(onDismiss = { navController.popBackStack() })
        }
      }
    }
  }
}

private data class TopLevelDestination(
  val label: String,
  val route: Any,
  val routeClass: KClass<*>,
  val icon: @Composable () -> Unit,
)

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
  this?.hierarchy?.any { it.hasRoute(route) } == true
