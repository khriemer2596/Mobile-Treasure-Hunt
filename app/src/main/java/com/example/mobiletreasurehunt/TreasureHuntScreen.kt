package com.example.mobiletreasurehunt

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobiletreasurehunt.data.DataSource
import com.example.mobiletreasurehunt.model.HuntUiState
import com.example.mobiletreasurehunt.ui.ClueScreen
import com.example.mobiletreasurehunt.ui.ClueSolvedScreen
import com.example.mobiletreasurehunt.ui.CompletedScreen
import com.example.mobiletreasurehunt.ui.HuntViewModel
import com.example.mobiletreasurehunt.ui.StartScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.delay

/* Assignment 6: Mobile Treasure Hunt
Kevin Riemer / riemerk@oregonstate.edu
CS 492 / Oregon State University
3/8/2024
*/

enum class HuntScreen(var title: String) {
    Start(title = R.string.treasure_hunt_start_page_title.toString()),
    Clue(title = R.string.clues.toString()),
    ClueSolved(title = R.string.clue_solved_title.toString()),
    CompletePage(title = R.string.treasure_hunt_completed_title.toString())
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreasureHuntAppBar(
    uiState: HuntUiState,
    currentScreen: HuntScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.clue != 0) { // Case where a clue has been solved
        if (uiState.clueSolved == 1) {
            currentScreen.title = stringResource(R.string.clue)
        }
        else {currentScreen.title = stringResource(R.string.treasure_hunt_completed)
        }

    }

    else if (uiState.clueSolved == 1) { // Clue #1 has been solved
        currentScreen.title = stringResource(R.string.clue_solved)
    }

    else if (uiState.start == 1) { // User has started the treasure hunt
        currentScreen.title = stringResource(R.string.clue)
    }

    else {
        currentScreen.title = stringResource(R.string.treasure_hunt_start)
    }

    TopAppBar(
        title = {
            Text(currentScreen.title)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@SuppressLint("MissingPermission")
@Composable
fun TreasureHuntApp(
    viewModel: HuntViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = HuntScreen.valueOf(
        backStackEntry?.destination?.route ?: HuntScreen.Start.name
    )

    val uiState by viewModel.uiState.collectAsState()
    var isTiming by remember { mutableStateOf(false) } // State variable for timer
    var isReset by remember { mutableStateOf(false) } // State variable for resetting timer

    Scaffold(
        topBar = {
            TreasureHuntAppBar(
                uiState = uiState,
                currentScreen = currentScreen,
                canNavigateBack =
                    navController.previousBackStackEntry != null &&
                            currentScreen.title != stringResource(id = R.string.treasure_hunt_start),
                navigateUp = { // Do not want back arrow available on Start page
                    if (uiState.clueSolved == 2) {
                        viewModel.updateClueSolved(1)
                        navController.navigateUp()
                    }

                    else if (uiState.clueSolved == 1) {
                        viewModel.updateClueSolved(0)
                        navController.navigateUp()
                    }

                    else if (uiState.clue != 0) {
                        viewModel.updateClue(0)
                        navController.navigateUp()
                    }

                    else if (uiState.start != 0) {
                        viewModel.updateStart(0)
                        navController.navigateUp()
                    }

                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HuntScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = HuntScreen.Start.name) {
                StartScreen(
                    onButtonClicked = {
                        viewModel.updateStart(it)
                        isTiming = true // Start timer
                        isReset = false // Timer not reset
                        navController.navigate(HuntScreen.Clue.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = HuntScreen.Clue.name) {
                val showPopUp = remember { mutableStateOf(false) }
                ClueNotFound(showPopUp = showPopUp)
                ClueScreen(
                    clueRef = uiState.clue,
                    onFoundItButtonClicked = {
                        if ((uiState.lat == DataSource.ClueLats[0 + uiState.clue]) &&
                            (uiState.long == DataSource.ClueLongs[0 + uiState.clue])
                        ) { // Check if current location lat/long matches clue lat/long
                            viewModel.updateClueSolved(it + 1)
                            if (uiState.huntCompleted == 1) { // Check it hunt is completed
                                isTiming = false
                                navController.navigate(HuntScreen.CompletePage.name)
                            }
                            else { // Otherwise go to Clue Solved Screen
                                navController.navigate(HuntScreen.ClueSolved.name)
                            }
                        }
                        else { // Show pop up notifying user that they are at the wrong location
                            showPopUp.value = true
                        }
                    },
                    onQuitButtonClicked = {
                        viewModel.resetHunt()
                        isTiming = false // Stop timing
                        isReset = true // Reset timer to 0
                        navController.navigate(HuntScreen.Start.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = HuntScreen.ClueSolved.name) {
                ClueSolvedScreen(
                    clueRef = uiState.clue,
                    onButtonClicked = {
                        viewModel.updateClue(it)
                        navController.navigate(HuntScreen.Clue.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = HuntScreen.CompletePage.name) {
                CompletedScreen(
                    clueRef = uiState.clue,
                    onButtonClicked = {
                        viewModel.resetHunt()
                        isReset = true // Reset timer to 0
                        navController.navigate(HuntScreen.Start.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
        Timer(isTiming, isReset)
    }
}


@Composable
fun ClueNotFound(showPopUp: MutableState<Boolean>, modifier: Modifier = Modifier) {
    if (showPopUp.value) {
        AlertDialog(
            title = {
                Text(text = stringResource(R.string.incorrect_location_alert_title))
            },
            text = {
                Text(
                    text = stringResource(R.string.incorrect_location_alert_body)
                )
            },
            onDismissRequest = {
                showPopUp.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPopUp.value = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPopUp.value = false
                    }
                ) {
                    Text(stringResource(R.string.dismiss))
                }
            }
        )
    }
}

@Composable
fun Timer (
    isTiming: Boolean,
    isReset: Boolean,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(isTiming) {
        while(isTiming) {
            delay(1000)
            currentTime++
        }
    }
    if (isReset) {
        currentTime = 0L
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.treasure_hunt_elapsed_time))
        Text(currentTime.displayTime())
    }
}

// Function to format the elapsed time into a more readable way
fun Long.displayTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}