package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.telnetquiz.constants.NavItem
import com.example.telnetquiz.constants.Screen
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun Navbar(
    navController: NavController,
    backgroundColor: Color = LitecartesColor.Surface
) {
    val haptic = LocalHapticFeedback.current
    val tutorialController = LocalTutorialController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(
                if (tutorialController != null) Modifier.onGloballyPositioned {
                    tutorialController.registerTarget("bottom_navbar", it)
                } else Modifier
            )
    ) {
        BottomNavigation(
            backgroundColor = LitecartesColor.Primary,
            modifier = Modifier
                .background(LitecartesColor.Primary)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val isOnLevelScreen = currentRoute?.startsWith(Screen.LevelScreen.route) == true

            NavItem.items.forEach { item ->
                val isSelected = currentRoute == item.route ||
                    (isOnLevelScreen && item == NavItem.Home)
                val icon = if (isSelected) item.activeIdIcon else item.idIcon
                val targetKey = when (item) {
                    is NavItem.Home -> "navbar_home"
                    is NavItem.Leaderboard -> "navbar_leaderboard"
                    is NavItem.Profile -> "navbar_profile"
                }

                BottomNavigationItem(
                    selected = isSelected,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        tutorialController?.notifyTargetClicked(targetKey)
                        if (!isSelected) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Image(
                            modifier = Modifier
                                .size(40.dp),
                            painter = painterResource(
                                id = icon
                            ),
                            contentDescription = item.label
                        )
                    },
                    modifier = Modifier
                        .padding(
                            vertical = 5.dp
                        )
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget(targetKey, it)
                            } else Modifier
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNavbar(){
    Navbar(navController = rememberNavController())
}