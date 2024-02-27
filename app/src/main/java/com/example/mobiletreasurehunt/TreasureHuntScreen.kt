package com.example.mobiletreasurehunt

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.MobileTreasureHuntTheme
import com.example.mobiletreasurehunt.model.HuntUiState
import com.example.mobiletreasurehunt.ui.ClueScreen
import com.example.mobiletreasurehunt.ui.ClueSolvedScreen
import com.example.mobiletreasurehunt.ui.CompletedScreen
import com.example.mobiletreasurehunt.ui.HuntViewModel
import com.example.mobiletreasurehunt.ui.StartScreen

enum class HuntScreen(var title: String) {
    Start(title = "Treasure Hunt Start"),
    Clue(title = "Clues"),
    ClueSolved(title = "Clue Solved"),
    CompletePage(title = "Treasure Hunt Completed")
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
    if (uiState.clue != 0) {
        if (uiState.clueSolved == 1) {
            currentScreen.title = stringResource(R.string.clue)
        }
        else {currentScreen.title = stringResource(R.string.treasure_hunt_completed)
        }

    }

    else if (uiState.clueSolved == 1) {
        currentScreen.title = stringResource(R.string.clue_solved)
    }

    else if (uiState.start == 1) {
        currentScreen.title = stringResource(R.string.clue)
    }

    else {currentScreen.title = stringResource(R.string.treasure_hunt_start)
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

    Scaffold(
        topBar = {
            TreasureHuntAppBar(
                uiState = uiState,
                currentScreen = currentScreen,
                canNavigateBack =
                    navController.previousBackStackEntry != null &&
                            currentScreen.title != stringResource(id = R.string.treasure_hunt_start),
                navigateUp = {
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
                        navController.navigate(HuntScreen.Clue.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = HuntScreen.Clue.name) {
                ClueScreen(
                    clueRef = uiState.clue,
                    onFoundItButtonClicked = {
                        viewModel.updateClueSolved(it + 1)
                        if (uiState.huntCompleted == 1) {
                            navController.navigate(HuntScreen.CompletePage.name)
                        }
                        else {
                            navController.navigate(HuntScreen.ClueSolved.name)
                        }
                    },
                    onQuitButtonClicked = {
                        viewModel.resetHunt()
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
                    onButtonClicked = {
                        viewModel.resetHunt()
                        navController.navigate(HuntScreen.Start.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}