# Commands Reference

## Docker

### Start

```bash
docker compose up --build      # first time or after code changes
docker compose up              # if nothing changed
```

### Stop

```bash
Ctrl+C                         # stop while logs are visible
docker compose down            # stop and remove containers
docker compose down -v         # stop and also wipe the database
```

### Check logs

```bash
docker compose logs backend
docker compose logs frontend
docker compose logs db
```

---

## Git

### Initial setup (one time)

```bash
git clone <your-repo-url>
cd <project-folder>
```

### Add a `.gitignore` in root (one time)

```
backend/target/
frontend/node_modules/
frontend/dist/
```

### Branch setup (one time per person)

Create your feature branch off `dev` — do this once when you start:

```bash
git checkout dev                          # switch to dev
git pull origin dev                       # get latest
git checkout -b feature/<your-branch>     # create your branch
```

Branch names per role:

| Person | Branch name |
|--------|-------------|
| 1 | `feature/backend-entities` |
| 2 | `feature/backend-controllers` |
| 3 | `feature/backend-seeding` |
| 4 | `feature/frontend-routing` |
| 5 | `feature/frontend-restaurant` |
| 6 | `feature/frontend-cart` |

### Daily workflow

```bash
# 1. Sync with latest dev before starting
git checkout dev
git pull origin dev
git checkout feature/<your-branch>
git merge dev                             # bring in teammates' updates

# 2. Do your work, then save progress
git add .
git commit -m "feat: describe what you did"

# 3. Push your branch
git push origin feature/<your-branch>
```

### Commit message format

```
feat: add Cart and CartItem entities
fix: correct foreign key in schema.sql
refactor: simplify CartRepository query
chore: update application.properties
```

Types: `feat`, `fix`, `refactor`, `chore`, `docs`

### Merging your work into dev (via Pull Request)

1. Push your branch: `git push origin feature/<your-branch>`
2. Open a Pull Request on GitHub: `feature/<your-branch>` → `dev`
3. Get at least one teammate to review it
4. Merge into `dev` once approved

### Merging dev into main (before demo only)

```bash
git checkout main
git pull origin main
git merge dev
git push origin main
```

### Resolving merge conflicts

```bash
# After a merge conflict, open the flagged file and look for:
<<<<<<< HEAD
your code
=======
teammate's code
>>>>>>> dev

# Edit the file to keep the correct code, remove the markers, then:
git add <conflicted-file>
git commit -m "fix: resolve merge conflict in <file>"
```

### Useful commands

```bash
git status                     # see changed/staged files
git log --oneline              # see recent commits
git diff                       # see unstaged changes
git stash                      # temporarily shelve changes
git stash pop                  # bring stashed changes back
git reset --soft HEAD~1        # undo last commit, keep changes
git checkout -- .              # discard all local changes (irreversible)
```

### Rules

- ✅ Always branch off `dev`, never `main`
- ✅ Always pull `dev` before starting each session
- ✅ Open a PR to `dev`, never push directly to `main`
- ✅ At least one person reviews each PR before merging
- ❌ Never `git push --force` on `dev` or `main`

---

## URLs (when running)

| Service  | URL                   |
| -------- | --------------------- |
| Frontend | http://localhost:4173 |
| Backend  | http://localhost:8080 |
| Database | localhost:5432        |
