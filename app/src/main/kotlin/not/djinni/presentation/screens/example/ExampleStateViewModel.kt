package not.djinni.presentation.screens.example

import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExampleStateViewModel : StateViewModel<ExampleState>(ExampleState())