# Vacancy Views Count

## Overview
Add a new vacancy views count feature to the `Vacancy Details` screen.

The screen should show an eye icon with the number of views for the current vacancy.

## Requested Behavior
- Add an eye icon with the vacancy views count to `Vacancy Details`.
- Place the views count at the bottom of the vacancy text content.
- If the vacancy has a lot of text, the user should need to scroll to the bottom of the text to see the views count.

## Scope
- Implement only the vacancy views count display in `Vacancy Details` for now.
- Do not implement a separate viewed vacancies list in this task.
- Do not add a viewed vacancies button to the top bar in this task.

## Backend Contract
No separate `Viewed Vacancies` backend contract will be provided for this task.

The only backend/model change is in the vacancy details response. The response includes the existing vacancy details fields:

```json
{
  "id": 1,
  "company": {},
  "title": "Vacancy title",
  "description": "Vacancy description",
  "salary_min": 1000,
  "salary_max": 2000,
  "min_experience_years": 1,
  "employment_type": "Full-time",
  "category": "Category",
  "status": "Status",
  "created_at": "2026-05-17T00:00:00Z",
  "updated_at": "2026-05-17T00:00:00Z",
  "applications_count": 0,
  "is_favorite": false
}
```

Source model:

```kotlin
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class VacancyDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("company")
    val company: CompanyResponse,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("salary_min")
    val salaryMin: Int,
    @SerialName("salary_max")
    val salaryMax: Int,
    @SerialName("min_experience_years")
    val minExperienceYears: Int?,
    @SerialName("employment_type")
    val employmentType: String?,
    @SerialName("category")
    val category: String?,
    @SerialName("status")
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("applications_count")
    val applicationsCount: Int,
    @EncodeDefault
    @SerialName("is_favorite")
    val isFavorite: Boolean = false,
)
```

## Acceptance Criteria
- `Vacancy Details` shows an eye icon with the current vacancy views count.
- The views count is displayed below the vacancy text content.
- The views count is not placed under the top bar or in the top-right area.
- A separate viewed vacancies list is not implemented as part of this task.
