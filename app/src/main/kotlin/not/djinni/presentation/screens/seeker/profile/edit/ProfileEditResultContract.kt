package not.djinni.presentation.screens.seeker.profile.edit

import not.djinni.presentation.navigation.NavResultContract

object ProfileEditResultContract : NavResultContract<Boolean> {
    override fun encode(value: Boolean): String = value.toString()
    override fun decode(value: String): Boolean = value.toBoolean()
}
