---
name: atomic-commits
description: Guidelines for creating atomic commits - small, focused, self-contained changes that do one thing well. Use with `/atomic-commits` command.
---

# Atomic Commits

## Instructions

Create atomic commits: each commit should represent ONE logical change that can be understood, reviewed, and reverted independently.

### 1. What Makes a Commit Atomic
*   **Single Purpose**: One commit = one logical change (fix, feature, refactor, docs, test).
*   **Self-Contained**: The codebase should build and tests should pass after each commit.
*   **Minimal Scope**: Include only files necessary for that specific change.
*   **No Mixing**: Never combine unrelated changes (e.g., don't mix a bug fix with a refactor).

### 2. Commit Message Format
```
<type>(<scope>): <subject>

[optional body]

[optional footer]
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code restructuring (no behavior change)
- `docs`: Documentation only
- `test`: Adding/updating tests
- `chore`: Build, config, dependencies
- `style`: Formatting (no code change)
- `perf`: Performance improvement

**Rules**:
- Subject: imperative mood, lowercase, no period, ≤50 chars
- Body: wrap at 72 chars, explain *what* and *why* (not *how*)

### 3. Workflow for Making Atomic Commits

```bash
# 1. Review all changes
git status
git diff

# 2. Stage related changes only (use -p for partial staging)
git add -p <file>        # Interactive: stage specific hunks
git add <specific-files> # Stage complete files

# 3. Verify staged changes are atomic
git diff --staged

# 4. Commit with descriptive message
git commit -m "type(scope): subject"

# 5. Repeat for remaining changes

# 6. Push when all commits are ready
git push
```

### 4. Interactive Staging (`git add -p`)
Use patch mode to split changes within a file:
- `y` - stage this hunk
- `n` - skip this hunk
- `s` - split into smaller hunks
- `e` - manually edit the hunk
- `q` - quit

### 5. Examples

**Good Atomic Commits**:
```
feat(auth): add password reset endpoint
fix(cart): correct quantity calculation on update
refactor(user): extract validation to separate module
test(api): add integration tests for order service
docs(readme): update installation instructions
```

**Bad (Non-Atomic) Commits**:
```
# Too vague
"updates"
"fixes"
"WIP"

# Mixed concerns
"add login feature and fix header styling and update deps"
```

### 6. When to Commit
*   After completing a single logical unit of work
*   Before switching to a different task
*   When you can write a clear, single-purpose commit message
*   After tests pass for the change

### 7. Splitting Large Changes
If a feature is large, break it into atomic commits:
```
feat(auth): add User model for authentication
feat(auth): implement password hashing utility
feat(auth): create login endpoint
feat(auth): add login form component
test(auth): add unit tests for login flow
```

### 8. Quick Command Reference
```bash
git add -p                    # Interactive staging
git reset HEAD <file>         # Unstage file
git commit --amend            # Fix last commit (before push)
git rebase -i HEAD~n          # Reorganize last n commits
git stash                     # Temporarily store changes
git diff --staged             # Review what will be committed
```