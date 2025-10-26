package not.djinni.presentation.screens.onboarding.mapper

import not.djinni.R
import not.djinni.model.EmploymentType
import not.djinni.presentation.core.components.base.model.TextData

fun EmploymentType.toTextData(): TextData = TextData.Resource(
    when (this) {
        EmploymentType.FULL_TIME -> R.string.employment_type_full_time
        EmploymentType.PART_TIME -> R.string.employment_type_part_time
        EmploymentType.CONTRACT -> R.string.employment_type_contract
    }
)