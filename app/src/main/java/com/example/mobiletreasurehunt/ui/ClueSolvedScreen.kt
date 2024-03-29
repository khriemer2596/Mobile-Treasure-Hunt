package com.example.mobiletreasurehunt.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.data.DataSource

/* Assignment 6: Mobile Treasure Hunt
Kevin Riemer / riemerk@oregonstate.edu
CS 492 / Oregon State University
3/8/2024
*/

@Composable
fun ClueSolvedScreen(
    clueRef: (Int),
    onButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = DataSource.solutionImages[0]),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(DataSource.solvedClues[clueRef]))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            ContinueButton(
                onClick = {
                    onButtonClicked(clueRef + 1)
                }
            )
        }
    }
}

@Composable
fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = dimensionResource(id = R.dimen.large_divider))
    ) {
        Text(stringResource(R.string.continue_text))
    }
}