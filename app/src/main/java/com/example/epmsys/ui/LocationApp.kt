package com.example.epmsys.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.epmsys.ui.theme.EpmsysTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationApp(navController: NavHostController = rememberNavController()) {

    // Location Permissions
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Navigation(
        navController = navController,
        locationPermissions = locationPermissions
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LocationAppPreview() {
    EpmsysTheme {
        LocationApp(navController = rememberNavController())
    }
}
