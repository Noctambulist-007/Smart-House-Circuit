package com.algostack.smartcircuithouse.features.settings_screen.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.algostack.smartcircuithouse.features.settings_screen.viewmodel.SettingViewModel

class SettingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

