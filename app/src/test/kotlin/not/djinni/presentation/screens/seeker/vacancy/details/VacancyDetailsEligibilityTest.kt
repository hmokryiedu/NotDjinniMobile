@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.vacancy.details

import not.djinni.model.company.Company
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.VacancyStatusCode
import not.djinni.presentation.core.components.base.model.TextData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class VacancyDetailsEligibilityTest {

    @Test
    fun isApplyAvailable_returns_false_when_eligibility_missing() {
        assertFalse((null as EligibilityState?).isApplyAvailable())
    }

    @Test
    fun calculateEligibility_allows_apply_when_experience_is_enough() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 3, salaryMax = 6000),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" }
        )

        assertTrue(eligibility.canApply)
        assertTrue(eligibility.experienceMatch)
    }

    @Test
    fun calculateEligibility_blocks_apply_when_experience_is_insufficient() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 5, salaryMax = 6000),
            profile = profile(experienceYears = 2, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" }
        )

        assertFalse(eligibility.canApply)
        assertFalse(eligibility.experienceMatch)
    }

    @Test
    fun calculateEligibility_does_not_block_apply_on_salary_mismatch_and_shows_hint() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 3, salaryMax = 4000),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" }
        )

        assertTrue(eligibility.canApply)
        assertFalse(eligibility.salaryMatch)
        assertNotNull(eligibility.salaryHint)
        assertEquals(
            "Salary is below your expectations ($5000)",
            (eligibility.salaryHint as TextData.Text).value
        )
    }

    @Test
    fun calculateEligibility_hides_salary_hint_when_salary_matches() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 3, salaryMax = 6000),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" }
        )

        assertTrue(eligibility.salaryMatch)
        assertNull(eligibility.salaryHint)
    }

    private fun vacancy(minExperienceYears: Int?, salaryMax: Int): Vacancy = Vacancy(
        id = 1L,
        company = Company(
            id = 10L,
            name = "Company",
            website = null,
            description = "Description"
        ),
        title = "Android Developer",
        description = "Details",
        viewsCount = 10,
        applicationsCount = 2,
        salaryMin = 1000,
        salaryMax = salaryMax,
        minExperienceYears = minExperienceYears,
        employmentType = EmploymentType.FULL_TIME,
        category = JobCategoryCode.SOFTWARE_DEV,
        status = VacancyStatusCode.ACTIVE,
        isFavorite = false,
        createdAt = Instant.parse("2026-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2026-01-01T00:00:00Z")
    )

    private fun profile(experienceYears: Int, desiredSalary: Int): SeekerProfile = SeekerProfile(
        id = 7L,
        aboutMe = null,
        speciality = "Android",
        desiredSalary = desiredSalary,
        experienceYears = experienceYears,
        jobCategory = JobCategoryCode.SOFTWARE_DEV,
        workExperience = emptyList()
    )
}
