package com.example.gadgetmultitable.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.gadgetmultitable.Application
import com.example.gadgetmultitable.R
import com.example.gadgetmultitable.data.Accessory
import com.example.gadgetmultitable.data.AppDao
import com.example.gadgetmultitable.data.Gadget
import com.example.gadgetmultitable.data.GadgetWithAccessory
import com.example.gadgetmultitable.ui.views.AppScreens
import com.example.gadgetmultitable.ui.views.AppUiState
import com.example.gadgetmultitable.ui.views.InsertAccessoryUiState
import com.example.gadgetmultitable.ui.views.InsertGadgetUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AppViewModel(
    private val appDao: AppDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var detailsGadgetScreen: Boolean = false
    private val _appUiState: MutableStateFlow<AppUiState> =
        MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> =
        _appUiState.asStateFlow()
    // Inserção de Gadget
    private var _insertGadgetUiState: MutableStateFlow<InsertGadgetUiState> = MutableStateFlow(
        InsertGadgetUiState()
    )
    val insertGadgetScreenUiState: StateFlow<InsertGadgetUiState> =
        _insertGadgetUiState.asStateFlow()
    fun onGadgetNameChange(newGadgetName: String) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(name = newGadgetName)
        }
    }
    fun onGadgetBrandChange(newGadgetBrand: String) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(brand = newGadgetBrand)
        }
    }
    fun onGadgetModelChange(newGadgetModel: String) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(model = newGadgetModel)
        }
    }
    fun onGadgetPurchaseDateChange(newGadgetPurchaseDate: Date) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(purchaseDate = newGadgetPurchaseDate)
        }
    }
    private val _priceInput = MutableStateFlow("")
    val priceInput: StateFlow<String> = _priceInput
    fun onGadgetPriceChange(newPrice: String) {
        _priceInput.value = newPrice
        _insertGadgetUiState.update { currentState ->
            currentState.copy(price = newPrice.toDoubleOrNull() ?: 0.0)
        }
    }
    /*
    fun onGadgetPriceChange(newGadgetPrice: Double) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(price = newGadgetPrice)
        }
    }*/
    fun onGadgetSpecifications(newGadgetSpecifications: String) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(specifications = newGadgetSpecifications)
        }
    }
    fun onGadgetStatus(newGadgetStatus: String) {
        _insertGadgetUiState.update { currentState ->
            currentState.copy(status = newGadgetStatus)
        }
    }
    fun createAccessory(){
        viewModelScope.launch {
            appDao.insertAccessory(
                accessory = Accessory(
                    id = 0,
                    name = "",
                    type = "",
                    gadgetId = 1,
                    purchaseDate = Date(),
                    price = 200.0,
                    notes = "Accessory teste manual"
                )
            )
        }
    }

    //Inserção de Accessory
    private var _insertAccessoryUiState: MutableStateFlow<InsertAccessoryUiState> = MutableStateFlow(
        InsertAccessoryUiState()
    )
    val insertAccessoryScreenUiState: StateFlow<InsertAccessoryUiState> =
        _insertAccessoryUiState.asStateFlow()
    fun onAccessoryName(newAccessoryName: String) {
        _insertAccessoryUiState.update { currentState ->
            currentState.copy(name = newAccessoryName)
        }
    }
    fun onAccessoryType(newAccessoryType: String) {
        _insertAccessoryUiState.update { currentState ->
            currentState.copy(type = newAccessoryType)
        }
    }
    fun onAccessoryPurchaseDateChange(newAccessoryPurchaseDate: Date) {
        _insertAccessoryUiState.update { currentState ->
            currentState.copy(purchaseDate = newAccessoryPurchaseDate)
        }
    }
    private val _priceAccessoryInput = MutableStateFlow("")
    val priceAccessoryInput: StateFlow<String> = _priceInput
    fun onAccessoryPriceChange(newAccessoryPrice: String) {
        _priceAccessoryInput.value = newAccessoryPrice
        _insertAccessoryUiState.update { currentState ->
            currentState.copy(price = newAccessoryPrice.toDoubleOrNull() ?: 0.0)
        }
    }
    fun onAccessoryNotes(newAccessoryNotes: String) {
        _insertAccessoryUiState.update { currentState ->
            currentState.copy(notes = newAccessoryNotes)
        }
    }

    private var gadgetId = MutableStateFlow(0)
    private var accessoryId = MutableStateFlow(0)

    val gadgets: StateFlow<List<Gadget>> =
        appDao.getALlGadget()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf(),
            )
    val accessories: StateFlow<List<Accessory>> =
        appDao.getALlAccessory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf()
            )

    val gadgetWithAccessory: StateFlow<GadgetWithAccessory?> =
        gadgetId.flatMapLatest { id ->
            Log.d("logdebug", "Buscando acessórios para o gadget com ID: $id")
            appDao.getGadgetWithAccessories(id)
                .onEach { result ->
                    if (result.accessories.isNullOrEmpty()) {
                        Log.d("logdebug", "Nenhum acessório encontrado para o gadget com ID: $id")
                    } else {
                        Log.d("logdebug", "Acessórios encontrados: ${result.accessories}")
                    }
                }
                .catch { exception ->
                    Log.e("logdebug", "Erro ao buscar acessórios: ${exception.message}")
                    emit(
                        GadgetWithAccessory(
                            gadget = Gadget(
                                id = 0,
                                name = "",
                                brand = "",
                                model = "",
                                purchaseDate = Date(),
                                price = 0.0,
                                specifications = "",
                                status = ""
                            ),
                            accessories = emptyList()
                        )
                    ) // Emitir null se ocorrer um erro
                }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = null // Inicialmente null
            )

    fun insertGadget(gadget: Gadget){
        viewModelScope.launch {
            appDao.insertGadge(gadget)
        }
    }
    fun updateGadget(gadget: Gadget){
        viewModelScope.launch {
            appDao.updateGadge(gadget)
        }
    }
    fun deleteGadget(gadget: Gadget){
        viewModelScope.launch {
            appDao.deleteGadge(gadget)
        }
    }

    fun insertAccessory(accessory: Accessory){
        viewModelScope.launch {
            appDao.insertAccessory(accessory)
        }
    }
    fun updateAccessory(accessory: Accessory){
        viewModelScope.launch {
            appDao.updateAccessory(accessory)
        }
    }
    fun deleteAccessory(accessory: Accessory){
        viewModelScope.launch {
            appDao.deleteAccessory(accessory)
        }
    }

    fun selectGadgets(gadget: Gadget){
        detailsGadgetScreen= true
        Log.d("logdebug", "Entrou em selectGadgets")
        gadgetId.value = gadget.id
        /*_insertAccessoryUiState.update { currentState ->
            currentState.copy(gadgetId = gadget.id)
        }*/
    }

    fun selectAccessory(accessory: Accessory){
        accessoryId.value = accessory.id
    }

    fun navigate(navController: NavController) {
        if (_appUiState.value.title == R.string.gadget_list) {
            if (detailsGadgetScreen){
                _appUiState.update { currentState ->
                    currentState.copy(
                        title = R.string.gadget_details,
                        fabIcon = R.drawable.baseline_add_24,
                        iconContentDescription = R.string.gadget_details,
                    )
                }
                navController.navigate(AppScreens.GadgetDetails.name)
                detailsGadgetScreen = false
            } else{
                _appUiState.update { currentState ->
                    currentState.copy(
                        title = R.string.insert_new_gadget,
                        fabIcon = R.drawable.baseline_check_24,
                        iconContentDescription = R.string.insert_new_gadget,
                    )
                }
                navController.navigate(AppScreens.InsertGadget.name)
            }
        } else if (_appUiState.value.title == R.string.insert_new_gadget) {
            insertGadget(
                Gadget(
                    name = _insertGadgetUiState.value.name,
                    brand = _insertGadgetUiState.value.brand,
                    model = _insertGadgetUiState.value.model,
                    purchaseDate = _insertGadgetUiState.value.purchaseDate,
                    price = _insertGadgetUiState.value.price,
                    specifications = _insertGadgetUiState.value.specifications,
                    status = _insertGadgetUiState.value.status
                )
            )
            _appUiState.update { currentState ->
                currentState.copy(
                    title = R.string.gadget_list,
                    fabIcon = R.drawable.baseline_add_24,
                    iconContentDescription = R.string.gadget_list,
                )
            }
            navController.navigate(AppScreens.GadgetList.name)
        } else if (_appUiState.value.title == R.string.gadget_details){
            _appUiState.update { currentState ->
                currentState.copy(
                    title = R.string.insert_new_accessory,
                    fabIcon = R.drawable.baseline_check_24,
                    iconContentDescription = R.string.insert_new_accessory,
                )
            }
            navController.navigate(AppScreens.InsertAccessory.name)
        }else if (_appUiState.value.title == R.string.insert_new_accessory){
            insertAccessory(
                Accessory(
                    name = _insertAccessoryUiState.value.name,
                    type = _insertAccessoryUiState.value.type,
                    gadgetId = _insertAccessoryUiState.value.gadgetId,
                    purchaseDate = _insertAccessoryUiState.value.purchaseDate,
                    price = _insertAccessoryUiState.value.price,
                    notes = _insertAccessoryUiState.value.notes
                )
            )
            _appUiState.update { currentState ->
                currentState.copy(
                    title = R.string.insert_new_gadget,
                    fabIcon = R.drawable.baseline_add_24,
                    iconContentDescription = R.string.insert_new_gadget,
                )
            }
            navController.navigate(AppScreens.GadgetList.name)
        }
    }

    fun navigateBack(navController: NavController){
        _appUiState.update {
            AppUiState()
        }
        navController.popBackStack()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ) :T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                return AppViewModel(
                    (application as Application).database.appDao(),
                    savedStateHandle,
                ) as T
            }
        }
    }
}
