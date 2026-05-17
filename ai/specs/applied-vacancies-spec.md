## Add new feature: 
Applied Vacancies

## What this feature about:
New page where seeker can see vacancies that he has applied already

## How should it be implemented:
Replace current page "Applications" page for seeker to new Applied Vacancies page.
On this page has to be shown vacancies list, user can tap on vacancy and open vacancy details screen

Vacancy details screen has to be also updated, if user already was applied on that vacancy -> user sees See Application button instead of Apply Now button

## Backend Contract:
Pinned in **applied-vacancies.md** files


## Task phases:
1. Planning:
Research codebase, find out how it has to be implemented, what obstacles can be and how to solve them. If there any uncertainty or low confidence -> ask user
As a result provide concise plan, but with all required technical and product details 
Then ask user to validate that plan, if user rejects -> work on plan, till user will not explicitly approve

2. Implementation: 
Once plan is ready, perform task strictly to plan, making out or making decisions is forbidden, if there is any uncertainty -> stop your work and start new planning phase

3. Validation
Once implementation is finished -> start validation phase. Validate results strictly to plan, without making assumptions, if something is not clear -> ask user.
If validation finished with issues -> start planning phase to find out what's the cause and how to fix it
To validate strictly use:
- unit testing
- @Emulator QA skill

Other tools are forbidden
Perform testing for basic scenarios and edge-cases

4. Report
When validation finished successfully -> create ai/report/[feature-generated-name]-report.md with concise, but informative report about what has been done, what files was changed, how to test

For validation use users from [demo-users.md]


Main agent is the ONLY orchestrator, no of described work in [Task phases] has to be performed directly by main agent
For each phases start separate subagent (default ones, ignore user created agents) and pass to them following information:
- Full initial task till ## Task phases section
- Required to them only instruction, like validation process, implementation and etc

For design system required to use: @ai/design-system/DESIGN.md
