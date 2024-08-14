package com.example.gadgetmultitable.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gadgetmultitable.data.Accessory
import com.example.gadgetmultitable.data.Gadget
import com.example.gadgetmultitable.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier, paddingValues: PaddingValues){
    val viewModel : AppViewModel = viewModel(factory = AppViewModel.Factory)
    val gadgets by viewModel.gadgets.collectAsState()
    val accessories by viewModel.accessories.collectAsState()
    val uiState by viewModel.appUiState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = uiState.title))
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.navigate(navController = navController)
            }) {
                Image(
                    painter = painterResource(id = uiState.fabIcon),
                    contentDescription = stringResource(
                        id = uiState.iconContentDescription
                    )
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = AppScreens.GadgetList.name,
            modifier = modifier.padding(it)
        ) {
            composable(route = AppScreens.GadgetList.name) {
                GadgetList(
                    gadgets = gadgets,
                    accessory = accessories,
                    navController = navController,
                    onGadgetSelection = viewModel::selectGadgets,
                    onAccessorySelection = viewModel::selectAccessory
                )
            }
            composable(route = AppScreens.InsertGadget.name) {
                //InsertTask(viewModel = viewModel, navController = navController)
            }
        }
    }
}

enum class AppScreens {
    GadgetList,
    InsertGadget,
}

@Composable
fun GadgetList(
    modifier: Modifier = Modifier,
    gadgets: List<Gadget>,
    accessory: List<Accessory>,
    navController: NavController,
    onGadgetSelection: (Gadget) -> Unit,
    onAccessorySelection: (Accessory) -> Unit,
){
    Row(modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5F)) {
            Text(text = "Gadgets")
            LazyColumn {
                items(gadgets) { gadget ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .clickable {
                            onGadgetSelection(gadget)
                            //navController.navigate("movie")
                        }) {
                        Text(text = gadget.name)
                    }
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
            Text(text = "Actors")
            LazyColumn {
                items(accessory) { Accessory ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .clickable {
                            onAccessorySelection(Accessory)
                            //navController.navigate("actor")
                        }) {
                        Text(text = Accessory.name)
                    }
                }
            }
        }
    }
}
