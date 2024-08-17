package com.example.gadgetmultitable.ui.views

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gadgetmultitable.R
import com.example.gadgetmultitable.data.Accessory
import com.example.gadgetmultitable.data.Gadget
import com.example.gadgetmultitable.data.GadgetWithAccessory
import com.example.gadgetmultitable.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier, paddingValues: PaddingValues){
    val viewModel : AppViewModel = viewModel(factory = AppViewModel.Factory)
    val gadgets by viewModel.gadgets.collectAsState()
    val accessories by viewModel.accessories.collectAsState()
    val appUiState by viewModel.appUiState.collectAsState()
    val gadgetWithAccessories by viewModel.gadgetWithAccessory.collectAsState()
    val navController = rememberNavController()

    //viewModel.createAccessory()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row {
                    // Dropdown for Gadget Selection
                    var expanded by remember { mutableStateOf(false) }
                    Text(text = stringResource(id = appUiState.title))
                    Spacer(modifier = modifier.weight(1f))
                    if (appUiState.optionsEnable){
                        Box(
                            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_more_vert_24),
                                contentDescription = "options",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        expanded = true
                                    }
                            )
                            Column(modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 4.dp)) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .wrapContentSize(Alignment.TopEnd)
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            viewModel.EditOption(navController)
                                        },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.baseline_edit_24),
                                                    contentDescription = "Edit",
                                                    modifier = Modifier
                                                        .padding(end = 8.dp)
                                                )
                                                Text(text = "Editar")
                                            }
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            viewModel.DeleteOption(navController)
                                        },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                                                    contentDescription = "Delete",
                                                    modifier = Modifier
                                                        .padding(end = 8.dp)
                                                )
                                                Text(text = "Delete")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            })
        },
        floatingActionButton = {
            if(appUiState.floatingActionButtonEnable){
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
                    navController = navController,
                    viewModel = viewModel,
                    onGadgetSelection = viewModel::selectGadgets,
                )
            }
            composable(route = AppScreens.InsertGadget.name) {
                InsertGadget(navController = navController, viewModel = viewModel)
            }
            composable(route = AppScreens.GadgetDetails.name) {
                GadgetDetails(
                    navController = navController,
                    viewModel = viewModel,
                    gadgetWithAccessories = gadgetWithAccessories,
                )
            }
            composable(route = AppScreens.GadgetEdition.name) {
                GadgetEdition(navController = navController, viewModel = viewModel)
            }
            composable(route = AppScreens.InsertAccessory.name) {
                InsertAccessory(
                   gadgets = gadgets,
                    navController = navController,
                    onGadgetSelection = viewModel::selectGadgets,
                    viewModel = viewModel
                )
            }
            composable(route = AppScreens.AccessoryDetails.name ) {
                val accessory = viewModel.accessoryDetailsOption()
                AccessoryDetails(
                    navController = navController,
                    viewModel = viewModel,
                    accessory = accessory,
                )
            }
            composable(route = AppScreens.AccessoryEdition.name) {
                AccessoryEdition(navController = navController, viewModel = viewModel)
            }
        }
    }
}

enum class AppScreens {
    GadgetList,
    GadgetDetails,
    GadgetEdition,
    InsertGadget,
    InsertAccessory,
    AccessoryDetails,
    AccessoryEdition
}

