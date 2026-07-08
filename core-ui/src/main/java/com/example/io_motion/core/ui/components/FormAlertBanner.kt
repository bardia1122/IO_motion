package com.example.io_motion.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.io_motion.core.ui.theme.StatusPoor

/**
 * Displays real-time form alerts as a translucent "poor form" banner that animates in/out; the
 * fill is the shared Poor status color so the camera overlay matches the report screens.
 * Alert strings are caller-supplied so this component has no core-analysis dependency.
 */
@Composable
fun FormAlertBanner(
    alerts: List<String>,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = alerts.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(StatusPoor.copy(alpha = 0.85f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            alerts.forEach { alert ->
                Text(
                    text = alert,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
