package not.djinni.core.extension

import android.annotation.SuppressLint

fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0
fun Float?.orZero(): Float = this ?: 0f
fun Double?.orZero(): Double = this ?: 0.0

@SuppressLint("DefaultLocale")
fun Long.formatTime(): String {
    val seconds = this / MILLISECONDS_PER_SECOND
    return String.format(
        "%02d:%02d:%02d",
        seconds / SECONDS_PER_HOUR,
        seconds / SECONDS_PER_MINUTE % MINUTES_PER_HOUR,
        seconds % SECONDS_PER_MINUTE
    )
}

private const val SECONDS_PER_HOUR = 3600
private const val SECONDS_PER_MINUTE = 60
private const val MINUTES_PER_HOUR = 60
private const val MILLISECONDS_PER_SECOND = 1000