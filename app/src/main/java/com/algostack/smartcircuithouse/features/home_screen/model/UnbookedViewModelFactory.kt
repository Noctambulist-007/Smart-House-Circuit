package com.algostack.smartcircuithouse.features.home_screen.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UnbookedViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UnbookedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UnbookedViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
