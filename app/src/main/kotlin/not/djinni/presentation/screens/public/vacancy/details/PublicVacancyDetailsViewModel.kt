@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.public.vacancy.details

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.ExperimentalTime

@KoinViewModel
internal class PublicVacancyDetailsViewModel(
    @InjectedParam private val vacancyId: Long,
    private val stringProvider: StringProvider,
    private val vacancyRepository: VacancyRepository,
) : StateViewModel<PublicVacancyDetailsState>(PublicVacancyDetailsState()) {

    private val _sideEffect = mutableSideEffect<PublicVacancyDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadVacancyDetails()
    }

    fun sendAction(action: PublicVacancyDetailsAction) {
        when (action) {
            PublicVacancyDetailsAction.Load -> loadVacancyDetails()
            PublicVacancyDetailsAction.NavigateBack -> {
                _sideEffect.tryEmit(PublicVacancyDetailsSideEffect.NavigateBack)
            }
        }
    }

    private fun loadVacancyDetails() {
        launch {
            updateState { copy(contentState = PublicVacancyDetailsContentState.Loading) }
            vacancyRepository.getPublicVacancyById(vacancyId)
                .onSuccess { vacancy ->
                    updateState {
                        copy(
                            contentState = PublicVacancyDetailsContentState.Data(
                                vacancy = vacancy.toDisplayData()
                            )
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            contentState = PublicVacancyDetailsContentState.Error(
                                R.string.vacancy_details_error.toTextData()
                            )
                        )
                    }
                }
        }
    }

    private fun Vacancy.toDisplayData(): PublicVacancyDisplayData {
        return PublicVacancyDisplayData(
            id = id,
            title = title.toTextData(),
            companyName = company.name.toTextData(),
            companyDescription = company.description.toTextData(),
            description = description.toTextData(),
            salaryRange = stringProvider.getString(
                R.string.vacancy_salary_range,
                salaryMin,
                salaryMax
            ).toTextData(),
            employmentType = employmentType.toDisplayName(),
            requiredExperience = formatExperience(minExperienceYears),
            category = category?.toDisplayName(),
            postedDate = createdAt.toFormattedFullDate().toTextData()
        )
    }

    private fun formatExperience(years: Int?): TextData {
        return if (years == null || years == 0) {
            R.string.vacancy_no_experience_required.toTextData()
        } else {
            stringProvider.getString(R.string.vacancy_years_experience, years).toTextData()
        }
    }
}
