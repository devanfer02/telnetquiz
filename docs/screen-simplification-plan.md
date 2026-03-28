# Screen Simplification Plan

## Context

Screen files in the mobile app have grown large (300-500+ lines) with inline UI blocks that can't be previewed independently. This plan extracts those blocks into atomic, previewable components — reducing screen file sizes and enabling isolated component development. **LevelScreen is excluded** (WIP).

Base source root: `app/src/main/java/com/example/telnetquiz/`

---

## Phase 1 — Global Shared Components

New files in `components/`. These deduplicate patterns used across 2+ features.

### 1A. `components/LoadingButton.kt`

**Duplicated in:** AuthLoginScreen (lines 217-243), AuthRegisterScreen (lines 281-313), QuestionScreen (lines 299-345)

Pattern: `Box` with `Button` (text hidden when loading) + `CircularProgressIndicator` overlay.

```kotlin
@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = LitecartesColor.Secondary,
    textColor: Color = LitecartesColor.Surface,
    borderColor: Color = LitecartesColor.DarkBrown
)
```

Add `@Preview` for normal + loading states.

### 1B. `components/EmptyStateBox.kt`

**Duplicated in:** ChapterScreen (lines 190-214), LeaderboardScreen (lines 218-247), ProfileScreen (lines 329-339)

Pattern: Centered column with optional image + title + subtitle text.

```kotlin
@Composable
fun EmptyStateBox(
    title: String,
    subtitle: String = "",
    imageResId: Int? = null,
    imageSize: Dp = 160.dp,
    modifier: Modifier = Modifier
)
```

Add `@Preview` with/without image.

### 1C. `components/ScoreCountRow.kt`

**Duplicated in:** ResultScreen (lines 99-153), PretestResultScreen (lines 149-191)

Pattern: `Row` with two rounded pill `Column`s, each showing `icon_benar`/`icon_salah` + count.

```kotlin
@Composable
fun ScoreCountRow(
    correctCount: Int,
    wrongCount: Int,
    modifier: Modifier = Modifier
)
```

Add `@Preview`.

---

## Phase 2 — ProfileScreen (513 lines → ~218)

### 2A. Move `AchievementCard` → `features/user/presentations/components/AchievementCard.kt`

Already a `private` composable at lines 360-459. Move as-is, change to public, keep existing previews.

```kotlin
fun AchievementCard(title: String, description: String, unlocked: Boolean, modifier: Modifier = Modifier)
```

### 2B. Move `StatCard` → `features/user/presentations/components/StatCard.kt`

Already a `private` composable at lines 462-502. Move, change to public, **add** a `@Preview` (currently missing).

```kotlin
fun StatCard(label: String, value: String, modifier: Modifier = Modifier)
```

### 2C. Extract `ProfileHeaderSection` → `features/user/presentations/components/ProfileHeaderSection.kt`

Lines 88-266: the orange rounded box with avatar, edit button, name, email, school, bio, stats row.

```kotlin
fun ProfileHeaderSection(
    profile: UserProfileDto?,
    isLoading: Boolean,
    error: String?,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
)
```

Add `@Preview` with fake `UserProfileDto`. Wire `EmptyStateBox` for achievements empty state (lines 329-339). Wire `StatCard` for the stats row.

---

## Phase 3 — LeaderboardScreen (426 lines → ~353)

### 3A. Move `SegmentedToggle` → `features/user/presentations/components/SegmentedToggle.kt`

Already a `private` composable at lines 358-398. Move, change to public, add `@Preview`.

```kotlin
fun SegmentedToggle(selectedTab: LeaderboardTab, onTabSelected: (LeaderboardTab) -> Unit, modifier: Modifier = Modifier)
```

### 3B. Extract `CurrentUserRankBox` → `features/user/presentations/components/CurrentUserRankBox.kt`

Lines 305-336: the "Peringkat kamu" box at the bottom of the leaderboard tab.

```kotlin
fun CurrentUserRankBox(rank: Int, totalScore: Int, modifier: Modifier = Modifier)
```

Add `@Preview`. Replace empty activity state (lines 218-247) with `EmptyStateBox`.

---

## Phase 4 — QuestionScreen (413 lines → ~233)

### 4A. Extract `QuestionHeaderBox` → `features/quiz/presentation/components/QuestionHeaderBox.kt`

Lines 171-250: gradient-background box with title, sound button, optional image, description.

