package com.example.gadgetmultitable.ui.views

import java.util.Date

data class InsertGadgetUiState (
    val name: String = "",
    val brand: String = "",
    val model: String = "",
    val purchaseDate: Date = Date(),
    val price: Double = 0.0,
    val specifications: String = "",
    val status: String = "",
)