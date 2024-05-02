package com.algostack.smartcircuithouse.features.home_screen.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData
import kotlinx.coroutines.launch

class UnbookedViewModel(private val context: Context) : ViewModel() {

    private val roomRepository: RoomRepository
    private val unbookedRoomsLiveData: LiveData<List<RoomData>>

    init {
        val roomDao = RoomDB.getDatabase(context).roomDao()
        roomRepository = RoomRepository(roomDao)
        unbookedRoomsLiveData = roomRepository.getUnbookedRooms()
    }

    fun getUnbookedRooms(): LiveData<List<RoomData>> {
        return unbookedRoomsLiveData
    }

    fun bookRoom(roomData: RoomData) {
        viewModelScope.launch {
            roomRepository.updateRoomStatus(roomData)
        }
    }

    fun cancelRoomBooking(roomData: RoomData) {
        viewModelScope.launch {
            roomRepository.cancelRoomBooking(roomData)
        }
    }
}