```kotlin
fun QuestionHeaderBox(
    title: String,
    description: String,
    imageLink: String?,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

Add `@Preview` with/without image.

### 4B. Extract `VerifyButton` → `features/quiz/presentation/components/VerifyButton.kt`

Lines 295-345: the verify/continue outline button + submitting spinner.

```kotlin
fun VerifyButton(
    isVisible: Boolean,
    isVerifying: Boolean,
    isSubmitting: Boolean,
    isLastQuestion: Boolean,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier
)
```

Add `@Preview` for multiple states.

### 4C. Extract `AnswerFeedbackSheet` → `features/quiz/presentation/components/AnswerFeedbackSheet.kt`

Lines 347-395: `ModalBottomSheet` with correct/incorrect feedback, mascot image, next/finish button.

```kotlin
fun AnswerFeedbackSheet(
    isCorrect: Boolean,
    isLastQuestion: Boolean,
    onDismiss: () -> Unit,
    onContinue: () -> Unit
)
```

Add `@Preview` for correct + incorrect variants.

---

## Phase 5 — Auth Screens

### 5A. Move `GenderToggleButton` → `features/auth/presentation/components/GenderToggleButton.kt`

From AuthRegisterScreen lines 322-365. Move, change to public, add `@Preview` (selected + unselected).

### 5B. Extract `ForgotPasswordDialog` → `features/auth/presentation/components/ForgotPasswordDialog.kt`

From AuthLoginScreen lines 265-299.

```kotlin
fun ForgotPasswordDialog(onDismiss: () -> Unit)
```

### 5C. Extract `ErrorBottomSheet` → `features/auth/presentation/components/ErrorBottomSheet.kt`

From AuthLoginScreen lines 301-345.

```kotlin
fun ErrorBottomSheet(title: String, message: String, onDismiss: () -> Unit)
```

### 5D. Wire `LoadingButton` into AuthLoginScreen (lines 217-243) and AuthRegisterScreen (lines 281-313).

---

## Phase 6 — EditProfileScreen (359 lines → ~259)

### 6A. Extract `CloseHeader` → `features/user/presentations/components/CloseHeader.kt`

Lines 113-143: row with close button + title.

```kotlin
fun CloseHeader(title: String, onClose: () -> Unit, modifier: Modifier = Modifier)
```

### 6B. Extract `ProfileImagePicker` → `features/user/presentations/components/ProfileImagePicker.kt`

Lines 147-215: avatar box with camera overlay + async image + "Maks 2MB" text.

```kotlin
fun ProfileImagePicker(currentImageUrl: Any?, onClick: () -> Unit, modifier: Modifier = Modifier)
```

---

## Phase 7 — Remaining Screens

### 7A. Extract `MaterialContentCard` → `features/auth/presentation/components/MaterialContentCard.kt`

From FeedbackScreen lines 92-171. Move `sanitizeHtml` helper with it (lines 272-285).

```kotlin
fun MaterialContentCard(title: String, content: String, imageLink: String?, onSpeakClick: () -> Unit, modifier: Modifier = Modifier)
```

### 7B. Move `ComingSoonCard` → `features/chapter/presentation/components/ComingSoonCard.kt`

From ChapterScreen lines 53-90. Already a public composable, just needs its own file + `@Preview`.

### 7C. Wire `ScoreCountRow` into ResultScreen (lines 99-153) and PretestResultScreen (lines 149-191).

### 7D. Wire `EmptyStateBox` into ChapterScreen (lines 190-214).

---

## Screens Skipped

| Screen | Lines | Reason |
|--------|-------|--------|
| LevelScreen | — | WIP, explicitly excluded |
| AuthStartScreen | 146 | Small enough |
| AboutScreen | 111 | Small enough |
| PretestScreen | 225 | Already well-structured, no duplication |
| RemedialScreen | 130 | Small enough |

---

## Verification

After each phase:
1. Check Gradle safety (`pgrep -f 'gradle\|GradleDaemon'` and `pgrep -f 'qemu-system\|emulator'`)
2. Run `./gradlew assembleDebug` to verify compilation
3. Spot-check that `@Preview` annotations render (IDE or lint)

---

## Summary

| Metric | Before | After |
|--------|--------|-------|
| New component files | 0 | 18 |
| ProfileScreen | 513 lines | ~218 lines |
| QuestionScreen | 413 lines | ~233 lines |
| LeaderboardScreen | 426 lines | ~353 lines |
| EditProfileScreen | 359 lines | ~259 lines |
| AuthLoginScreen | 358 lines | ~278 lines |
| AuthRegisterScreen | 376 lines | ~332 lines |
| Total lines extracted | — | ~1,080 lines |
