package com.mohammedi.projet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import java.util.ArrayList

data class HouseAdapter(
    private val context: Context ,
    private val dataSource: ArrayList<House>,
) : BaseAdapter()
{
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.houses_list, parent, false)

        val house = getItem(position) as House

        val houseId = rowView.findViewById<TextView>(R.id.houseIdView)
        val owner = rowView.findViewById<TextView>(R.id.ownerView)


        houseId.text = house.houseId.toString()
        owner.text = house.owner.toString()


        return rowView
    }
}
