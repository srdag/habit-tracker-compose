package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.ui.screens.AddEditHabitScreen
import com.example.habittracker.ui.screens.HabitListScreen
import com.example.habittracker.ui.theme.HabitTrackerTheme
import com.example.habittracker.viewmodel.HabitViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            val viewModel: HabitViewModel = viewModel()

            NavHost(navController, startDestination = "habits") {
                composable("habits") { HabitListScreen(viewModel) { navController.navigate("add_habit") } }
                composable("add_habit") { AddEditHabitScreen(viewModel) { navController.popBackStack() } }
            }
        }
    }
}
