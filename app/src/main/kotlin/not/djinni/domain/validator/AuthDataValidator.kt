package not.djinni.domain.validator

interface AuthDataValidator {
    fun validateName(name: String): Boolean
    fun validateEmail(email: String): Boolean
    fun validatePassword(password: String): Boolean
}