package com.algostack.smartcircuithouse.features.home_screen.model

data class Item(
    val id: String,
    val name: String,
    val details: String,
    val entryDate: String,
    val exitDate: String,
    val roomBuildingName: String?,
    val roomNumber: String?,
    val floorNumber: String?,
    val bedType: String?
)
