package com.mohammedi.projet.user

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mohammedi.projet.Api
import com.mohammedi.projet.R

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
        val userData = UserData(userLogin = login, owner = 0)

        Api().post<UserData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", userData, ::addUserSuccess, token)

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
        Api().delete<UserData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", user, ::deleteUserSuccess, token)

    }

    private fun deleteUserSuccess(responseCode: Int) {
        if (responseCode == 200) {
            println("Utilisateur supprimé avec succès")
        }
    }

}