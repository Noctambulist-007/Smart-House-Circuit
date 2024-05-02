package com.algostack.smartcircuithouse.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomData(
    @PrimaryKey(autoGenerate = false)
    val id: String =  "room"+(1..6).map { ('a'..'z').random() }.joinToString("") + (1..9).map { (0..6).random() }.joinToString(""),
    val roomNo: String,
    val bedType: String,
    val floorNo: String,
    val buildingId: String,
    val roomBuildingName: String,
    var isBooked: Boolean = false,
    var customerName: String? = "",
    var customerDetails: String? = "",
    var entryDate: Long? = 0,
    var exitDate: Long? = 0,
    var primaryKey: String
)
