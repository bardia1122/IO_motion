package com.example.io_motion.data.model

import com.example.io_motion.core.analysis.model.SessionMetrics
import com.example.io_motion.core.common.models.AnalysisMode

/** Public view of a persisted session, combining storage metadata with computed metrics. */
data class SessionRecord(
    val id: Long,
    val recordedAt: Long,
    val analysisMode: AnalysisMode,
    val modelVariant: String,
    val metrics: SessionMetrics,
) {
    /**
     * Session quality score (0–100), surfaced directly so consumers that only need the number
     * (e.g. the Home hub stats) don't have to depend on `:core-analysis` for the [SessionMetrics]
     * type.
     */
    val qualityScore: Int get() = metrics.sessionQualityScore
}
