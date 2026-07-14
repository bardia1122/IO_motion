package com.example.io_motion.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.io_motion.core.common.stats.Greeting
import com.example.io_motion.core.common.stats.HomeStats
import com.example.io_motion.core.ui.components.SectionLabel
import com.example.io_motion.core.ui.theme.IOMotionTextStyles
import com.example.io_motion.core.ui.theme.extendedColors
import com.example.io_motion.core.ui.theme.scoreColor

@Composable
fun HomeHubScreen(
    onOpenAssessment: () -> Unit,
    onOpenWorkouts: () -> Unit,
    onOpenDiet: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeHubViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 36.dp),
    ) {
        HubTopRow(onOpenSettings = onOpenSettings)

        Spacer(Modifier.height(28.dp))

        Text(text = uiState.greeting.displayText(), style = IOMotionTextStyles.greeting, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(4.dp))
        Text(
            text = uiState.dateLine,
            style = IOMotionTextStyles.metaModeVariant,
            color = MaterialTheme.extendedColors.textMuted,
        )

        Spacer(Modifier.height(28.dp))

        SectionLabel(text = "THIS WEEK")
        Spacer(Modifier.height(14.dp))
        StatGrid(stats = uiState.stats)

        Spacer(Modifier.height(28.dp))

        SectionLabel(text = "FEATURES")
        Spacer(Modifier.height(14.dp))
        FeatureRows(
            onOpenAssessment = onOpenAssessment,
            onOpenWorkouts = onOpenWorkouts,
            onOpenDiet = onOpenDiet,
        )
    }
}

private fun Greeting.displayText(): String = when (this) {
    Greeting.MORNING -> "Good morning"
    Greeting.AFTERNOON -> "Good afternoon"
    Greeting.EVENING -> "Good evening"
}

@Composable
private fun HubTopRow(onOpenSettings: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row {
            Text(text = "IO", style = IOMotionTextStyles.wordmark, color = MaterialTheme.colorScheme.primary)
            Text(text = "Motion", style = IOMotionTextStyles.wordmark, color = MaterialTheme.colorScheme.onBackground)
        }
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.extendedColors.hairline, RoundedCornerShape(12.dp))
                .clickable(onClick = onOpenSettings),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

/** 3-cell bordered stat grid: 1dp outer border + vertical dividers between cells. */
@Composable
private fun StatGrid(stats: HomeStats) {
    val extended = MaterialTheme.extendedColors
    val hairline = extended.hairline
    val onBackground = MaterialTheme.colorScheme.onBackground
    val lastQualityColor = stats.lastQuality?.let { extended.scoreColor(it) } ?: extended.textMuted
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .border(1.dp, hairline),
    ) {
        StatCell(
            modifier = Modifier.weight(1f),
            value = stats.lastQuality?.toString() ?: "—",
            caption = "LAST QUALITY",
            valueColor = lastQualityColor,
        )
        Box(Modifier.width(1.dp).fillMaxHeight().background(hairline))
        StatCell(
            modifier = Modifier.weight(1f),
            value = stats.sessionsThisWeek.toString(),
            caption = "SESSIONS",
            valueColor = onBackground,
        )
        Box(Modifier.width(1.dp).fillMaxHeight().background(hairline))
        StatCell(
            modifier = Modifier.weight(1f),
            value = stats.dayStreak.toString(),
            caption = "DAY STREAK",
            valueColor = onBackground,
        )
    }
}

@Composable
private fun StatCell(
    value: String,
    caption: String,
    valueColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight().padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = value, style = IOMotionTextStyles.bigNumber, color = valueColor)
        Spacer(Modifier.height(6.dp))
        Text(text = caption, style = IOMotionTextStyles.statLabel, color = MaterialTheme.extendedColors.textMuted)
    }
}

@Composable
private fun FeatureRows(
    onOpenAssessment: () -> Unit,
    onOpenWorkouts: () -> Unit,
    onOpenDiet: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FeatureRow(
            icon = { MotionAssessmentIcon() },
            title = "Motion Assessment",
            description = "Live camera or video rep counting & form scoring",
            onClick = onOpenAssessment,
            showDivider = true,
        )
        FeatureRow(
            icon = { CreateWorkoutIcon() },
            title = "Create Your Workout",
            description = "Build custom routines from your 4 tracked exercises",
            onClick = onOpenWorkouts,
            showDivider = true,
        )
        FeatureRow(
            icon = { DietPlanningIcon() },
            title = "Diet Planning",
            description = "Log meals, track macros & hit your daily targets",
            onClick = onOpenDiet,
            showDivider = false,
        )
    }
}

@Composable
private fun FeatureRow(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit,
    showDivider: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = IOMotionTextStyles.sessionRowName, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(3.dp))
                Text(
                    text = description,
                    style = IOMotionTextStyles.repMeta,
                    color = MaterialTheme.extendedColors.textMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(12.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.extendedColors.textMuted,
            )
        }
        if (showDivider) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.extendedColors.hairline))
        }
    }
}
