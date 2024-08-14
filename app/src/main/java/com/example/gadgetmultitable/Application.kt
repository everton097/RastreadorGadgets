package com.example.gadgetmultitable

import com.example.gadgetmultitable.data.AppDatabase

class Application {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}