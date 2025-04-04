package com.mohammedi.projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

class DeviceAdapter(
    private val context: Context,
    private val dataSource: ArrayList<DeviceData>,
    private val commandListener: DeviceCommandListener
) : BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

    override fun getCount(): Int = dataSource.size;

    override fun getItem(position: Int): Any = dataSource[position];

    override fun getItemId(position: Int): Long = position.toLong();

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.devices_list, parent, false);
        val device = getItem(position) as DeviceData;

        val deviceId = rowView.findViewById<TextView>(R.id.idView);
        val type = rowView.findViewById<TextView>(R.id.typeView);
        val opening = rowView.findViewById<TextView>(R.id.openingView);
        val openingMode = rowView.findViewById<TextView>(R.id.openingModeView);
        val availableCommView = rowView.findViewById<TextView>(R.id.AvailableCommView);
        val power = rowView.findViewById<TextView>(R.id.powerView);
        val switch = rowView.findViewById<Switch>(R.id.itemSwitch);
        val stopButton = rowView.findViewById<Button>(R.id.stopButton);

        deviceId.text = device.id;
        type.text = device.type;
        opening.text = device.opening.toString();
        openingMode.text = device.openingMode.toString()
        availableCommView.text = device.availableCommands.joinToString(", ")
        power.text = device.power.toString()


        if (device.power == 1){
            switch.isChecked = true
        }
        else
            switch.isChecked = false

        switch.setOnCheckedChangeListener { _, isChecked ->
            val command = if (isChecked) {
                if (device.type == "light") "TURN ON" else "OPEN"
            } else {
                if (device.type == "light") "TURN OFF" else "CLOSE"
            }
            commandListener.sendCommandToDevice(device.id, command)
        }

        if (device.type == "rolling shutter" || device.type == "garage door") {
            stopButton.visibility = View.VISIBLE
            stopButton.setOnClickListener {
                commandListener.sendCommandToDevice(device.id, "STOP");
            }
        } else {
            stopButton.visibility = View.GONE
        }
        return rowView;
    }
}

interface DeviceCommandListener {
    fun sendCommandToDevice(deviceId: String, command: String);
}
