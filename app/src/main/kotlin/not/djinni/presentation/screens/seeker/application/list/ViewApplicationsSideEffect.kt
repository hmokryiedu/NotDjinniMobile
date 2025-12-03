package not.djinni.presentation.screens.seeker.application.list

internal sealed interface ViewApplicationsSideEffect {
    data class NavigateToDetails(val id: Long) : ViewApplicationsSideEffect
}
