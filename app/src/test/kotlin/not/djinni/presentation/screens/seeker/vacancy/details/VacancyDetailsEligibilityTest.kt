@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.vacancy.details

import not.djinni.model.company.Company
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.VacancyStatusCode
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toTextData
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
    fun calculateEligibility_blocks_apply_on_category_mismatch() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(category = JobCategoryCode.PRODUCT_MGMT),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertFalse(eligibility.canApply)
        assertEquals(
            "Your job category does not match this vacancy",
            eligibility.blockers.single().asText()
        )
    }

    @Test
    fun calculateEligibility_allows_apply_on_category_match_when_other_rules_pass() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(category = JobCategoryCode.SOFTWARE_DEV),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertTrue(eligibility.canApply)
        assertTrue(eligibility.blockers.isEmpty())
    }

    @Test
    fun calculateEligibility_warns_only_when_vacancy_category_missing() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(category = null),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertTrue(eligibility.canApply)
        assertEquals("Vacancy category is missing", eligibility.warnings.single().asText())
    }

    @Test
    fun calculateEligibility_blocks_apply_when_vacancy_not_active() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(status = VacancyStatusCode.CLOSED),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertFalse(eligibility.canApply)
        assertEquals("Vacancy is not active", eligibility.blockers.single().asText())
    }

    @Test
    fun calculateEligibility_allows_apply_when_experience_equals_minimum() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 3),
            profile = profile(experienceYears = 3, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertTrue(eligibility.canApply)
        assertTrue(eligibility.blockers.isEmpty())
    }

    @Test
    fun calculateEligibility_blocks_apply_when_experience_below_minimum() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 5),
            profile = profile(experienceYears = 2, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertFalse(eligibility.canApply)
        assertEquals("At least 5 years experience required", eligibility.blockers.single().asText())
    }

    @Test
    fun calculateEligibility_allows_apply_when_min_experience_is_null_or_zero() {
        val withNull = calculateEligibility(
            vacancy = vacancy(minExperienceYears = null),
            profile = profile(experienceYears = 0, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )
        val withZero = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 0),
            profile = profile(experienceYears = 0, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertTrue(withNull.canApply)
        assertTrue(withZero.canApply)
    }

    @Test
    fun calculateEligibility_does_not_block_apply_on_salary_mismatch_and_shows_warning() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 3, salaryMax = 4000),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertTrue(eligibility.canApply)
        assertTrue(eligibility.blockers.isEmpty())
        assertNotNull(eligibility.salaryHint)
        assertEquals(1, eligibility.warnings.size)
        assertEquals(
            "Salary is below your expectations ($5000)",
            eligibility.warnings.single().asText()
        )
    }

    @Test
    fun calculateEligibility_hides_salary_hint_when_salary_matches() {
        val eligibility = calculateEligibility(
            vacancy = vacancy(minExperienceYears = 3, salaryMax = 6000),
            profile = profile(experienceYears = 4, desiredSalary = 5000),
            salaryHintProvider = { desiredSalary -> "Salary is below your expectations (\$$desiredSalary)" },
            missingCategoryWarningProvider = { "Vacancy category is missing" },
            categoryMismatchBlockerProvider = { "Your job category does not match this vacancy" },
            inactiveVacancyBlockerProvider = { "Vacancy is not active" },
            insufficientExperienceBlockerProvider = { minYears -> "At least $minYears years experience required" }
        )

        assertNull(eligibility.salaryHint)
        assertTrue(eligibility.warnings.isEmpty())
    }

    @Test
    fun isApplyAvailable_returns_false_when_blockers_exist() {
        val eligibility = EligibilityState(
            canApply = false,
            blockers = listOf("blocked".toTextData()),
            warnings = emptyList(),
            salaryHint = null
        )

        assertFalse(eligibility.isApplyAvailable())
    }

    private fun vacancy(
        minExperienceYears: Int? = 3,
        salaryMax: Int = 6000,
        category: JobCategoryCode? = JobCategoryCode.SOFTWARE_DEV,
        status: VacancyStatusCode = VacancyStatusCode.ACTIVE,
    ): Vacancy = Vacancy(
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
        category = category,
        status = status,
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

    private fun TextData.asText(): String = (this as TextData.Text).value
}
