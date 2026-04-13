# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Gradle Safety Rules

**BEFORE running ANY `./gradlew` command**, you MUST check for conflicts:

1. **Check if Gradle is already running**: Run `pgrep -f 'gradle|GradleDaemon'`. If any process is found, **DO NOT run Gradle**. Instead, notify the user: _"Cannot run Gradle — a Gradle process is already running (likely from Android Studio). Please wait for it to finish or stop it before retrying."_
2. **Check if an Android Studio emulator is running**: Run `pgrep -f 'qemu-system|emulator'`. If any process is found, **DO NOT run Gradle**. Instead, notify the user: _"Cannot run Gradle — an Android Studio emulator is currently running. Running Gradle from CLI while the emulator/Android Studio is active can cause build conflicts and lock issues. Please close the emulator or run the build from Android Studio instead."_

If both checks pass (no output from either `pgrep`), proceed with the Gradle command.

## Code Comments

**Do NOT add unnecessary comments to code.** Remove comments that merely restate what the code does (e.g., `// increment counter`, `// return result`). Only add comments when:
- You are documenting a workaround for a bug or loop you ran into during implementation
- You are explaining a non-obvious solution to a problem that was solved after investigation
- The logic is genuinely subtle and would confuse a reader without context

When editing existing code, remove any unnecessary comments you encounter in the lines you touch.

## Code Knowledge Graph

This project has a **code-review-graph** knowledge graph at `.code-review-graph/graph.db`. Use it instead of scanning the whole codebase:

- **Before exploring code**, query the graph (`query_graph_tool`, `semantic_search_nodes_tool`, `get_review_context_tool`) to find relevant files, classes, and their relationships.
- **After every code change**, run `build_or_update_graph_tool` (incremental) with `repo_root` set to this project's directory to keep the graph in sync.
- **For code reviews**, use `get_impact_radius_tool` to understand blast radius of changes instead of manually tracing imports/inheritance.
- **To find large/complex functions**, use `find_large_functions_tool` instead of grepping the codebase.

This saves significant time and tokens compared to re-reading files to understand the codebase structure.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run Android instrumented tests
./gradlew connectedAndroidTest

# Clean and rebuild
./gradlew clean build

# Run lint checks
./gradlew lint
```

## Architecture

This is a native Android app built with Kotlin and Jetpack Compose for TelNetQuiz — an educational quiz application for Vocational (SMK) students studying the "Media dan Jaringan Telekomunikasi" subject.

**Tech Stack:**
- Kotlin 1.9.0 with Jetpack Compose
- Navigation Compose for routing
- Hilt for dependency injection (configured but minimally used)
- Arrow-kt for functional programming utilities
- Coil for image loading
- Target SDK 34, Min SDK 24

**Code Organization:**

The app follows a feature-based architecture under `app/src/main/java/com/example/litecartesnative/`:

- `components/` - Shared UI components (Button, Navbar, Navigation, StrokedText)
- `constants/` - Route definitions (Screen sealed class), static quiz data, navigation items
- `features/` - Feature modules, each with `presentation/` (screens, components) and optionally `domain/` (models)
  - `auth/` - Authentication screens (start, login, register, about)
  - `chapter/` - Chapter selection screen
  - `quiz/` - Quiz functionality (levels, questions, results, feedback)
  - `pretest/` - Pretest functionality
  - `user/` - Profile, leaderboard, friends
- `ui/theme/` - Material 3 theming (LitecartesColor palette, typography with Nunito font)

**Navigation:**

Routes defined as sealed class objects in `constants/Screen.kt`. The NavHost in `components/Navigation.kt` handles all routing with typed arguments:
- Simple routes: `Screen.AuthStartScreen.route`
- Routes with args: `${Screen.LevelScreen.route}/{id}` or `${Screen.QuestionScreen.route}/{chapterId}/levels/{level}/questions/{id}`

**State Management:**

- Singleton objects for global state: `WrongQuizManager` (tracks wrong answers in ArrayDeque), `MarkAsDoneManager` (2D BooleanArray for completed levels)
- Local state via Compose `remember { mutableStateOf() }`
- No ViewModel/LiveData pattern in use

**Data:**

Quiz content is hardcoded in `constants/` (chaptersData, pretestsData, levelsData). Models in `features/*/domain/model/` are data classes (Chapter, LevelData, Question, Material, QuizIndex).
