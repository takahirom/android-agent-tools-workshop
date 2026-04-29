package com.example.eventschedule.ui.main

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
import com.example.eventschedule.data.MigrationModule
import com.example.eventschedule.data.MigrationRepository
import com.example.eventschedule.data.MigrationTool
import com.example.eventschedule.theme.MyApplicationTheme

@Composable
fun HomeScreen(
  contentPadding: PaddingValues,
  onModuleClick: (String) -> Unit,
  onOpenNotes: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ScreenScaffold(
    title = "Event Schedule",
    subtitle = "Plan your day and keep useful venue notes close at hand.",
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
      AssistChip(onClick = onOpenNotes, label = { Text(module.time) })
      Spacer(Modifier.height(16.dp))
      Text(module.summary, style = MaterialTheme.typography.bodyLarge)
      Spacer(Modifier.height(24.dp))
      Text("Session details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
      Spacer(Modifier.height(8.dp))
    }
    items(module.details) { detail ->
      DetailRow(detail)
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
    title = "Venue Guide",
    subtitle = "Useful logistics for getting around during the event.",
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
      Text("Saved in the guide so you can find it again without leaving the schedule.", style = MaterialTheme.typography.bodyMedium)
    }
  }
}

@Composable
fun MigrationNotesDialog(onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
    title = { Text("Event notes") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Doors open at 09:00. Coffee is available beside the main hall.")
        Text("The help desk can answer schedule, room, and accessibility questions throughout the day.")
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
        top = contentPadding.calculateTopPadding(),
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
        top = contentPadding.calculateTopPadding(),
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
      Text(module.time, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
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
private fun DetailRow(text: String) {
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
