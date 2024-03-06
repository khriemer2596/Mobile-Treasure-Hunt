package com.example.mobiletreasurehunt.model

/* Assignment 6: Mobile Treasure Hunt
Kevin Riemer / riemerk@oregonstate.edu
CS 492 / Oregon State University
3/8/2024
*/

data class HuntUiState(
    val clue: Int = 0,
    val clueSolved: Int = 0,
    val huntCompleted: Int = 0,
    val start: Int = 0,
    val hint: Int = 0,
    val name: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
)
