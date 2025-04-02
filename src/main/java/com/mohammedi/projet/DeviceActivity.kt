package com.mohammedi.projet

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
    }

    private fun loadDevices() {
        Api().get<Devices>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices", ::loadDevicesSuccess, token)
    }

    private fun loadDevicesSuccess(responseCode: Int, loadDevices: Devices?) {
        if (responseCode == 200 && loadDevices != null) {
            runOnUiThread {
                devices.clear()
                devices.addAll(loadDevices.devices)
                initDevicesListView()
            }
        }
    }

    private fun initDevicesListView() {
        val listView = findViewById<ListView>(R.id.ListDevices)
        val adapter = DeviceAdapter(this, devices, this)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun sendCommandToDevice(deviceId: String, command: String) {

        Api().post<Map<String, String>, Unit>(
            "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command",
            mapOf("command" to command),
            { responseCode, _ ->
                if (responseCode == 200) {
                    println("Commande envoyée avec succès à $deviceId")
                } else {
                    println("Erreur $responseCode pour $deviceId")
                }
            },
            token
        )
    }
}
