@file:OptIn(ExperimentalTime::class)

package not.djinni.model.seeker.vacancy

import not.djinni.model.company.Company
import not.djinni.network.vacancy.response.EmploymentTypeResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse
import kotlin.time.ExperimentalTime

fun VacancyDetailsResponse.toDomain(): Vacancy = Vacancy(
    id = id,
    company = Company(
        id = company.id,
        name = company.name,
        website = company.website,
        description = company.description
    ),
    title = title,
    description = description,
    viewsCount = viewsCount,
    applicationsCount = applicationsCount,
    salaryMin = salaryMin,
    salaryMax = salaryMax,
    minExperienceYears = minExperienceYears,
    employmentType = employmentType?.toDomain() ?: EmploymentType.FULL_TIME,
    category = category?.toDomain(),
    status = status.toDomain(),
    isFavorite = isFavorite,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun EmploymentTypeResponse.toDomain(): EmploymentType = when (this) {
    EmploymentTypeResponse.FULL_TIME -> EmploymentType.FULL_TIME
    EmploymentTypeResponse.PART_TIME -> EmploymentType.PART_TIME
    EmploymentTypeResponse.CONTRACT -> EmploymentType.CONTRACT
    EmploymentTypeResponse.TEMPORARY -> EmploymentType.TEMPORARY
    EmploymentTypeResponse.INTERNSHIP -> EmploymentType.INTERNSHIP
    EmploymentTypeResponse.FREELANCE -> EmploymentType.FREELANCE
}

fun JobCategoryCodeResponse.toDomain(): JobCategoryCode {
    return when (this) {
        JobCategoryCodeResponse.SOFTWARE_DEV -> JobCategoryCode.SOFTWARE_DEV
        JobCategoryCodeResponse.DATA_SCIENCE -> JobCategoryCode.DATA_SCIENCE
        JobCategoryCodeResponse.DEVOPS -> JobCategoryCode.DEVOPS
        JobCategoryCodeResponse.QA -> JobCategoryCode.QA
        JobCategoryCodeResponse.PRODUCT_MGMT -> JobCategoryCode.PRODUCT_MGMT
        JobCategoryCodeResponse.DESIGN -> JobCategoryCode.DESIGN
        JobCategoryCodeResponse.MARKETING -> JobCategoryCode.MARKETING
        JobCategoryCodeResponse.SALES -> JobCategoryCode.SALES
        JobCategoryCodeResponse.HR -> JobCategoryCode.HR
        JobCategoryCodeResponse.FINANCE -> JobCategoryCode.FINANCE
        JobCategoryCodeResponse.OPERATIONS -> JobCategoryCode.OPERATIONS
        JobCategoryCodeResponse.SUPPORT -> JobCategoryCode.SUPPORT
    }
}

fun VacancyStatusCodeResponse.toDomain(): VacancyStatusCode = when (this) {
    VacancyStatusCodeResponse.DRAFT -> VacancyStatusCode.DRAFT
    VacancyStatusCodeResponse.ACTIVE -> VacancyStatusCode.ACTIVE
    VacancyStatusCodeResponse.PAUSED -> VacancyStatusCode.PAUSED
    VacancyStatusCodeResponse.CLOSED -> VacancyStatusCode.CLOSED
    VacancyStatusCodeResponse.EXPIRED -> VacancyStatusCode.EXPIRED
}

fun JobCategoryCode.toResponse(): JobCategoryCodeResponse = when (this) {
    JobCategoryCode.SOFTWARE_DEV -> JobCategoryCodeResponse.SOFTWARE_DEV
    JobCategoryCode.DATA_SCIENCE -> JobCategoryCodeResponse.DATA_SCIENCE
    JobCategoryCode.DEVOPS -> JobCategoryCodeResponse.DEVOPS
    JobCategoryCode.QA -> JobCategoryCodeResponse.QA
    JobCategoryCode.PRODUCT_MGMT -> JobCategoryCodeResponse.PRODUCT_MGMT
    JobCategoryCode.DESIGN -> JobCategoryCodeResponse.DESIGN
    JobCategoryCode.MARKETING -> JobCategoryCodeResponse.MARKETING
    JobCategoryCode.SALES -> JobCategoryCodeResponse.SALES
    JobCategoryCode.HR -> JobCategoryCodeResponse.HR
    JobCategoryCode.FINANCE -> JobCategoryCodeResponse.FINANCE
    JobCategoryCode.OPERATIONS -> JobCategoryCodeResponse.OPERATIONS
    JobCategoryCode.SUPPORT -> JobCategoryCodeResponse.SUPPORT
}

fun EmploymentType.toResponse(): EmploymentTypeResponse = when (this) {
    EmploymentType.FULL_TIME -> EmploymentTypeResponse.FULL_TIME
    EmploymentType.PART_TIME -> EmploymentTypeResponse.PART_TIME
    EmploymentType.CONTRACT -> EmploymentTypeResponse.CONTRACT
    EmploymentType.TEMPORARY -> EmploymentTypeResponse.TEMPORARY
    EmploymentType.INTERNSHIP -> EmploymentTypeResponse.INTERNSHIP
    EmploymentType.FREELANCE -> EmploymentTypeResponse.FREELANCE
}
