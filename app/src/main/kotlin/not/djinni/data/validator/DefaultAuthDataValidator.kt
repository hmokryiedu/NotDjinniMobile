package not.djinni.data.validator

import android.util.Patterns
import not.djinni.domain.validator.AuthDataValidator
import org.koin.core.annotation.Single

@Single(binds = [AuthDataValidator::class])
class DefaultAuthDataValidator : AuthDataValidator {

    override fun validateName(name: String): Boolean {
        return NAME_REGEX.toRegex().matches(name)
    }

    override fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun validatePassword(password: String): Boolean {
        return PASSWORD_REGEX.toRegex().matches(password)
    }

    private companion object {
        const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{6,}\$"
        const val NAME_REGEX = "^.{4,}$"
    }
}