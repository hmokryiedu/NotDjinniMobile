package not.djinni.presentation.screens.main

import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel : StateViewModel<MainState>(MainState())
