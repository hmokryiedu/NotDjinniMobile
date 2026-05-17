# Profile Editing

## Overview
Добавляем функцию редактирования профилей для двух типов пользователей:

- `employer`
- `seeker`

## User Goal
Пользователь должен иметь возможность обновлять доступные для своего типа профиля данные.

## Requested Behavior

### Seeker Profile
Seeker может редактировать почти все поля профиля:

- speciality
- experience_years
- desired_salary
- about_me
- job_category

Seeker также может управлять work experience:

- создать новый work experience
- редактировать существующий work experience
- удалить существующий work experience

### Employer Profile
Employer может редактировать только роль.

Компания остается неизменной:

- компания выбирается один раз
- после выбора компанию больше нельзя изменить

## Backend Contract

### Update Seeker Profile

```http
PUT /seeker/profile
```

Auth required.

Updates seeker fields:

- speciality
- experience_years
- desired_salary
- about_me
- job_category

### Update Seeker Work Experience

```http
PUT /seeker/profile/experience/{id}
```

Auth required.

Updates one seeker work experience.

### Update Employer Profile

```http
PUT /employer/profile
```

Auth required.

Updates employer profile role only.

## Acceptance Criteria

- Seeker can update profile fields listed in the backend contract.
- Seeker can edit existing work experience.
- Seeker can create new work experience.
- Seeker can delete existing work experience.
- Employer can update role.
- Employer cannot change company after it has been selected.
- Employer profile edit must not expose company editing.

## Open Questions

- Backend endpoint for creating seeker work experience was not provided.
- Backend endpoint for deleting seeker work experience was not provided.
