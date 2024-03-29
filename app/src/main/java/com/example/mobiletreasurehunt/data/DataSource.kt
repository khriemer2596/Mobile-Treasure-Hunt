package com.example.mobiletreasurehunt.data

import com.example.mobiletreasurehunt.R

/* Assignment 6: Mobile Treasure Hunt
Kevin Riemer / riemerk@oregonstate.edu
CS 492 / Oregon State University
3/8/2024
*/

object DataSource {
    val clues = listOf(
        R.string.bean_clue,
        R.string.wrigley_clue
    )

    val hints = listOf(
        R.string.bean_hint,
        R.string.wrigley_hint
    )

    val solvedClues = listOf(
        R.string.bean_description,
        R.string.wrigley_description
    )

    val ClueLats = listOf<Double>(
        41.8826567,
        41.9484383,
    )

    val ClueLongs = listOf<Double>(
        -87.6233033,
        -87.6553317
    )

    val solutionImages = listOf(
        R.drawable.cloudgate800,
        R.drawable.wrigley_field
    )
}