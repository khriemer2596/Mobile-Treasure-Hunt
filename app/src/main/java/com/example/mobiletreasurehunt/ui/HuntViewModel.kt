package com.example.mobiletreasurehunt.ui

import androidx.lifecycle.ViewModel
import com.example.mobiletreasurehunt.model.HuntUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HuntViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HuntUiState())
    val uiState: StateFlow<HuntUiState> = _uiState.asStateFlow()

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
    }
}