package not.djinni.presentation.screens.seeker.vacancy.details.coverletter

import not.djinni.presentation.navigation.NavResultContract

object CoverLetterResultContract : NavResultContract<String> {
    override fun encode(value: String): String = value
    override fun decode(value: String): String = value
}
