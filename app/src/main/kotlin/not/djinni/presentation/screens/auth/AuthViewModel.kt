package not.djinni.presentation.screens.auth

import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AuthViewModel : StateViewModel<AuthState>(AuthState())
