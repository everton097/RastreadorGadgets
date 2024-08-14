package com.example.gadgetmultitable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gadgetmultitable.data.Accessory
import com.example.gadgetmultitable.data.AccessoryWithGadget
import com.example.gadgetmultitable.data.AppDao
import com.example.gadgetmultitable.data.Gadget
import com.example.gadgetmultitable.data.GadgetWithAccessory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private  val appDao: AppDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
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
    val gadgetWithAccessory: Flow<GadgetWithAccessory> =
        gadgetId.flatMapLatest { id ->
            appDao.getGadgetWithAccessories(id)
        }

    val accessoryWithGadget: Flow<AccessoryWithGadget> =
        accessoryId.flatMapLatest { id ->
            appDao.getAccessoryWithGadgets(id)
        }

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
}
