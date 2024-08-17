package com.example.gadgetmultitable.ui.views

import java.util.Date

data class InsertAccessoryUiState (
    val name: String = "",
    val type: String = "",
    val gadgetId: Int = 0,
    val purchaseDate: Date = Date(),
    val price: Double = 0.0,
    val notes: String = ""
)