package com.algostack.smartcircuithouse.features.home_screen.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.algostack.smartcircuithouse.services.db.RoomRepository

class InOutViewModelFactory(
    private val roomRepository: RoomRepository,
    private val fragment: Fragment
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InOutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InOutViewModel(roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

