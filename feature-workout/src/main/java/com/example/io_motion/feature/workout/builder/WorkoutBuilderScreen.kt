@file:OptIn(ExperimentalLayoutApi::class)

package com.example.io_motion.feature.workout.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import com.example.io_motion.core.common.models.ExerciseType
import com.example.io_motion.core.common.models.displayName
import com.example.io_motion.core.ui.components.PrimaryButton
import com.example.io_motion.core.ui.components.SectionLabel
import com.example.io_motion.core.ui.theme.IOMotionTextStyles
import com.example.io_motion.core.ui.theme.Manrope
import com.example.io_motion.core.ui.theme.extendedColors
import com.example.io_motion.feature.workout.list.BackHeader

@Composable
fun WorkoutBuilderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutBuilderViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.saved.collect { onNavigateBack() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 36.dp),
    ) {
        BackHeader(
            title = if (uiState.isEditing) "Edit Workout" else "New Workout",
            onNavigateBack = onNavigateBack,
        )

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(24.dp))
            SectionLabel(text = "WORKOUT NAME")
            Spacer(Modifier.height(12.dp))
            NameField(value = uiState.name, onValueChange = viewModel::setName)

            Spacer(Modifier.height(28.dp))
            SectionLabel(text = "ADD EXERCISE")
            Spacer(Modifier.height(12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ExerciseType.entries.forEach { type ->
                    ExerciseChip(label = type.displayName(), onClick = { viewModel.addExercise(type) })
                }
            }

            Spacer(Modifier.height(28.dp))
            SectionLabel(text = "ROUTINE")
            Spacer(Modifier.height(12.dp))
            if (uiState.items.isEmpty()) {
                Text(
                    text = "Tap an exercise above to add it to this routine.",
                    style = IOMotionTextStyles.modelVariantCaption,
                    color = MaterialTheme.extendedColors.textMuted,
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.items.forEach { item ->
                        RoutineRow(
                            item = item,
                            onRemove = { viewModel.removeItem(item.localId) },
                            onStepSets = { up -> viewModel.stepSets(item.localId, up) },
                            onStepReps = { up -> viewModel.stepReps(item.localId, up) },
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        Spacer(Modifier.height(8.dp))
        PrimaryButton(text = "Save Workout", onClick = viewModel::save)
    }
}

@Composable
private fun NameField(value: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.extendedColors.hairline)
                    .padding(horizontal = 14.dp, vertical = 16.dp),
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "e.g. Morning Burn",
                        style = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W400, fontSize = 16.sp),
                        color = MaterialTheme.extendedColors.textMuted,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Composable
private fun ExerciseChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .border(1.dp, MaterialTheme.extendedColors.hairline)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Text(text = "+ $label", style = IOMotionTextStyles.segmentedLabel, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun RoutineRow(
    item: DraftItem,
    onRemove: () -> Unit,
    onStepSets: (Boolean) -> Unit,
    onStepReps: (Boolean) -> Unit,
) {
    val isPlank = item.exerciseType == ExerciseType.PLANK
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.extendedColors.hairline)
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.exerciseType.displayName(),
                style = IOMotionTextStyles.cardTitle,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Remove ${item.exerciseType.displayName()}",
                    tint = MaterialTheme.extendedColors.textMuted,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stepper(
                value = item.sets,
                unitLabel = "sets",
                onDecrement = { onStepSets(false) },
                onIncrement = { onStepSets(true) },
                modifier = Modifier.weight(1f),
            )
            Stepper(
                value = item.reps,
                unitLabel = if (isPlank) "sec" else "reps",
                onDecrement = { onStepReps(false) },
                onIncrement = { onStepReps(true) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun Stepper(
    value: Int,
    unitLabel: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.border(1.dp, MaterialTheme.extendedColors.hairline),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepButton(symbol = "–", onClick = onDecrement)
        Text(
            text = "$value $unitLabel",
            style = IOMotionTextStyles.segmentedLabel,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        StepButton(symbol = "+", onClick = onIncrement)
    }
}

@Composable
private fun StepButton(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = symbol, style = IOMotionTextStyles.exerciseRowName, color = MaterialTheme.colorScheme.primary)
    }
}
