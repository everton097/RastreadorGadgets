package com.example.gadgetmultitable.ui.views

import android.app.DatePickerDialog
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gadgetmultitable.data.Accessory
import com.example.gadgetmultitable.data.Gadget
import com.example.gadgetmultitable.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier, paddingValues: PaddingValues){
    val viewModel : AppViewModel = viewModel(factory = AppViewModel.Factory)
    val gadgets by viewModel.gadgets.collectAsState()
    val accessories by viewModel.accessories.collectAsState()
    val appUiState by viewModel.appUiState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = appUiState.title))
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.navigate(navController = navController)
            }) {
                Image(
                    painter = painterResource(id = appUiState.fabIcon),
                    contentDescription = stringResource(
                        id = appUiState.iconContentDescription
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
                InsertGadget(navController = navController, viewModel = viewModel)
                // InsertAccessory(
                //   gadgets = gadgets,
                //    navController = navController,
                //    onGadgetSelection = viewModel::selectGadgets,
                //    viewModel = viewModel
                //)
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
    Row(
        modifier
            .fillMaxSize()
            .padding(2.dp)) {
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
            Text(text = "Accessories")
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

@Composable
fun InsertAccessory2(
    modifier: Modifier = Modifier,
    gadgets: List<Gadget>,
    accessory: List<Accessory>,
    navController: NavController,
    onGadgetSelection: (Gadget) -> Unit,
    onAccessorySelection: (Accessory) -> Unit,
    viewModel: AppViewModel
){
    BackHandler() {
        viewModel.navigateBack(navController)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(value = "",
            onValueChange = {  },
            label = { Text(text = "Name") },
            singleLine = true,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = modifier.height(8.dp))
        TextField(value = "",
            onValueChange = {},
            label = { Text(text = "Notes") },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
fun InsertAccessory3(
    modifier: Modifier = Modifier,
    gadgets: List<Gadget>,
    navController: NavController,
    viewModel: AppViewModel,
    onGadgetSelection: (Gadget) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var selectedGadget by remember { mutableStateOf<Gadget?>(null) }
    val purchaseDate by remember { mutableStateOf(Date()) }
    var price by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.navigateBack(navController)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth() // Usar Modifier diretamente em vez de 'modifier'
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text(text = "Type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Button to trigger DropdownMenu
        Button(
            onClick = { isDropdownExpanded = !isDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedGadget?.name ?: "Select Gadget")
        }

        // Dropdown for Gadget Selection
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            gadgets.forEach { gadget ->
                DropdownMenuItem(
                    onClick = {
                        selectedGadget = gadget
                        onGadgetSelection(gadget)
                        isDropdownExpanded = false
                    },
                    text = {
                        Text(text = gadget.name)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Date Picker for Purchase Date (Simplified for now)
        TextField(
            value = purchaseDate.toString(), // Placeholder
            onValueChange = { /* No-op, Date picker would update this */ },
            label = { Text(text = "Purchase Date") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Date would be selected via a dialog, not manually entered
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text(text = "Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(text = "Notes") },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Save Accessory to the database
                selectedGadget?.let { gadget ->
                    viewModel.insertAccessory(
                        Accessory(
                            name = name,
                            type = type,
                            gadgetId = gadget.id,
                            purchaseDate = purchaseDate,
                            price = price.toDoubleOrNull() ?: 0.0,
                            notes = notes
                        )
                    )
                }
                navController.navigateUp()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Save")
        }
    }
}


@Composable
fun InsertAccessory(
    modifier: Modifier = Modifier,
    gadgets: List<Gadget>,
    navController: NavController,
    viewModel: AppViewModel,
    onGadgetSelection: (Gadget) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var selectedGadget by remember { mutableStateOf<Gadget?>(null) }
    val purchaseDate by remember { mutableStateOf(Date()) }
    var price by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.navigateBack(navController)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text(text = "Type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Box to manage DropdownMenu visibility
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { isDropdownExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = selectedGadget?.name ?: "Select Gadget")
            }

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                gadgets.forEach { gadget ->
                    DropdownMenuItem(
                        onClick = {
                            selectedGadget = gadget
                            onGadgetSelection(gadget)
                            isDropdownExpanded = false
                        },
                        text = {
                            Text(text = gadget.name)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Date Picker for Purchase Date (Simplified for now)
        TextField(
            value = purchaseDate.toString(), // Placeholder
            onValueChange = { /* No-op, Date picker would update this */ },
            label = { Text(text = "Purchase Date") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Date would be selected via a dialog, not manually entered
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text(text = "Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(text = "Notes") },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Save Accessory to the database
                selectedGadget?.let { gadget ->
                    viewModel.insertAccessory(
                        Accessory(
                            name = name,
                            type = type,
                            gadgetId = gadget.id,
                            purchaseDate = purchaseDate,
                            price = price.toDoubleOrNull() ?: 0.0,
                            notes = notes
                        )
                    )
                }
                navController.navigateUp()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun InsertGadget(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AppViewModel
) {
    BackHandler {
        viewModel.navigateBack(navController)
    }
    val uiState by viewModel.insertGadgetScreenUiState.collectAsState()
    // Para evitar terque apagar valor inicial
    val priceInput by viewModel.priceInput.collectAsState()

    val context = LocalContext.current

    // Variável que controla a exibição do DatePickerDialog
    val calendar = Calendar.getInstance()
    calendar.time = uiState.purchaseDate

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            // Converte a data selecionada para um objeto Date
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }.time
            // Chama a função para atualizar o estado
            viewModel.onGadgetPurchaseDateChange(selectedDate)
        },
        year, month, day
    )

    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(uiState.purchaseDate)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            value = uiState.name,
            onValueChange = viewModel::onGadgetNameChange,
            label = { Text(text = "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.brand,
            onValueChange = viewModel::onGadgetBrandChange,
            label = { Text(text = "Brand") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.model,
            onValueChange = viewModel::onGadgetModelChange,
            label = { Text(text = "Model") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Box para tornar o TextField clicável
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    datePickerDialog.show() // Mostra o DatePickerDialog quando o Box é clicado
                }
        ) {
            TextField(
                value = formattedDate, // Exibe a data formatada
                onValueChange = { /* No-op */ }, // Não faz nada porque o DatePicker controla o valor
                label = { Text(text = "Purchase Date") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = false, // Impede edição direta do campo
                readOnly = true // Evita que o usuário digite manualmente
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = priceInput,
            onValueChange = viewModel::onGadgetPriceChange,
            label = { Text(text = "Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.specifications,
            onValueChange = viewModel::onGadgetSpecifications,
            label = { Text(text = "Specifications") },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.status,
            onValueChange = viewModel::onGadgetStatus,
            label = { Text(text = "Status") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


