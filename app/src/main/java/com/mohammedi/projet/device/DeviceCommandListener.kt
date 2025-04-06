package com.mohammedi.projet.device

//interface qui permet l'envoi des commandes entre l'adapter et l'activité
interface DeviceCommandListener {
    fun sendCommandToDevice(deviceId: String, command: String)
}
