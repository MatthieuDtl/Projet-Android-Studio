package com.mohammedi.projet

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserActivity : AppCompatActivity(), DeleteListener {
    private var token: String? = null
    private var houseId: String? = null
    private val users: ArrayList<UserData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        token = intent.getStringExtra("TOKEN")
        houseId = intent.getStringExtra("HOUSEID")
        loadUsers()
    }


    private fun loadUsers() {
        Api().get<List<UserData>>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", ::loadUsersSuccess, token)
    }


    private fun loadUsersSuccess(responseCode: Int, loadUsers: List<UserData>?) {
        if (responseCode == 200 && loadUsers != null) {
            runOnUiThread {
                users.clear()
                users.addAll(loadUsers)
                initUsersListView()
            }
        }
    }

    private fun initUsersListView() {
        val listView = findViewById<ListView>(R.id.listUsers)
        val adapter = UserAdapter(this, users, this)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    fun addUser(view: View) {
        val login = findViewById<EditText>(R.id.editLogin).text.toString()
        val userInfoAdd = mapOf("userLogin" to login)

        Api().post<Any>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", userInfoAdd, ::addUserSuccess, token)
    }

    private fun addUserSuccess(responseCode: Int) {
        if (responseCode == 200) {
            runOnUiThread {
                val login = findViewById<EditText>(R.id.editLogin).text.toString()
                val user = UserData(login, 0)
                users.add(user)
                initUsersListView()
            }
        }
    }


    override fun onUserDelete(user: UserData) {
        Api().delete<Any>(
            "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", mapOf("userLogin" to user.userLogin), ::deleteUserSuccess, token
        )
    }

    private fun deleteUserSuccess(responseCode: Int) {
        if (responseCode == 200) {
            println("Utilisateur supprimé avec succès")
        }
    }

}