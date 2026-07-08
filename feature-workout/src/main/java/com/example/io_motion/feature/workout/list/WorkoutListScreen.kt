package com.example.io_motion.feature.workout.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.io_motion.core.ui.components.PrimaryButton
import com.example.io_motion.core.ui.theme.IOMotionTextStyles
import com.example.io_motion.core.ui.theme.extendedColors
import com.example.io_motion.data.model.Workout
import com.example.io_motion.feature.workout.workoutSummary

@Composable
fun WorkoutListScreen(
    onNavigateBack: () -> Unit,
    onNewWorkout: () -> Unit,
    onEditWorkout: (Long) -> Unit,
    onStartRun: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 36.dp),
    ) {
        BackHeader(title = "Your Workouts", onNavigateBack = onNavigateBack)
        Spacer(Modifier.height(24.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (uiState.workouts.isEmpty() && !uiState.isLoading) {
                Text(
                    text = "No workouts yet.\nBuild your first one below.",
                    style = IOMotionTextStyles.modelVariantCaption,
                    color = MaterialTheme.extendedColors.textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(uiState.workouts, key = { it.id }) { workout ->
                        WorkoutCard(
                            workout = workout,
                            onEdit = { onEditWorkout(workout.id) },
                            onDelete = { viewModel.delete(workout.id) },
                            onStart = { onStartRun(workout.id) },
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        PrimaryButton(text = "+ New Workout", onClick = onNewWorkout)
    }
}

@Composable
private fun WorkoutCard(
    workout: Workout,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStart: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.extendedColors.hairline)
            .padding(16.dp),
    ) {
        Text(text = workout.name, style = IOMotionTextStyles.cardTitle, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(4.dp))
        Text(
            text = workoutSummary(workout.items),
            style = IOMotionTextStyles.repMeta,
            color = MaterialTheme.extendedColors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CardActionButton(label = "Edit", onClick = onEdit, modifier = Modifier.weight(1f))
            CardActionButton(
                label = "Delete",
                onClick = onDelete,
                modifier = Modifier.weight(1f),
                labelColor = MaterialTheme.extendedColors.danger,
            )
            CardActionButton(label = "Start", onClick = onStart, modifier = Modifier.weight(1f), primary = true)
        }
    }
}

@Composable
private fun CardActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
    labelColor: Color? = null,
) {
    val background = if (primary) MaterialTheme.colorScheme.primary else Color.Transparent
    val border = if (primary) null else BorderStroke(1.dp, MaterialTheme.extendedColors.hairline)
    val contentColor = labelColor
        ?: if (primary) MaterialTheme.extendedColors.accentOn else MaterialTheme.colorScheme.onBackground
    Box(
        modifier = modifier
            .then(if (border != null) Modifier.border(border) else Modifier)
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label.uppercase(), style = IOMotionTextStyles.segmentedLabel, color = contentColor)
    }
}

@Composable
internal fun BackHeader(title: String, onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .clickable(onClick = onNavigateBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text = title, style = IOMotionTextStyles.screenTitle, color = MaterialTheme.colorScheme.onBackground)
    }
}
