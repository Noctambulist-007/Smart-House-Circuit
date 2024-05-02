package com.algostack.smartcircuithouse.services.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.algostack.smartcircuithouse.services.model.RoomData

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(roomData: RoomData)

    @Update
    suspend fun updateRoom(roomData: RoomData)

    @Query("SELECT * FROM rooms")
    suspend fun getAllRoomDataForBackup(): List<RoomData>

    @Query("SELECT * FROM rooms WHERE roomNo = :roomNo AND floorNo = :floorNo AND roomBuildingName = :buildingName")
    suspend fun getRoomByNumberAndFloorAndBuilding(
        roomNo: String,
        floorNo: String,
        buildingName: String
    ): RoomData?


    @Query("DELETE FROM rooms WHERE primaryKey = :primaryKey")
    suspend fun deleteRoomsForBuilding(primaryKey: String)

    @Query("UPDATE rooms SET buildingId = :buildingId, roomBuildingName = :buildingName, floorNo = :floorNumber, roomNo = :roomNumber, bedType = :bedType WHERE id = :roomId")
    suspend fun updateRoomDetails(
        roomId: String,
        buildingId: String,
        buildingName: String,
        floorNumber: kotlin.String,
        roomNumber: String,
        bedType: String
    )

    @Query("UPDATE rooms SET isBooked = 0 WHERE id = :roomId")
    suspend fun cancelRoomBooking(roomId: String)

    @Query("UPDATE rooms SET isBooked = 0, customerName = null, customerDetails = null, entryDate = null, exitDate = null WHERE id = :roomId")
    suspend fun cancelBooking(roomId: kotlin.String)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): LiveData<List<RoomData>>

//    @Query("SELECT * FROM rooms WHERE buildingId = :buildingId")
//    fun getRoomsForBuilding(buildingId: Int): LiveData<List<RoomData>>


    @Query("SELECT * FROM rooms WHERE primaryKey = :primaryKey")
    fun getRoomsForBuilding(primaryKey: String): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE bedType = :bedType")
    fun getRoomsByBedType(bedType: String): LiveData<List<RoomData>>


    @Query("SELECT * FROM rooms WHERE isBooked = 1")
    fun getBookedRooms(): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE isBooked = 0")
    fun getUnbookedRooms(): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE customerName = :customerName")
    fun getRoomsByCustomerName(customerName: String): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE customerDetails = :customerDetails")
    fun getRoomsByCustomerDetails(customerDetails: String): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE entryDate = :entryDate")
    fun getRoomsByEntryDate(entryDate: Long): LiveData<List<RoomData>>

    @Query("SELECT * FROM rooms WHERE exitDate = :exitDate")
    fun getRoomsByExitDate(exitDate: Long): LiveData<List<RoomData>>

    @Delete
    suspend fun delete(roomData: RoomData)

}

