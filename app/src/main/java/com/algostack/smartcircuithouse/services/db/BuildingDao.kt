package com.algostack.smartcircuithouse.services.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.algostack.smartcircuithouse.services.model.BuildingData

@Dao
interface BuildingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(building: BuildingData)

    @Query("SELECT * FROM buildings")
    fun getAllBuildings(): LiveData<List<BuildingData>>

//    @Query("DELETE FROM buildings WHERE id = :buildingId")
//    suspend fun delete(buildingId: kotlin.Int)

    @Query("DELETE FROM buildings WHERE primaryKey = :primaryKey")
    suspend fun delete(primaryKey: String)

    @Query("SELECT * FROM buildings WHERE name = :buildingName")
    fun getBuildingByName(buildingName: String): BuildingData?


    @Query("SELECT * FROM buildings")
    suspend fun getAllBuildingsForBackup(): List<BuildingData>

    @Query("SELECT * FROM buildings WHERE id = :buildingId")
    suspend fun getBuildingById(buildingId: String): BuildingData


}
