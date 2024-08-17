package com.example.gadgetmultitable.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(
    tableName = "accessory",
    foreignKeys = [
        ForeignKey(
            entity = Gadget::class,
            parentColumns = ["id"],
            childColumns = ["gadgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Accessory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val gadgetId: Int,
    val purchaseDate: Date,
    val price: Double,
    val notes: String
)
