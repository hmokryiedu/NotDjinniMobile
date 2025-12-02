package not.djinni.presentation.screens.seeker.profile.view

import androidx.compose.runtime.Immutable
import not.djinni.model.User
import not.djinni.model.seeker.SeekerProfile

@Immutable
internal data class SeekerProfileState(
    val user: User? = null,
    val profile: SeekerProfile? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
)
