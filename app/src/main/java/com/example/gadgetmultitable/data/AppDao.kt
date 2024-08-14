package com.example.gadgetmultitable.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface AppDao {
    //Gadge
    @Query("SELECT * FROM gadget")
    fun getALlGadget(): Flow<List<Gadget>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllGadge(gadget: List<Gadget>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGadge(gadget: Gadget)

    @Update()
    suspend fun updateGadge(gadget: Gadget)

    @Delete()
    suspend fun deleteGadge(gadget: Gadget)

    //Accessory
    @Query("SELECT * FROM accessory")
    fun getALlAccessory(): Flow<List<Accessory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllAccessory(accessory: List<Accessory>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccessory(accessory: Accessory)

    @Update()
    suspend fun updateAccessory(accessory: Accessory)

    @Delete()
    suspend fun deleteAccessory(accessory: Accessory)

    // Relacionamentos

    // Recupera um gadget com seus acessórios
    @Query("SELECT * FROM gadget WHERE id = :gadgetId")
    fun getGadgetWithAccessories(gadgetId: Int): Flow<GadgetWithAccessory>

    // Recupera um acessório com seus gadgets (embora neste caso seja geralmente 1:1, pode haver casos específicos)
    @Query("SELECT * FROM accessory WHERE id = :accessoryId")
    fun getAccessoryWithGadgets(accessoryId: Int): Flow<AccessoryWithGadget>
}