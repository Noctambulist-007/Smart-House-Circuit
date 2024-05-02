package com.algostack.smartcircuithouse.features.home_screen.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.RoomRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InOutViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    val itemList: LiveData<List<Item>> = roomRepository.getBookedRooms().map { rooms ->
        rooms.map { room ->
            Item(
                room.id,
                room.customerName ?: "",
                room.customerDetails ?: "",
                formatDate(room.entryDate),
                formatDate(room.exitDate),
                room.roomBuildingName ?: "",
                room.roomNo ?: "",
                room.floorNo ?: "",
                room.bedType ?: ""
            )
        }
    }

    fun filterItemsByEntryDate(selectedDate: String, callback: (List<Item>) -> Unit) {
        viewModelScope.launch {
            val allItems = itemList.value ?: emptyList()
            val filteredItems = allItems.filter { item ->
                item.entryDate.equals(selectedDate, ignoreCase = true)
            }
            callback(filteredItems)
        }
    }


    fun filterItemsByExitDate(selectedDate: String, callback: (List<Item>) -> Unit) {
        viewModelScope.launch {
            val allItems = itemList.value ?: emptyList()
            val filteredItems = allItems.filter { item ->
                item.exitDate.equals(selectedDate, ignoreCase = true)
            }
            callback(filteredItems)
        }
    }

    fun cancelBooking(roomId: String) {
        viewModelScope.launch {
            roomRepository.cancelBooking(roomId)
        }
    }

    private fun formatDate(dateInMillis: Long?): String {
        return if (dateInMillis != null) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance().apply { timeInMillis = dateInMillis }
            dateFormat.format(calendar.time)
        } else {
            ""
        }
    }
}
