package com.mohammedi.projet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView


class UserAdapter(
    private val context: Context,
    private val dataSource: ArrayList<UserData>,
    private val listener: DeleteListener
) : BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;

    override fun getCount(): Int = dataSource.size;

    override fun getItem(position: Int): Any = dataSource[position];

    override fun getItemId(position: Int): Long = position.toLong();

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.users_list, parent, false);
        val user = getItem(position) as UserData

        val login = rowView.findViewById<TextView>(R.id.userName);
        val statut = rowView.findViewById<TextView>(R.id.userStatus);
        val deleteButton = rowView.findViewById<Button>(R.id.deleteButton);

        login.text = user.userLogin
        if(user.owner == 1)
            statut.text = "Propriétaire"
        else
            statut.text = "Invité"

        deleteButton.setOnClickListener {
            listener.onUserDelete(user)
            dataSource.removeAt(position)
            notifyDataSetChanged()
        }

        if (user.owner == 1)
            deleteButton.visibility = View.GONE

        return rowView;
    }
}

interface DeleteListener {
    fun onUserDelete(user: UserData)
}