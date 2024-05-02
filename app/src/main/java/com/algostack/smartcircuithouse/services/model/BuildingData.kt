package com.algostack.smartcircuithouse.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buildings")
data class BuildingData(
    @PrimaryKey(autoGenerate = false)
    val id: String = "build"+(1..6).map { ('a'..'z').random() }.joinToString("")+(1..9).map { (0..6).random() }.joinToString(""),
    val name: String,
    // generated random key is first 4 are string and last 4 are number
    val primaryKey: String = "SCH"+(1..6).map { ('a'..'z').random() }.joinToString("") + (1..9).map { (0..6).random() }.joinToString("")
)