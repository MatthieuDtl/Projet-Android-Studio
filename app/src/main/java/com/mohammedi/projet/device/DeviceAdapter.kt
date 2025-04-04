package com.mohammedi.projet.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.mohammedi.projet.R
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
        val btnOpen = rowView.findViewById<Button>(R.id.btnOpen)
        val btnClose = rowView.findViewById<Button>(R.id.btnClose)
        val stopButton = rowView.findViewById<Button>(R.id.stopButton);

        deviceId.text = device.id;
        //type.text = device.type;
        //opening.text = device.opening.toString();
        //openingMode.text = device.openingMode.toString()
        //availableCommView.text = device.availableCommands.joinToString(", ")
        //power.text = device.power.toString()



        if (device.type == "rolling shutter" || device.type == "garage door") {
            btnOpen.text = "Ouvrir"
            btnClose.text = "Fermer"
            stopButton.visibility = View.VISIBLE
            stopButton.setOnClickListener {
                commandListener.sendCommandToDevice(device.id, "STOP");
            }
            btnOpen.setOnClickListener {
                commandListener.sendCommandToDevice(device.id, "OPEN");
            }
            btnClose.setOnClickListener {
                commandListener.sendCommandToDevice(device.id, "CLOSE");
            }
        } else {
            btnOpen.text = "Allumer"
            btnClose.text = "Eteindre"
            stopButton.visibility = View.GONE
            btnOpen.setOnClickListener {
                commandListener.sendCommandToDevice(device.id, "TURN ON");
            }
            btnClose.setOnClickListener {
                commandListener.sendCommandToDevice(device.id, "TURN OFF");
            }
        }
        return rowView;
    }
}

