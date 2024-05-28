package com.example.epmsys.ui.map

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.epmsys.R
import com.example.epmsys.ui.components.CircularIndicator
import com.example.epmsys.ui.components.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    locationPermissions: MultiplePermissionsState,
    modifier: Modifier = Modifier,
    onAddLocation: () -> Unit,
) {

    // Maps viewModel
    val mapViewModel: MapViewModel = hiltViewModel()
    val currentLocation = mapViewModel.currentLocation
    val points = mapViewModel.mapUiState.collectAsState().value

    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

//    val accessMediaPermissions =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            listOf(
//                Manifest.permission.READ_MEDIA_IMAGES,
//                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
//            )
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            listOf(
//                Manifest.permission.READ_MEDIA_IMAGES,
//            )
//        } else {
//            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//
//    val accessMediaPermissionState =
//        rememberMultiplePermissionsState(permissions = accessMediaPermissions)

    //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    // Map config
    var mapProperties by remember {
        mutableStateOf(MapProperties())
    }
    var isMapLoaded by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            mapProperties =
                mapProperties.copy(isMyLocationEnabled = locationPermissions.allPermissionsGranted)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.locations),
                //scrollBehavior = scrollBehavior,
                actions = {
                    ShareLocationButton(onShareLocation = {
                        if (!locationPermissions.allPermissionsGranted) locationPermissions.launchMultiplePermissionRequest()
                    })
                    NewLocationActionButton(isLocationEnabled = locationPermissions.allPermissionsGranted) {
                        onAddLocation()
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxSize()
        //.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                isMapLoaded = true
                if (!locationPermissions.allPermissionsGranted) {
                    Toast.makeText(
                        context,
                        "Must Share Location",
                        Toast.LENGTH_LONG
                    ).show()
                    return@GoogleMap
                }
                mapViewModel.getCurrentLocation()
                if (currentLocation.value != null) {
                    initialLocation(
                        scope,
                        cameraPositionState,
                        LatLng(currentLocation.value?.latitude!!, currentLocation.value?.longitude!!)
                    )
                }
            }
        ) {
            points.pointList.forEach { point ->
                /*Marker(
                    state = rememberMarkerState(position = LatLng(point.latitude, point.longitude)),
                    title = point.action,
                    snippet = point.category,
                    tag = point
                )*/
                val image = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(point.photoUrl)
                        .size(Size.ORIGINAL)
                        .build(),
                    imageLoader = ImageLoader
                        .Builder(LocalContext.current)
                        .allowHardware(false)
                        .build()
                )
                val imageState = image.state

                MarkerInfoWindow(
                    state = rememberMarkerState(position = LatLng(point.latitude, point.longitude)),
                    tag = point,
                    title = point.action,
                    snippet = point.category,
                    onClick = {
//                        if (!accessMediaPermissionState.allPermissionsGranted) {
//                            accessMediaPermissionState.launchMultiplePermissionRequest()
//                        }
                        false
                    }
                ) { marker ->
                    Column(
                        modifier = modifier
                            .width(250.dp)
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = marker.title ?: "Default Marker Title",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = marker.snippet ?: "Default Marker Snippet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = point.dateTime,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
//                        if (accessMediaPermissionState.allPermissionsGranted) {
                        if (imageState is AsyncImagePainter.State.Loading) {
                            Image(
                                imageVector = Icons.Default.Downloading,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (imageState is AsyncImagePainter.State.Success) {
                            Image(
                                painter = imageState.painter,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (imageState is AsyncImagePainter.State.Error) {
                            Image(
                                imageVector = Icons.Default.ImageNotSupported,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
//                        }
                    }
                }
            }
        }

        if (!isMapLoaded) {
            CircularIndicator(
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun NewLocationActionButton(
    isLocationEnabled: Boolean,
    onAddLocation: () -> Unit
) {
    IconButton(
        onClick = onAddLocation,
        enabled = isLocationEnabled
        /*
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        */
    ) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
    }
}

@Composable
fun ShareLocationButton(
    onShareLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onShareLocation,
        modifier = modifier,
    ) {
        Icon(imageVector = Icons.Default.ShareLocation, contentDescription = null)
        Spacer(modifier = modifier.width(8.dp))
        Text(text = "Share Location")
    }
}

fun initialLocation(
    scope: CoroutineScope,
    cameraPositionState: CameraPositionState,
    initialLocation: LatLng?
) {
    scope.launch {
        if (initialLocation != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(initialLocation, 18F),
                durationMs = 1000
            )
        }
    }
}