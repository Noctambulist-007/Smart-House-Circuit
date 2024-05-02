package com.algostack.smartcircuithouse.features.room_screen.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository) : ViewModel() {

    val allRooms: LiveData<List<RoomData>> = repository.allRooms

    fun getRoomsForBuilding(buildingPrimaryKey: String): LiveData<List<RoomData>> {
        return repository.getRoomsForBuilding(buildingPrimaryKey)
    }

    fun insert(roomData: RoomData) {
        viewModelScope.launch {
            repository.insert(roomData)
        }
    }

    fun bookRoom(roomData: RoomData) {
        viewModelScope.launch {
            repository.updateRoomStatus(roomData)
        }
    }

    fun cancelRoomBooking(roomData: RoomData) {
        viewModelScope.launch {
            repository.cancelRoomBooking(roomData)
        }
    }

    fun deleteRoom(roomData: RoomData) {
        viewModelScope.launch {
            repository.deleteRoom(roomData)
        }
    }

    fun bookRoom(roomData: RoomData, buildingId: String, buildingName: String, floorNumber: String, roomNumber: String, bedType: String) {
        viewModelScope.launch {
            repository.updateRoomDetails(roomData.id, buildingId, buildingName, floorNumber, roomNumber, bedType)

            roomData.isBooked = true
            repository.updateRoomStatus(roomData)
        }
    }


}
