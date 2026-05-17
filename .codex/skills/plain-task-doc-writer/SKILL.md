---
name: plain-task-doc-writer
description: Use when converting user-dictated plain-text task notes, feature ideas, bug descriptions, or rough requirements into a clean Markdown task document under ai/specs.
---

# Plain Task Doc Writer

## Overview

Convert only the user's supplied text into a concise Markdown task document. Do not research the codebase, inspect implementation files, verify referenced contracts, or add workflow instructions that belong to agents.

## Hard Rules

- Use only information from the user's prompt and follow-up answers.
- Do not inspect repo files, search code, read backend contracts, or infer implementation from project structure.
- Ask only blocking questions that are visible from the supplied text and required to avoid changing meaning.
- Use `request_user_input` when available for blocking questions.
- Never add `Task phases`, orchestration instructions, validation phases, report phases, or agent workflow text.
- Preserve explicit constraints, names, paths, files, contracts, roles, statuses, and product wording.
- Clean dictation into clear documentation without inventing scope, APIs, schemas, screens, edge cases, rollout, or tests.

## Workflow

1. Read the dictated text as the only source of truth.
2. Identify the main task title and generate a kebab-case name, for example `Applied Vacancies` -> `applied-vacancies`.
3. Decide whether blocking questions are needed:
   - Ask when required title, target user, requested behavior, or destination meaning is ambiguous.
   - Do not ask optional polish questions.
   - If uncertainty can be captured as an explicit `Open Questions` section without blocking document usefulness, write the file instead of asking.
4. Choose adaptive sections from the text. Common sections:
   - `Overview`
   - `User Goal`
   - `Requested Behavior`
   - `UI Notes`
   - `Backend Contract`
   - `Acceptance Criteria`
   - `Open Questions`
5. Omit sections with no source text.
6. Write final Markdown to `ai/specs/{generated-name}-spec.md`.
7. If target file exists, ask before overwriting. If user does not approve overwrite, create a suffixed alternative such as `ai/specs/{generated-name}-2-spec.md`.

## Writing Rules

- Title: `# <Task title>`.
- Prefer short paragraphs and bullets.
- Use user's language unless they request another language.
- Keep named files exactly as provided, for example `applied-vacancies.md`.
- If user says a backend contract is pinned in a file, write that reference but do not verify the file.
- Mark unresolved source ambiguity under `Open Questions`; do not fill gaps with assumptions unless the user answered them.
- Acceptance criteria may be added only when directly derivable from supplied behavior.

## Example Shape

```markdown
# Applied Vacancies

## Overview
New seeker page where user can see vacancies they already applied to.

## Requested Behavior
- Replace current seeker `Applications` page with `Applied Vacancies`.
- Show list of applied vacancies.
- Tapping vacancy opens vacancy details.

## Vacancy Details
- If seeker already applied to vacancy, show `See Application` instead of `Apply Now`.

## Backend Contract
Pinned in `applied-vacancies.md`.
```
