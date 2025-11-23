package not.djinni.presentation.screens.auth.extension

private val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$".toRegex()
private val emailRegex =
    "[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+".toRegex()

internal fun CharSequence.isPasswordValid(): Boolean {
    return passwordRegex.matches(this)
}

internal fun CharSequence.isEmailValid(): Boolean {
    return emailRegex.matches(this)
}

