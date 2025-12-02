package not.djinni.presentation.core.extension

import not.djinni.R
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.presentation.core.components.base.model.TextData

fun EmploymentType.toDisplayName(): TextData = when (this) {
    EmploymentType.FULL_TIME -> R.string.employment_type_full_time.toTextData()
    EmploymentType.PART_TIME -> R.string.employment_type_part_time.toTextData()
    EmploymentType.CONTRACT -> R.string.employment_type_contract.toTextData()
    EmploymentType.TEMPORARY -> R.string.employment_type_temporary.toTextData()
    EmploymentType.INTERNSHIP -> R.string.employment_type_internship.toTextData()
    EmploymentType.FREELANCE -> R.string.employment_type_freelance.toTextData()
}
