package com.example.nav2workshopdemo.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nav2workshopdemo.data.MigrationModule
import com.example.nav2workshopdemo.data.MigrationRepository
import com.example.nav2workshopdemo.data.MigrationTool
import com.example.nav2workshopdemo.theme.MyApplicationTheme

@Composable
fun HomeScreen(
  contentPadding: PaddingValues,
  onModuleClick: (String) -> Unit,
  onOpenNotes: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ScreenScaffold(
    title = "Navigation Migration Demo",
    subtitle = "A compact sample app for practicing the official Navigation 3 migration skill.",
    contentPadding = contentPadding,
    action = { NotesButton(onOpenNotes) },
    modifier = modifier,
  ) {
    items(MigrationRepository.modules) { module ->
      ModuleCard(module = module, onClick = { onModuleClick(module.id) })
    }
  }
}

@Composable
fun ModuleDetailScreen(
  moduleId: String,
  contentPadding: PaddingValues,
  onBack: () -> Unit,
  onOpenNotes: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val module = MigrationRepository.module(moduleId)

  DetailScaffold(
    title = module.title,
    contentPadding = contentPadding,
    onBack = onBack,
    action = { NotesButton(onOpenNotes) },
    modifier = modifier,
  ) {
    item {
      AssistChip(onClick = onOpenNotes, label = { Text(module.migrationFocus) })
      Spacer(Modifier.height(16.dp))
      Text(module.summary, style = MaterialTheme.typography.bodyLarge)
      Spacer(Modifier.height(24.dp))
      Text("Migration checkpoints", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(8.dp))
    }
    items(module.checkpoints) { checkpoint ->
      CheckpointRow(checkpoint)
    }
  }
}

@Composable
fun LibraryScreen(
  contentPadding: PaddingValues,
  onToolClick: (String) -> Unit,
  onOpenNotes: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ScreenScaffold(
    title = "Migration Notes",
    subtitle = "Short references to use while running the Android Skills demonstration.",
    contentPadding = contentPadding,
    action = { NotesButton(onOpenNotes) },
    modifier = modifier,
  ) {
    items(MigrationRepository.tools) { tool ->
      ToolCard(tool = tool, onClick = { onToolClick(tool.id) })
    }
  }
}

@Composable
fun ToolDetailScreen(
  toolId: String,
  contentPadding: PaddingValues,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val tool = MigrationRepository.tool(toolId)

  DetailScaffold(title = tool.title, contentPadding = contentPadding, onBack = onBack, modifier = modifier) {
    item {
      Text(tool.description, style = MaterialTheme.typography.bodyLarge)
      Spacer(Modifier.height(20.dp))
      Text("This screen exists to demonstrate a second top-level back stack in Nav2.", style = MaterialTheme.typography.bodyMedium)
    }
  }
}

@Composable
fun MigrationNotesDialog(onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
    title = { Text("Migration scope") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("This baseline intentionally uses Navigation 2 APIs.")
        Text("It avoids deep links and custom destination types so the official migration guide can proceed without unsupported cases.")
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close")
      }
    },
  )
}

@Composable
private fun ScreenScaffold(
  title: String,
  subtitle: String,
  contentPadding: PaddingValues,
  action: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding =
      PaddingValues(
        start = 20.dp,
        top = contentPadding.calculateTopPadding() + 24.dp,
        end = 20.dp,
        bottom = contentPadding.calculateBottomPadding() + 24.dp,
      ),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
          Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
          Spacer(Modifier.height(6.dp))
          Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.width(12.dp))
        action()
      }
    }
    content()
  }
}

@Composable
private fun DetailScaffold(
  title: String,
  contentPadding: PaddingValues,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
  action: @Composable (() -> Unit)? = null,
  content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding =
      PaddingValues(
        start = 20.dp,
        top = contentPadding.calculateTopPadding() + 12.dp,
        end = 20.dp,
        bottom = contentPadding.calculateBottomPadding() + 24.dp,
      ),
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        action?.invoke()
      }
      Spacer(Modifier.height(20.dp))
    }
    content()
  }
}

@Composable
private fun ModuleCard(module: MigrationModule, onClick: () -> Unit) {
  Card(
    onClick = onClick,
    shape = MaterialTheme.shapes.small,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(module.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(6.dp))
      Text(module.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(Modifier.height(12.dp))
      Text(module.migrationFocus, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
  }
}

@Composable
private fun ToolCard(tool: MigrationTool, onClick: () -> Unit) {
  Card(
    onClick = onClick,
    shape = MaterialTheme.shapes.small,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(tool.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(6.dp))
      Text(tool.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun CheckpointRow(text: String) {
  Surface(
    shape = MaterialTheme.shapes.small,
    color = MaterialTheme.colorScheme.secondaryContainer,
    modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
  ) {
    Text(text = text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(12.dp))
  }
}

@Composable
private fun NotesButton(onClick: () -> Unit) {
  OutlinedButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 12.dp)) {
    Icon(Icons.Filled.Info, contentDescription = null)
    Spacer(Modifier.width(8.dp))
    Text("Notes")
  }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  MyApplicationTheme {
    HomeScreen(contentPadding = PaddingValues(), onModuleClick = {}, onOpenNotes = {})
  }
}
