package com.example.mobiletreasurehunt.model

data class HuntUiState(
    val clue: Int = 0,
    val clueSolved: Int = 0,
    val huntCompleted: Int = 0,
    val start: Int = 0,
    val hint: Int = 0,
    val name: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val secondClue: Boolean = false,
)
