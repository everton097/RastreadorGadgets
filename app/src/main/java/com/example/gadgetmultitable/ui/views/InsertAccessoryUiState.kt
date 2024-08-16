package com.example.gadgetmultitable.ui.views

import java.util.Date

data class InsertAccessoryUiState (
    val name: String = "",
    val type: String = "",
    val gadgetId: Int, // Chave estrangeira para GadgetEntity
    val purchaseDate: Date,
    val price: Double,
    val notes: String
)