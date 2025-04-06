package com.mohammedi.projet.device

//data classe d'un périphérique
data class DeviceData(
    val id: String,
    val type: String,
    val availableCommands: ArrayList<String>,
    val opening: Double?,
    val openingMode: Int?,
    val power: Int?
)

