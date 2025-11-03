package com.example.litecartesnative.constants

sealed class Screen(
    val route: String
) {
    object AuthStartScreen : Screen(
        "auth_start_screen"
    )

    object AuthLoginScreen : Screen(
        "auth_login_screen"
    )

    object AuthRegisterScreen : Screen(
        "auth_register_screen"
    )

    object HomeScreen : Screen(
        "home_screen"
    )

    object LevelScreen : Screen(
        "level_screen"
    )

    object QuestionScreen : Screen(
        "question_screen"
    )

    object LeaderboardScreen : Screen(
        "leaderboard_screen"
    )

    object FriendScreen : Screen(
        "friend_screen"
    )

    object ProfileScreen : Screen(
        "profile_screen"
    )

    object QuickCheckScren : Screen(
        "quickcheck_screen"
    )

    object PretestScreen : Screen(
        "pretest_screen"
    )

    object ResultScreen : Screen(
        "result_screen"
    )

    object AboutScreen : Screen(
        "about_screen"
    )

    object FeedbackScreen : Screen(
        "feedback_screen"
    )
}