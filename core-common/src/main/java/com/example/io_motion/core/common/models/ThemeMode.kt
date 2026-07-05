package com.example.io_motion.core.common.models

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
}

fun ThemeMode.displayName(): String = when (this) {
    ThemeMode.LIGHT  -> "Light"
    ThemeMode.DARK   -> "Dark"
    ThemeMode.SYSTEM -> "Auto"
}

/** Resolves whether dark colors should be used, given the current system setting. */
fun ThemeMode.isDark(systemDark: Boolean): Boolean = when (this) {
    ThemeMode.LIGHT  -> false
    ThemeMode.DARK   -> true
    ThemeMode.SYSTEM -> systemDark
}
