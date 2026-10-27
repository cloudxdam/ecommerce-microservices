# Contributing

## Commit messages

This repository follows [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description, imperative, lowercase>

feat(auth): implement login and refresh rotation
fix(inventory): prevent stock going negative under concurrency
docs(readme): document how to run the stack
chore(ci): add gitleaks to the pipeline
```

Allowed types: `feat`, `fix`, `docs`, `chore`, `refactor`, `test`, `perf`, `build`, `ci`, `style`.

## Branching

Git-flow, kept simple:

```
main     ← production; only receives merges from develop via PR, CI green
develop  ← integration branch; all feature branches start here
feat/*   ← one phase / feature per branch
fix/*    ← bug fixes, branched from develop
hotfix/* ← urgent fixes, branched from main
```

Workflow for one feature:

```bash
git switch develop && git pull
git switch -c feat/<name>
# ... work, test, commit ...
git push -u origin feat/<name>
# → open a Pull Request against develop
# → CI must be green before merging
```

Merges into `main` use **Create a merge commit** (never squash or rebase), so the history shows each feature as a separate piece of work.

## Rules

- **Never commit secrets.** Keys, tokens and `.env` files stay out of the repo. `gitleaks` runs in CI and will fail the build.
- **Never merge with failing tests.** If a test is flaky, fix it; do not re-run until green.
- **Tests are not optional.** A feature without tests is not done.
- **Write the ADR before the code** when a decision is non-obvious. See `docs/adr/`.
