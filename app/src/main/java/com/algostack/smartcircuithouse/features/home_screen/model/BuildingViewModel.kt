package com.algostack.smartcircuithouse.features.home_screen.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.BuildingDB
import com.algostack.smartcircuithouse.services.model.BuildingData
import kotlinx.coroutines.launch

class BuildingViewModel : ViewModel() {

    private val _itemCreated = MutableLiveData<Boolean>()
    val itemCreated: LiveData<Boolean> get() = _itemCreated

    fun saveBuilding(context: Context, buildingName: String) {
        val buildingDao = BuildingDB.getDatabase(context).buildingDao()
        val building = BuildingData(name = buildingName)
        viewModelScope.launch {
            buildingDao.insert(building)
            _itemCreated.value = true
        }
    }
}

