

package com.hudyweas.workouttogether

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.hudyweas.workouttogether.common.composable.PermissionDialog
import com.hudyweas.workouttogether.common.composable.RationaleDialog
import com.hudyweas.workouttogether.common.snackbar.SnackbarManager
import com.hudyweas.workouttogether.screens.edit_workout.EditWorkoutScreen
import com.hudyweas.workouttogether.screens.login.LoginScreen
import com.hudyweas.workouttogether.screens.settings.SettingsScreen
import com.hudyweas.workouttogether.screens.sign_up.SignUpScreen
import com.hudyweas.workouttogether.screens.splash.SplashScreen
import com.hudyweas.workouttogether.screens.workouts.WorkoutsScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.hudyweas.workouttogether.screens.workout_map.MapScreen
import com.hudyweas.workouttogether.screens.workoutdetails.WorkoutDetailsScreen
import kotlinx.coroutines.CoroutineScope

@Composable
@ExperimentalMaterialApi
fun WorkoutTogetherApp() {
  AppTheme {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      RequestNotificationPermissionDialog()
    }

    Surface{
      val appState = rememberAppState()

      Scaffold(
        snackbarHost = {
          SnackbarHost(
            hostState = it,
            modifier = Modifier.padding(8.dp),
            snackbar = { snackbarData ->
              Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
            }
          )
        },
        scaffoldState = appState.scaffoldState
      ) { innerPaddingModifier ->
        NavHost(
          navController = appState.navController,
          startDestination = SPLASH_SCREEN,
          modifier = Modifier.padding(innerPaddingModifier)
        ) {
          workoutTogetherGraph(appState)
        }
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
  val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

  if (!permissionState.status.isGranted) {
    if (permissionState.status.shouldShowRationale) RationaleDialog()
    else PermissionDialog { permissionState.launchPermissionRequest() }
  }
}

@Composable
fun rememberAppState(
  scaffoldState: ScaffoldState = rememberScaffoldState(),
  navController: NavHostController = rememberNavController(),
  snackbarManager: SnackbarManager = SnackbarManager,
  resources: Resources = resources(),
  coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
  remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
    WorkoutTogetherAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
  }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
  LocalConfiguration.current
  return LocalContext.current.resources
}

@ExperimentalMaterialApi
fun NavGraphBuilder.workoutTogetherGraph(appState: WorkoutTogetherAppState) {
  composable(SPLASH_SCREEN) {
    SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
  }

  composable(SETTINGS_SCREEN) {
    SettingsScreen(
      restartApp = { route -> appState.clearAndNavigate(route) },
      openScreen = { route -> appState.navigate(route) }
    )
  }

  composable(LOGIN_SCREEN) {
    LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
  }

  composable(SIGN_UP_SCREEN) {
    SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
  }

  composable(WORKOUTS_SCREEN) { WorkoutsScreen(openScreen = { route -> appState.navigate(route) }) }

  composable(
    route = "$EDIT_WORKOUT_SCREEN$TASK_ID_ARG",
    arguments = listOf(navArgument(TASK_ID) {
      nullable = true
      defaultValue = null
    })
  ) {
    EditWorkoutScreen(
      popUpScreen = { appState.popUp() }
    )
  }

  composable(
    route = "$WORKOUT_DETAILS_SCREEN$TASK_ID_ARG",
    arguments = listOf(navArgument(TASK_ID) {
      nullable = true
      defaultValue = null
    })
  ) {
    WorkoutDetailsScreen(
      popUpScreen = { appState.popUp() }
    )
  }

  composable(WORKOUT_MAP) { MapScreen() }
}
