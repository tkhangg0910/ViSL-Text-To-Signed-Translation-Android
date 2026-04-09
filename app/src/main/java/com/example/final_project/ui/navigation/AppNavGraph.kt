package com.example.final_project.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.final_project.ui.screens.history.HistoryScreen
import com.example.final_project.ui.screens.settings.SettingsScreen
//import com.example.final_project.ui.screens.settings.SettingsScreen
import com.example.final_project.ui.screens.translate.TranslateScreen
import com.example.final_project.ui.theme.Background
import com.example.final_project.ui.theme.Primary
import com.example.final_project.ui.theme.Surface
import com.example.final_project.ui.theme.TextMuted


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ViSLBottomBar(navController = navController)
        },
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Translate.route,
            modifier         = Modifier.padding(innerPadding),
            enterTransition  = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> (fullWidth * 0.08f).toInt() }
                        )
            },
            exitTransition   = {
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition  = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> -(fullWidth * 0.08f).toInt() }
                        )
            },
            popExitTransition   = {
                fadeOut(animationSpec = tween(200))
            },

            ) {
            composable(Screen.Translate.route) {
                TranslateScreen(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    },
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    onPlayItem = {
                        navController.navigate(Screen.Translate.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun RenderIcon(icon: NavIcon, contentDescription: String) {
    when (icon) {
        is NavIcon.Vector -> Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription
        )
        is NavIcon.Drawable -> Icon(
            painter = painterResource(icon.resId),
            contentDescription = contentDescription
        )
    }
}

// Bottom Bar
@Composable
private fun ViSLBottomBar(
    navController: NavHostController,
) {
    val navBackStackEntry  by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination
                ?.hierarchy
                ?.any { it.route == item.screen.route } == true

            NavigationBarItem(
                selected = isSelected,
                onClick  = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null && item.badgeCount > 0) {
                                Badge { Text(item.badgeCount.toString()) }
                            }
                        }
                    ) {
                        RenderIcon(
                            icon = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.label)
                        )                    }
                },
                label = {
                    Text(
                        text = stringResource(item.label),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )                },
                colors = NavigationBarItemDefaults.colors(    selectedIconColor   = MaterialTheme.colorScheme.primary,
                    selectedTextColor   = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor      = MaterialTheme.colorScheme.surface,
                ),
            )
        }
    }
}

