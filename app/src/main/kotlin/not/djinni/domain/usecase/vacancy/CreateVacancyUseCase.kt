package not.djinni.domain.usecase.vacancy

import not.djinni.domain.repository.VacancyRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy
import org.koin.core.annotation.Factory

@Factory
class CreateVacancyUseCase(
    private val vacancyRepository: VacancyRepository,
) : UseCaseWithParams<Result<Vacancy>, CreateVacancyUseCase.Params> {

    override suspend fun invoke(params: Params): Result<Vacancy> = runCatching {
        require(params.title.isNotBlank()) { "Title cannot be empty" }
        require(params.description.isNotBlank()) { "Description cannot be empty" }
        require(params.salaryMin >= 0) { "Minimum salary must be non-negative" }
        require(params.salaryMax > 0) { "Maximum salary must be positive" }
        require(params.salaryMin < params.salaryMax) { "Minimum salary must be less than maximum salary" }
        require(params.experienceYears >= 0) { "Experience years must be non-negative" }

        vacancyRepository.createVacancy(
            title = params.title,
            description = params.description,
            salaryMin = params.salaryMin,
            salaryMax = params.salaryMax,
            experienceYears = params.experienceYears,
            employmentType = params.employmentType,
            category = params.category
        ).getOrThrow()
    }

    data class Params(
        val title: String,
        val description: String,
        val salaryMin: Int,
        val salaryMax: Int,
        val experienceYears: Int,
        val employmentType: EmploymentType,
        val category: JobCategoryCode,
    )
}
