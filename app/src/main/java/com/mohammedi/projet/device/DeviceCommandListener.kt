package com.mohammedi.projet.device

interface DeviceCommandListener {
    fun sendCommandToDevice(deviceId: String, command: String);
}
