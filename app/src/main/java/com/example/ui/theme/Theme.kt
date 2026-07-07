package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.Calendar

enum class JobSector(val displayName: String) {
    CIVIL("Civil Services"),
    ARMY("Indian Army"),
    NAVY("Indian Navy"),
    POLICE("Police Force")
}

object DailyTheme {
    val dayOfWeek: Int
        get() = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    val accentColor: Color
        get() = when (dayOfWeek) {
            Calendar.MONDAY -> Color(0xFFD97706) // Warm Amber
            Calendar.TUESDAY -> Color(0xFF0D9488) // Fresh Teal
            Calendar.WEDNESDAY -> Color(0xFF2563EB) // Royal Blue
            Calendar.THURSDAY -> Color(0xFF7C3AED) // Amethyst Purple
            Calendar.FRIDAY -> Color(0xFFDB2777) // Rose Petal
            Calendar.SATURDAY -> Color(0xFF059669) // Deep Emerald
            else -> Color(0xFFEA580C) // Warm Terracotta (Sunday)
        }

    val themeName: String
        get() = when (dayOfWeek) {
            Calendar.MONDAY -> "Warm Amber"
            Calendar.TUESDAY -> "Fresh Teal"
            Calendar.WEDNESDAY -> "Royal Blue"
            Calendar.THURSDAY -> "Amethyst Purple"
            Calendar.FRIDAY -> "Rose Petal"
            Calendar.SATURDAY -> "Deep Emerald"
            else -> "Warm Terracotta"
        }

    val Background = Color(0xFFF8FAFC) // Soft natural off-white
    val CardBackground = Color(0xFFFFFFFF) // Crisp white
    val CardBorder = Color(0xFFE2E8F0) // Subtle Slate Border

    val TextPrimary = Color(0xFF0F172A) // Charcoal
    val TextSecondary = Color(0xFF475569) // Muted Slate
    val TextMuted = Color(0xFF94A3B8) // Light Gray
}

@Composable
fun MyApplicationTheme(
    activeSector: JobSector = JobSector.CIVIL,
    content: @Composable () -> Unit
) {
    val dailyAccent = DailyTheme.accentColor
    
    val colorScheme = lightColorScheme(
        primary = dailyAccent,
        secondary = DailyTheme.TextSecondary,
        tertiary = DailyTheme.TextMuted,
        background = DailyTheme.Background,
        surface = DailyTheme.CardBackground,
        onPrimary = Color.White,
        onSecondary = DailyTheme.TextPrimary,
        onBackground = DailyTheme.TextPrimary,
        onSurface = DailyTheme.TextPrimary,
        error = ErrorRed
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
