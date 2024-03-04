package com.example.mobiletreasurehunt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletreasurehunt.model.HuntUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/* Assignment 6: Mobile Treasure Hunt
Kevin Riemer / riemerk@oregonstate.edu
CS 492 / Oregon State University
3/8/2024
*/

class HuntViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HuntUiState())
    val uiState: StateFlow<HuntUiState> = _uiState.asStateFlow()

    private val _lat = MutableStateFlow(HuntUiState())
    val lat: StateFlow<HuntUiState> = _lat.asStateFlow()

    private val _long = MutableStateFlow(HuntUiState())
    val long: StateFlow<HuntUiState> = _long.asStateFlow()

    fun updateLat(newLat: Double) {
        _uiState.update { currentState ->
            currentState.copy(lat = newLat)
        }
    }

    fun updateLong(newLong: Double) {
        _uiState.update { currentState ->
            currentState.copy(long = newLong)
        }
    }

    fun updateClue(clueNum: Int) {
        _uiState.update { currentState ->
            currentState.copy(clue = clueNum)
        }
    }

    fun updateStart(startNum: Int) {
        _uiState.update { currentState ->
            currentState.copy(start = startNum)
        }
    }

    fun updateClueSolved(clueSolvedNum: Int) {
        _uiState.update { currentState ->
            currentState.copy(clueSolved = clueSolvedNum)
        }
        if (clueSolvedNum == 1) {
            updateHuntCompleted()
        }
    }

    private fun updateHuntCompleted() {
        _uiState.update { currentState ->
            currentState.copy(huntCompleted = 1)
        }
    }

    fun updateSecondClue() {
        _uiState.update { currentState ->
            currentState.copy(secondClue = true)
        }
    }

    fun resetHunt() {
        _uiState.update { currentState ->
            currentState.copy(huntCompleted = 0)
        }

        _uiState.update { currentState ->
            currentState.copy(clue = 0)
        }

        _uiState.update { currentState ->
            currentState.copy(start = 0)
        }

        _uiState.update { currentState ->
            currentState.copy(clueSolved = 0)
        }

        _uiState.update { currentState ->
            currentState.copy(secondClue = false)
        }
    }
}