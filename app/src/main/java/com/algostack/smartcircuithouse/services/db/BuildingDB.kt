package com.algostack.smartcircuithouse.services.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.algostack.smartcircuithouse.services.model.BuildingData

@Database(entities = [BuildingData::class], version = 3, exportSchema = false)
abstract class BuildingDB : RoomDatabase() {

    abstract fun buildingDao(): BuildingDao

    companion object {
        @Volatile
        private var INSTANCE: BuildingDB? = null

        fun getDatabase(context: Context): BuildingDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuildingDB::class.java,
                    "building_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}