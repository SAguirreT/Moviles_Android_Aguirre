package com.aguirre.clinicasalud.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.aguirre.clinicasalud.components.DrawerContent
import com.aguirre.clinicasalud.data.MockData
import com.aguirre.clinicasalud.model.Appointment
import com.aguirre.clinicasalud.screens.*
import com.example.clinicasalud.screens.MedicalHistoryScreen
import com.example.clinicasalud.screens.ProfileScreen
import com.example.clinicasalud.screens.ScheduleAppointmentScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val appointmentsList = remember { mutableStateListOf(*MockData.initialAppointments.toTypedArray()) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onDoctorClick = { id -> navController.navigate(Screen.DoctorProfile.createRoute(id)) }
                )
            }

            composable(
                route = Screen.DoctorProfile.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStack ->
                val doctorId = backStack.arguments?.getInt("doctorId") ?: 1
                DoctorProfileScreen(
                    doctorId = doctorId,
                    onBack = { navController.popBackStack() },
                    onScheduleClick = { id -> navController.navigate(Screen.ScheduleAppointment.createRoute(id)) }
                )
            }

            composable(
                route = Screen.ScheduleAppointment.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStack ->
                val doctorId = backStack.arguments?.getInt("doctorId") ?: 1
                ScheduleAppointmentScreen(
                    doctorId = doctorId,
                    onBack = { navController.popBackStack() },
                    onConfirmClick = { docName, date, time ->
                        appointmentsList.add(0, Appointment(appointmentsList.size + 1, docName, date, "$time am", "Confirmada"))
                        navController.navigate(Screen.Confirmation.createRoute(docName, date, time))
                    }
                )
            }

            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument("doctorName") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType }
                )
            ) { backStack ->
                val docName = backStack.arguments?.getString("doctorName") ?: ""
                val date = backStack.arguments?.getString("date") ?: ""
                val time = backStack.arguments?.getString("time") ?: ""

                ConfirmationScreen(
                    doctorName = docName,
                    date = date,
                    time = time,
                    onViewAppointmentsClick = {
                        navController.navigate(Screen.MyAppointments.route) { popUpTo(Screen.Home.route) }
                    }
                )
            }

            composable(Screen.MyAppointments.route) {
                MyAppointmentsScreen(
                    appointments = appointmentsList,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.MedicalHistory.route) {
                MedicalHistoryScreen(onOpenDrawer = { scope.launch { drawerState.open() } })
            }

            composable(Screen.Profile.route) {
                ProfileScreen(onOpenDrawer = { scope.launch { drawerState.open() } })
            }
        }
    }
}