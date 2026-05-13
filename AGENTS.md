# Project Agent Rules

## Role Mapping

Project-level `AGENTS.md` имеет приоритет над global role mapping.

| Role | Agent | Required rules |
| --- | --- | --- |
| `architect` | [@android_architect](subagent://android_architect) | `ai/rules/android-architecture-rules.md` (present) |
| `ui-designer` | [@android_designer](subagent://android_designer) | `ai/rules/android-ui-rules.md` (present) |
| `executor` | [@android_implementor](subagent://android_implementor) | `ai/rules/android-implementation-rules.md` (present) |
| `tester` | [@android_testing](subagent://android_testing) | `ai/rules/android-testing-rules.md` (present) |
| `plan-coordinator` | [@plan_coordinator](subagent://plan_coordinator) | `ai/rules/plan-coordinator-rules.md` (missing) |

Порядок resolution:

1. Project-level `AGENTS.md`.
2. Global `AGENTS.md`.
3. Unresolved role -> stop and report.

Disabled roles не должны fallback-ить на global mapping.

## Disabled Roles

- Нет

## Проверка регистрации agents

- Нет.

## AI Pipeline V2

Planning artifacts находятся в:

`ai/specs/<task-id>/`

Required standard-mode artifacts:

- `manifest.md`
- `manifest.yml`
- `architecture-plan.md`
- `architecture-contract.yml`
- `ui-build-sheet.md`, только если UI active
- `ui-contract.yml`, только если UI active
- `compatibility-report.md`
- `summary.md`
- `questions.md`
- `user-review-notes.md`
- `changelog.md`
- `validator-report.json`

Required small-mode artifacts:

- `manifest.yml`
- `architecture-plan.md`, только если research нужен
- `architecture-contract.yml`, только если research нужен
- `questions.md`
- `changelog.md`
- `validator-report.json`

Правила main agent:

- Держать main context компактным.
- Не вставлять full research plans в chat.
- Проводить user questions и answers через `questions.md`.
- Запрашивать approval по artifact versions из `manifest.yml`.
- Не запускать executor, пока artifacts stale, incompatible или unapproved.
- В `small` mode не создавать `summary.md`, `compatibility-report.md`, validation artifacts или code-review artifacts без риска или user request.

Правила research agent:

- Писать detailed plans в `ai/specs/<task-id>/`.
- Возвращать compact status и artifact paths в chat.
- Не редактировать production/source/config files во время research.
- Использовать sections `Do Not Infer`, чтобы заблокировать executor guesswork.
- В `small` mode держать plan около 60-80 строк, evidence около 8-10 files, contracts только compact.

Правила plan coordinator:

- По умолчанию запускать только для `standard` или `large` mode.
- Читать compact contracts и `validator-report.json`.
- Писать `compatibility-report.md` и `summary.md`.
- Не принимать architecture, UI или implementation decisions.
- Назначать conflicts owners.

Executor gates:

- `manifest.yml` approved.
- Current artifact versions совпадают с approved versions.
- `validator-report.json` status = `pass`.
- `compatibility-report.md` status = `pass` для `standard` или `large` mode.
- Нет stale artifacts.
- Нет open blocking questions.
- Required artifacts существуют.

## Runtime Modes

- `small`: один owner domain, без cross-role dependency, coordinator/summary по умолчанию не нужны.
- `standard`: architecture + UI или cross-contract dependency, coordinator required.
- `large`: 3+ domains или unknown/high-risk scope, coordinator required.

Mode выбирает main agent и записывает в `manifest.yml`.
Mode можно повысить, но нельзя понизить в рамках той же task.

## Project Context

- Stack: Android/Kotlin. Refine this after project scan.
- UI source: Compose/Design System if present.
- Project-local rules переопределяют global rules.

## Verification

- Use existing Gradle commands from the project.
- For UI changes, compile affected module and run UI checks if present.

## Core / Rules Update

Если меняется project core architecture, обновить relevant `ai/rules/*` в той же task.
Core/architecture changes без matching rule updates считаются incomplete.
