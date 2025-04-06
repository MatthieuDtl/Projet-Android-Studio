package com.mohammedi.projet.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mohammedi.projet.R
import java.util.ArrayList

//adapter de la classe MenuActivity
data class HouseAdapter(
    private val context: Context,
    private val dataSource: ArrayList<HouseData>,
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

        val houseData = getItem(position) as HouseData

        val houseId = rowView.findViewById<TextView>(R.id.houseIdView)
        val owner = rowView.findViewById<TextView>(R.id.houseOwnerView)


        houseId.text = "Numéro de maison : "+houseData.houseId.toString()

        if (houseData.owner)
            owner.text = "Propriétaire"
        else
            owner.text = "Invité"


        return rowView
    }
}
