package com.mohammedi.projet.user

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mohammedi.projet.Api
import com.mohammedi.projet.R

//activité qui permet la gestion des différents comptes associés à une maispn
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
        val userTitle = findViewById<TextView>(R.id.userTitle)
        userTitle.text = "Utilisateurs de la maison $houseId"
    }

    //chargement des tous les utilisateurs associés à une maison
    private fun loadUsers() {
        Api().get<List<UserData>>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", ::loadUsersSuccess, token)
    }

    //succès du chargement
    private fun loadUsersSuccess(responseCode: Int, loadUsers: List<UserData>?) {
        if (responseCode == 200 && loadUsers != null) {
            runOnUiThread {
                users.clear()
                users.addAll(loadUsers)
                initUsersListView()
            }
        }
    }

    //initialisation de l'adapter
    private fun initUsersListView() {
        val listView = findViewById<ListView>(R.id.listUsers)
        val adapter = UserAdapter(this, users, this)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    //ajout d'un utilisateur dans la maison
    fun addUser(view: View) {
        val login = findViewById<EditText>(R.id.editLogin).text.toString()
        val userData = UserData(userLogin = login, owner = 0)

        Api().post<UserData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", userData, ::addUserSuccess, token)

    }

    //succès de l'ajout de l'utilisateur + ajout de l'utilisateur dans la liste
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

    //suppression d'un utilisateur de la maison
    override fun onUserDelete(user: UserData) {
        Api().delete<UserData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users", user, ::deleteUserSuccess, token)

    }

    //succès de la suppression
    private fun deleteUserSuccess(responseCode: Int) {
        if (responseCode == 200) {
            println("Utilisateur supprimé avec succès")
        }
    }

    //retour sur l'activité Menu
    fun goBack(view:View){
        finish()
    }

}