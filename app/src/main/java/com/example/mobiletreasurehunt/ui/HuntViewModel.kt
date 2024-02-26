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
    }

    fun updateHint(hintNum: Int) {
        _uiState.update { currentState ->
            currentState.copy(hint = hintNum)
        }
    }
}