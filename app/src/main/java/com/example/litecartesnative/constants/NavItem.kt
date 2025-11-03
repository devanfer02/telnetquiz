package com.example.litecartesnative.constants

import com.example.litecartesnative.R

sealed class NavItem(
    val route: String,
    val idIcon: Int,
    val activeIdIcon: Int,
    val label: String
) {
    object Home : NavItem(
        Screen.HomeScreen.route,
        R.drawable.home,
        R.drawable.active_family_home,
        "home"
    )

    object Leaderboard : NavItem(
        Screen.LeaderboardScreen.route,
        R.drawable.emoji_events,
        R.drawable.active_emoji_events,
        "leaderboard"
    )

    object Friends : NavItem(
        Screen.FriendScreen.route,
        R.drawable.add_friends,
        R.drawable.active_add_friends,
        "friends"
    )

    object Profile : NavItem(
        Screen.ProfileScreen.route,
        R.drawable.profile,
        R.drawable.active_account_circle,
        "profile"
    )

    companion object {
        val items = listOf(Home, Leaderboard, Friends, Profile)
    }
}