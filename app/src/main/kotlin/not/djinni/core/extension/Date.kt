package not.djinni.core.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration

fun LocalDateTime.toDisplayFormat(): String {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm")
    return this.format(formatter)
}

fun Duration.formatDuration(): String {
    val hours = inWholeHours
    val minutes = inWholeMinutes % 60
    val seconds = inWholeSeconds % 60

    return when {
        hours != 0L -> String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}