package com.example.gadgetmultitable.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(tableName = "gadget")
data class Gadget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val model: String,
    val purchaseDate: Date,
    val price: Double,
    val specifications: String,
    val status: String
)

data class GadgetWithAccessory(
    @Embedded val gadget: Gadget,
    @Relation(
        parentColumn = "id",  // Refere-se à coluna "gadget_id" em Gadget
        entityColumn = "id",   // Refere-se à coluna "gadgetId" em Accessory
        associateBy = Junction(
            Accessory::class,         // Junction deve ser com a entidade Accessory
            parentColumn = "id", // Refere-se à coluna "gadget_id" em Gadget
            entityColumn = "id"   // Refere-se à coluna "gadgetId" em Accessory
        )
    )
    val accessories: List<Accessory>
)
