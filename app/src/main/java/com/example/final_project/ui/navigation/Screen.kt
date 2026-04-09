package com.example.final_project.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.final_project.R

sealed class Screen(val route: String) {
    data object Translate : Screen("translate")
    data object History : Screen("history")
    data object Settings : Screen("settings")
}
sealed class NavIcon {
    data class Vector(val imageVector: ImageVector) : NavIcon()
    data class Drawable(val resId: Int) : NavIcon()
}
data class BottomNavItem(
    val screen:         Screen,
    val label:          Int,
    val selectedIcon:   NavIcon,
    val unselectedIcon: NavIcon,
    val badgeCount:     Int? = null,
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Translate,
        label =  R.string.nav_translate,
        selectedIcon = NavIcon.Drawable(R.drawable.sign_language_filled_24),
        unselectedIcon = NavIcon.Drawable(R.drawable.sign_language_outlined_24),
    ),
    BottomNavItem(
        screen = Screen.History,
        label =  R.string.nav_history,
        selectedIcon = NavIcon.Drawable(R.drawable.history_24),
        unselectedIcon = NavIcon.Drawable(R.drawable.history_24),
    ),
    BottomNavItem(
        screen = Screen.Settings,
        label =  R.string.nav_settings,
        selectedIcon = NavIcon.Vector(Icons.Filled.Settings),
        unselectedIcon = NavIcon.Vector(Icons.Outlined.Settings),
    ),
)