package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.habittracker.ui.screens.AddEditHabitScreen
import com.example.habittracker.ui.screens.HabitListScreen
import com.example.habittracker.viewmodel.HabitViewModel
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import com.example.habittracker.ui.screens.SettingsScreen
import com.example.habittracker.ui.screens.StatisticsScreen


data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            val viewModel: HabitViewModel = viewModel()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute in listOf("habits", "statistics", "settings")

            val bottomNavItems = listOf<BottomNavItem>(
                BottomNavItem("habits", "привычки", Icons.Default.List),
                BottomNavItem("statistics", "статистика", Icons.Default.Info),
                BottomNavItem("settings", "настройки", Icons.Default.Settings)
            )

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        NavigationBar {


                            bottomNavItems.forEach { item ->
                                ShortNavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) },
                                )
                            }
                        }
                    }

                }
            ) { paddingValues ->
                Box(Modifier.padding(paddingValues)) {
                    NavHost(navController, startDestination = "habits") {
                        composable("habits") {
                            HabitListScreen(
                                viewModel,
                                onHabitClick = { habit ->
                                    navController.navigate("habits/${habit.id}")},
                                onAddHabit = { navController.navigate("addHabit") }) }

                        composable("addHabit") { AddEditHabitScreen(viewModel) { navController.popBackStack() } }

                        composable("statistics") { StatisticsScreen() }
                        composable("settings") { SettingsScreen() }

                        composable(
                            route = "habits/{habitId}",
                            arguments = listOf(
                                navArgument("habitId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val habitId = backStackEntry.arguments?.getInt("habitId") ?: return@composable
                            AddEditHabitScreen(
                                viewModel = viewModel,
                                habitId = habitId,
                            ) { navController.popBackStack() }

                        }
                    }
                }
            }


        }
    }
}
