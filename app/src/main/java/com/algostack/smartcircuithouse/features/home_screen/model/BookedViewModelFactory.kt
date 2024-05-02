package com.algostack.smartcircuithouse.features.home_screen.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BookedViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookedViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
