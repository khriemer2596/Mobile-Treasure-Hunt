package com.example.mobiletreasurehunt.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.data.DataSource
import kotlin.math.exp

@Composable
fun ClueScreen(
    clueRef: (Int),
    onFoundItButtonClicked: (Int) -> Unit,
    onQuitButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(DataSource.clues[clueRef])
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Hint(
                clueRef = clueRef
            )
            FoundItButton(
                onClick = { onFoundItButtonClicked(clueRef) }
            )
            QuitButton(
                onClick = { onQuitButtonClicked() }
            )
        }
    }
}

@Composable
fun Hint(
    clueRef: (Int),
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        //elevation = ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .clickable(
                onClick = { expanded = !expanded }
            )
    ) {
        Column {
            Text(
                "Hint",
                //style =
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )
            if (expanded) {
                Text(
                    text = DataSource.hints[clueRef]
                )
            }
        }
    }
}

@Composable
fun FoundItButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text("Found It!")
    }
}

@Composable
fun QuitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text("Quit")
    }
}