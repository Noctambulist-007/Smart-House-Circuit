package com.algostack.smartcircuithouse.features.settings_screen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.smartcircuithouse.services.db.BuildingDB
import com.algostack.smartcircuithouse.services.db.BuildingDao
import com.algostack.smartcircuithouse.services.db.DataBackupRepository
import com.algostack.smartcircuithouse.services.db.RoomDB
import kotlinx.coroutines.launch

class SettingViewModel  (private val context: Context) : ViewModel() {

    private val dataBackupRepository: DataBackupRepository


    init {
        val roomDao = RoomDB.getDatabase(context).roomDao()
        val buildingDao = BuildingDB.getDatabase(context).buildingDao()
        dataBackupRepository = DataBackupRepository(buildingDao, roomDao)
    }


    suspend fun getAllDataForBackup(context: Context){
        viewModelScope.launch {
            dataBackupRepository.getAllDataForBackup(context)

        }


    }



    suspend fun syncData(context: Context){
        viewModelScope.launch {
            dataBackupRepository.syncDataFromServer(context)
        }
    }






}