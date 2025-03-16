package com.andredigital.catancounter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.internal.ClassProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Composable
@Preview
fun App(
    viewModel: CatanResourceViewModel = viewModel { CatanResourceViewModel() }
) {


    MaterialTheme {
        val cardStateMap: Map<String, CatanResourceItem> by viewModel.cardStateFlow.collectAsState()
        val statesList: List<CatanCardState> = remember(cardStateMap) {
            val cardStates = viewModel.toCardStates()
            cardStates
        }
        var attemptingToClear by remember {
            mutableStateOf(false)
        }
        var isAddDialogShowing by remember {
            mutableStateOf(false)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF121212))
        ) {
            Scaffold(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(color = Color(0xFF121212))
                    .fillMaxSize(),
                floatingActionButton = {
                    Row {
                        if (statesList.isNotEmpty()) {
                            FloatingActionButton(
                                onClick = { attemptingToClear = true },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Clear")
                            }
                        }
                        FloatingActionButton(
                            onClick = { isAddDialogShowing = true }
                        ) {
                            Icon(imageVector = Icons.Sharp.Add, contentDescription = null)
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(color = Color(0xFF121212))
                        .fillMaxSize()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(count = 1)
                    ) {
                        items(statesList, key = { state -> state.rollNumber }) { state ->
                            CatanCell(
                                state = state,
                                onDeleteItem = viewModel::removeResource,
                                onItemChanged = viewModel::storeCountUpdates
                            )
                        }
                    }

                    if (attemptingToClear) {
                        SureToClearDialog(
                            onAttemptingToClearChanged = { clear -> attemptingToClear = clear },
                            onYesClicked = viewModel::clearResources
                        )
                    }

                    if (isAddDialogShowing) {
                        CatanResourcePicker(
                            displayPicker = isAddDialogShowing,
                            onAddResource = { item ->
                                viewModel.addResource(item)
                            },
                            onDismiss = { isAddDialogShowing = false },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.SureToClearDialog(
    onAttemptingToClearChanged: (Boolean) -> Unit,
    onYesClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.Companion
            .align(Alignment.Center)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0xFF96939B))
            .padding(16.dp)
    ) {
        Text(
            text = "All resources will be cleared. Are you sure?",
            color = Color.Black
        )
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = {
                    onAttemptingToClearChanged(false)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = FloatingActionButtonDefaults.containerColor
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "No"
                )
            }
            Button(
                onClick = {
                    onYesClicked()
                    onAttemptingToClearChanged(false)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = FloatingActionButtonDefaults.containerColor
                )
            ) {
                Text(
                    text = "Yes"
                )
            }
        }
    }
}