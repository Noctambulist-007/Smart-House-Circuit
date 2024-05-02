package com.algostack.smartcircuithouse.services.db

import androidx.lifecycle.LiveData
import com.algostack.smartcircuithouse.services.model.RoomData

class RoomRepository(private val roomDao: RoomDao) {

    val allRooms: LiveData<List<RoomData>> = roomDao.getAllRooms()

    fun getRoomsForBuilding(buildingPrimaryKey: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsForBuilding(buildingPrimaryKey)
    }

    suspend fun insert(roomData: RoomData) {
        roomDao.insert(roomData)
    }

    suspend fun updateRoomStatus(roomData: RoomData) {
        roomDao.updateRoom(roomData)
    }

    suspend fun updateRoomDetails(roomId: String, buildingId: String, buildingName: String, floorNumber: String, roomNumber: String, bedType: String) {
        roomDao.updateRoomDetails(roomId, buildingId, buildingName, floorNumber, roomNumber, bedType)
    }

    suspend fun cancelRoomBooking(roomData: RoomData) {
        roomDao.cancelRoomBooking(roomData.id)
    }

    suspend fun cancelBooking(roomId: String) {
        roomDao.cancelBooking(roomId)
    }

    fun getRoomsByBedType(bedType: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsByBedType(bedType)
    }

    fun getBookedRooms(): LiveData<List<RoomData>> {
        return roomDao.getBookedRooms()
    }

    fun getUnbookedRooms(): LiveData<List<RoomData>> {
        return roomDao.getUnbookedRooms()
    }

    fun getRoomsByCustomerName(customerName: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsByCustomerName(customerName)
    }

    fun getRoomsByCustomerDetails(customerDetails: String): LiveData<List<RoomData>> {
        return roomDao.getRoomsByCustomerDetails(customerDetails)
    }

    fun getRoomsByEntryDate(entryDate: Long): LiveData<List<RoomData>> {
        return roomDao.getRoomsByEntryDate(entryDate)
    }

    fun getRoomsByExitDate(exitDate: Long): LiveData<List<RoomData>> {
        return roomDao.getRoomsByExitDate(exitDate)
    }

    suspend fun deleteRoom(roomData: RoomData) {
        roomDao.delete(roomData)
    }


}
