package com.mohammedi.projet.device

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mohammedi.projet.Api
import com.mohammedi.projet.R

//activité qui permet de d'afficher les périphériques et d'envoyer des commandes
class DeviceActivity : AppCompatActivity(), DeviceCommandListener {
    private var token: String? = null
    private var houseId: String? = null
    private val devices: ArrayList<DeviceData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        token = intent.getStringExtra("TOKEN")
        houseId = intent.getStringExtra("HOUSEID")
        loadDevices()
        val deviceTitle = findViewById<TextView>(R.id.deviceTitle)
        deviceTitle.text = "Périphériques de la maison $houseId"
    }

    //chargement des Périphériques
    private fun loadDevices() {
        Api().get<Devices>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices", ::loadDevicesSuccess, token)
    }

    //succès du chargement
    private fun loadDevicesSuccess(responseCode: Int, loadDevices: Devices?) {
        if (responseCode == 200 && loadDevices != null) {
            runOnUiThread {
                devices.clear()
                devices.addAll(loadDevices.devices)
                initDevicesListView()
            }
        }
    }

    //initialisation de l'adapter
    private fun initDevicesListView() {
        val listView = findViewById<ListView>(R.id.listDevices)
        val adapter = DeviceAdapter(this, devices, this)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    //envoie les commandes à l'API
    override fun sendCommandToDevice(deviceId: String, command: String) {
        Api().post<Map<String, String>, Any>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command", mapOf("command" to command), ::sendCommandSuccess, token)
    }

    //succès de l'envoi des commandes
    private fun sendCommandSuccess(responseCode: Int, a: Any?) {
        runOnUiThread {
            if (responseCode == 200) {
                println("Commande envoyée avec succès")
            } else {
                println("Erreur $responseCode lors de l'envoi de la commande")
            }
        }
    }

    //mode nuit qui ferme toutes les portes et éteint toutes les lumières
    fun nightMode (view: View){
        for (device in devices) {
            if(device.type =="light")
                sendCommandToDevice(device.id,"TURN OFF")
            else
                sendCommandToDevice(device.id,"CLOSE")
        }
    }

    //mode jour qui ouvre toutes les portes et allume toutes les lumières
    fun dayMode (view: View){
        for (device in devices) {
            if(device.type =="light")
                sendCommandToDevice(device.id,"TURN ON")
            else
                sendCommandToDevice(device.id,"OPEN")
        }
    }

    //retour sur l'activité Menu
    fun goBack(view: View){
        finish()
    }
}
