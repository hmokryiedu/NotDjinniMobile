# Application Actions Improvements

## Overview
Нужно добавить и исправить несколько функций, связанных с applied vacancies, applications, cover letter templates и logout.

Каждый пункт ниже описан отдельно в одинаковом формате.

## Applied Vacancies Status Filter

### Problem
В applied vacancies не хватает фильтрации по статусу.

### Requested Behavior
- Добавить фильтрацию applied vacancies по нескольким статусам.
- Прокинуть фильтр в backend query как `application_status: ApplicationStatus[]?`.
- Разместить кнопку фильтра над списком вакансий.
- Попап фильтра должен работать через draft-выбор: не перезагружать список при каждом toggle.
- При закрытии попапа (в том числе tap outside) сравнивать draft/selected и делать один reload только если фильтр изменился.
- Под кнопкой показывать выбранные статусы как `FlowRow` chip-ы (label + close icon).
- Цвет chip зависит от статуса (accent background/text).
- Тап по chip удаляет статус из фильтра и делает один reload.

### Acceptance Criteria
- `Vacancy.Applied` принимает `application_status`.
- `VacancyDataSource` / `VacancyRepository` принимают список статусов.
- В UI есть кнопка фильтра + popup multi-select.
- Пока popup открыт, список не reload.
- При закрытии popup, reload только один раз при изменении набора.
- Выбранные фильтры отображаются chip-ами в `FlowRow`.
- Удаление фильтра через chip работает и reload делает один раз.

## Application Withdraw Confirmation

### Problem
Для withdraw application отсутствует попап подтверждения.

### Requested Behavior
- Добавить попап подтверждения для действия withdraw application.
- Withdraw должен выполняться только после подтверждения пользователя.

### Acceptance Criteria
- При попытке withdraw application пользователь видит confirmation popup.
- Пользователь может подтвердить действие.
- Пользователь может отменить действие.
- Application withdraw не выполняется без подтверждения.

## Cover Letter Templates Bug

### Problem
Есть баг с cover letter templates.

### Reference
- https://glebmokryy.atlassian.net/browse/NOT-52?atlOrigin=eyJpIjoiMTkwYzllMWYwMDUyNGQ2OGE5MThmMWUxODE0ZDJhOGEiLCJwIjoiaiJ9

### Requested Behavior
- Исправить баг с cover letter templates согласно задаче `NOT-52`.

### Acceptance Criteria
- Поведение cover letter templates соответствует ожидаемому результату из `NOT-52`.

## Logout Confirmation

### Problem
На logout отсутствует попап подтверждения.

### Requested Behavior
- Добавить попап подтверждения для logout.
- Logout должен выполняться только после подтверждения пользователя.

### Acceptance Criteria
- При попытке logout пользователь видит confirmation popup.
- Пользователь может подтвердить logout.
- Пользователь может отменить logout.
- Logout не выполняется без подтверждения.

## NOT-54 Reference

### Problem
Описание пункта не предоставлено в тексте. Дана только ссылка на задачу `NOT-54`.

### Reference
- https://glebmokryy.atlassian.net/browse/NOT-54?atlOrigin=eyJpIjoiNzQwMmZkY2ExYjJjNDZmOWEyZTdlNjI2MzQxMzNhZjciLCJwIjoiaiJ9

### Requested Behavior
- Учесть задачу `NOT-54`.

### Acceptance Criteria
- Поведение соответствует ожидаемому результату из `NOT-54`.
