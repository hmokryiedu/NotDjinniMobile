@file:OptIn(ExperimentalTime::class)

package not.djinni.core.extension

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun Instant.toFormatterMonthYearDate(): String {
    val date = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${date.month.number}/${date.year}"
}

fun Instant.toFormattedFullDate(): String {
    val date = toLocalDateTime(TimeZone.currentSystemDefault())
    val monthName = date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    return "$monthName ${date.dayOfMonth}, ${date.year}"
}

fun Long.toInstant(): Instant {
    return Instant.fromEpochMilliseconds(this)
}
