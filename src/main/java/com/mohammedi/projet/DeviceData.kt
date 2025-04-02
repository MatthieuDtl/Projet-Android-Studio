package com.mohammedi.projet

data class DeviceData(
    val id: String,
    val type: String,
    val availableCommands: ArrayList<String>,
    val opening: Double?,
    val openingMode: Int?,
    val power: Int?
)

data class Devices(
    val devices: ArrayList<DeviceData>
)