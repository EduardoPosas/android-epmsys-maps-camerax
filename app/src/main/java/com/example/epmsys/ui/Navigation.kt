package com.example.epmsys.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.epmsys.R
import com.example.epmsys.ui.map.MapScreen
import com.example.epmsys.ui.point.PointScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    locationPermissions: MultiplePermissionsState,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Map.route,
    ) {
        composable(route = NavigationRoutes.Map.route) {
            MapScreen(
                locationPermissions = locationPermissions,
                onAddLocation = {
                    navController.navigate(NavigationRoutes.Point.route)
                }
            )
        }

        composable(route = NavigationRoutes.Point.route) {
            PointScreen(
                locationPermissions = locationPermissions,
                navigateUp = {
                    navController.navigateUp()
                },
/*                onOpenCamera = {
                    navController.navigate(NavigationRoutes.Camera.route)
                }*/
            )
        }

/*        composable(route = NavigationRoutes.Camera.route) {
            CameraPreview(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }*/
    }
}

enum class NavigationRoutes(
    val route: String,
    @StringRes val title: Int
) {
    Map(
        route = "map",
        title = R.string.locations
    ),
    Point(
        route = "point",
        title = R.string.new_point
    ),
/*    Camera(
        route = "camera",
        title = R.string.camera
    )*/
}