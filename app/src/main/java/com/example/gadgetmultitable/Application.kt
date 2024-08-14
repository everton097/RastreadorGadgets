package com.example.gadgetmultitable

import android.app.Application
import com.example.gadgetmultitable.data.AppDatabase

class Application: Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}