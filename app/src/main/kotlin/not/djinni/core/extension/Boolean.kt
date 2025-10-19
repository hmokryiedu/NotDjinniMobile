package not.djinni.core.extension

fun Boolean?.orFalse(): Boolean {
    return this == true
}