@Composable
fun GadgetList(
    modifier: Modifier = Modifier,
    gadgets: List<Gadget>,
    navController: NavController,
    viewModel: AppViewModel,
    onGadgetSelection: (Gadget) -> Unit,
){
    Row(
        modifier
            .fillMaxSize()
            .padding(2.dp)) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
        ) {
            LazyColumn {
                items(gadgets) { gadget ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .clickable {
                            onGadgetSelection(gadget)
                            viewModel.navigate(navController)
                        }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceTint)
                        ) {
                            Column(modifier = modifier.padding(8.dp)) {
                                Text(
                                    text = gadget.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    fontStyle = FontStyle.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GadgetDetails(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AppViewModel,
    gadgetWithAccessories: GadgetWithAccessory?
) {
    BackHandler() {
        viewModel.navigateBack(navController)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)) {
        //Text(text = gadgetWithAccessories!!.gadget.name)
        Text(text = "Name: ${gadgetWithAccessories!!.gadget.name}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Brand: ${gadgetWithAccessories!!.gadget.brand}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Model: ${gadgetWithAccessories!!.gadget.model}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Purchase Date: ${SimpleDateFormat("dd/MM/yyyy").format(gadgetWithAccessories!!.gadget.purchaseDate)}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Price: ${gadgetWithAccessories!!.gadget.price}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Specifications: ${gadgetWithAccessories!!.gadget.specifications}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Status: ${gadgetWithAccessories!!.gadget.status}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(gadgetWithAccessories!!.accessories){ accessory ->
                Card(modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .clickable {
                        viewModel.selectAccessory(navController,accessory)
                    }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = modifier.padding(6.dp)) {
                            Text(text = accessory.name, fontWeight = FontWeight.Normal, fontSize = 14.sp, fontStyle = FontStyle.Normal)
                        }
                    }
                }
            }
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

    // Variável que controla a exibição do DatePickerDialog
    val context = LocalContext.current
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

@Composable
fun GadgetEdition(
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

    // Variável que controla a exibição do DatePickerDialog
    val context = LocalContext.current
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

@Composable
fun InsertAccessory(
    modifier: Modifier = Modifier,
    gadgets: List<Gadget>,
    navController: NavController,
    viewModel: AppViewModel,
    onGadgetSelection: (Gadget) -> Unit
) {
    BackHandler {
        viewModel.navigateBack(navController)
    }
    val uiAccessoryState by viewModel.insertAccessoryScreenUiState.collectAsState()
    // Para evitar terque apagar valor inicial
    val priceAccessoryInput by viewModel.priceAccessoryInput.collectAsState()

    // Variável que controla a exibição do DatePickerDialog
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = uiAccessoryState.purchaseDate
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
            viewModel.onAccessoryPurchaseDateChange(selectedDate)
        },
        year, month, day
    )
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(uiAccessoryState.purchaseDate)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            value = uiAccessoryState.name,
            onValueChange = viewModel::onAccessoryName,
            label = { Text(text = "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiAccessoryState.type,
            onValueChange = viewModel::onAccessoryType,
            label = { Text(text = "Type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date Picker for Purchase Date (Simplified for now)
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
            value = priceAccessoryInput,
            onValueChange = viewModel::onAccessoryPriceChange,
            label = { Text(text = "Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiAccessoryState.notes,
            onValueChange = viewModel::onAccessoryNotes,
            label = { Text(text = "Notes") },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AccessoryDetails(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AppViewModel,
    accessory: Accessory?,
) {
    BackHandler() {
        viewModel.navigateBack(navController)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)) {
        if (accessory == null) {
            // Exibe uma mensagem de erro ou navega para outra tela
            Text("Accessory not found")
        } else {
            //Text(text = gadgetWithAccessories!!.gadget.name)
            Text(text = "Name: ${accessory.name}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type: ${accessory.type}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Purchase Date: ${SimpleDateFormat("dd/MM/yyyy").format(accessory.purchaseDate)}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Price: ${accessory.price}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Notes: ${accessory.notes}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun AccessoryEdition(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AppViewModel,
) {
    BackHandler {
        viewModel.navigateBack(navController)
    }
    val uiAccessoryState by viewModel.insertAccessoryScreenUiState.collectAsState()
    // Para evitar terque apagar valor inicial
    val priceAccessoryInput by viewModel.priceAccessoryInput.collectAsState()

    // Variável que controla a exibição do DatePickerDialog
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = uiAccessoryState.purchaseDate
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
            viewModel.onAccessoryPurchaseDateChange(selectedDate)
        },
        year, month, day
    )
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(uiAccessoryState.purchaseDate)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            value = uiAccessoryState.name,
            onValueChange = viewModel::onAccessoryName,
            label = { Text(text = "Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiAccessoryState.type,
            onValueChange = viewModel::onAccessoryType,
            label = { Text(text = "Type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date Picker for Purchase Date (Simplified for now)
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
            value = priceAccessoryInput,
            onValueChange = viewModel::onAccessoryPriceChange,
            label = { Text(text = "Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiAccessoryState.notes,
            onValueChange = viewModel::onAccessoryNotes,
            label = { Text(text = "Notes") },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
