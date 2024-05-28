package com.example.epmsys.ui.point

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.epmsys.R
import com.example.epmsys.ui.camera.CameraPreview
import com.example.epmsys.ui.components.Actions
import com.example.epmsys.ui.components.BottomBar
import com.example.epmsys.ui.components.Categories
import com.example.epmsys.ui.components.DropDownMenu
import com.example.epmsys.ui.components.FormTextField
import com.example.epmsys.ui.components.TopBar
import com.example.epmsys.ui.components.names
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.launch

const val STATE_TAG = "STATE_TAG"
const val IS_PHOTO_TAKEN = "PHOTO_TAKEN"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PointScreen(
    locationPermissions: MultiplePermissionsState,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
) {
    // Screen viewmodel and state
    val pointViewModel: PointViewModel = hiltViewModel()
    val pointUiState = pointViewModel.pointUiState.collectAsState().value
    val errors = pointUiState.errors
    val scope = rememberCoroutineScope()
    Log.d(STATE_TAG, pointUiState.pointDetails.toString())

    var startCamera by remember {
        mutableStateOf(false)
    }
    val isPhotoTaken by pointViewModel.isPhotoTaken.collectAsState()
    if (isPhotoTaken) {
        startCamera = false
        pointViewModel.resetIsPhotoTaken()
    }
    Log.d(IS_PHOTO_TAKEN, isPhotoTaken.toString())

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(id = R.string.new_point),
                    canNavigateUp = true,
                    navigateUp = navigateUp
                )
            },
            bottomBar = {
                BottomBar(
                    isValidEntry = pointUiState.isValidEntry,
                    onPictureClick = {
                        startCamera = true
                    },
                    onSavePointEntry = {
                        scope.launch {
                            pointViewModel.savePointEntry()
                            navigateUp()
                        }
                    }
                )
            },
            modifier = modifier
                .fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .windowInsetsPadding(
                        WindowInsets(
                            left = 8.dp,
                            top = 8.dp,
                            right = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DropDownMenu(
                    optionList = Categories.entries.names(),
                    label = R.string.category,
                    onClick = {
                        pointViewModel.updatePointUiState(
                            pointDetails = pointUiState.pointDetails.copy(
                                category = it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.containsKey("category"),
                    supportingText = {
                        if (errors.containsKey("category")) Text(
                            text = errors["category"]!!,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )

                DropDownMenu(
                    optionList = Actions.entries.names(),
                    label = R.string.action,
                    onClick = {
                        pointViewModel.updatePointUiState(
                            pointDetails = pointUiState.pointDetails.copy(
                                action = it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.containsKey("action"),
                    supportingText = {
                        if (errors.containsKey("action")) Text(
                            text = errors["action"]!!,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )

                if (pointUiState.pointDetails.dateTime.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FormTextField(
                            value = pointUiState.pointDetails.dateTime,
                            modifier = Modifier.weight(1F),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(text = "Date") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null
                                )
                            },
                            isError = errors.containsKey("dateTime"),
                            supportingText = {
                                if (errors.containsKey("dateTime")) Text(
                                    text = errors["dateTime"]!!,
                                    modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                        IconButton(onClick = pointViewModel::updateDate) {
                            Icon(imageVector = Icons.Default.Update, contentDescription = null)
                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoordinatesCard(
                        pointDetails = pointUiState.pointDetails,
                        modifier = Modifier.weight(1F),
                        isError = errors.containsKey("location"),
                        supportingText = {
                            if (errors.containsKey("location")) Text(
                                text = errors["location"]!!,
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (locationPermissions.allPermissionsGranted) {
                                    pointViewModel.getCurrentLocation()
                                }
                            }
                        },
                        enabled = locationPermissions.allPermissionsGranted
                    ) {
                        Icon(imageVector = Icons.Default.AddLocationAlt, contentDescription = null)
                    }
                }


                pointUiState.pointDetails.photoUrl.let {
                    ImagePreview(
                        imageUri = it,
                        isError = errors.containsKey("photo"),
                        supportingText = {
                            if (errors.containsKey("photo")) Text(
                                text = errors["photo"]!!,
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }

            }
        }

        if (startCamera) {
            CameraPreview()
        }
    }
}


@Composable
fun CoordinatesCard(
    pointDetails: PointDetails,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Location",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Lat: ${pointDetails.latitude}, Long: ${pointDetails.longitude}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (isError) {
                supportingText?.invoke()
            }
        }
    }
}

@Composable
fun ImagePreview(
    imageUri: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Image",
            style = MaterialTheme.typography.labelLarge
        )
        if (isError) {
            supportingText?.invoke()
        }
        Spacer(modifier = modifier.height(8.dp))
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            imageLoader = ImageLoader(context),
            placeholder = painterResource(id = R.drawable.image_loading),
            error = painterResource(id = R.drawable.image_error)
        )
    }
}