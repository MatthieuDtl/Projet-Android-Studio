package com.mohammedi.projet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var userdata = LoginData("","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
    }

    public fun login(view: View)
    {
        val login = findViewById<TextView>(R.id.txtLogin).text.toString()
        val pw = findViewById<TextView>(R.id.txtPassword).text.toString()

        userdata = LoginData(login,pw)
        Api().post<LoginData,TokenData>("https://polyhome.lesmoulinsdudev.com/api/users/auth",userdata,::loginSuccess)

    }

    private fun loginSuccess(responseCode:Int,token:TokenData?)
    {
        if (responseCode==200 && token?.token != null)
        {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("TOKEN", token.token)
            startActivity(intent)
        }
        else if (responseCode == 401)
        {


            val error = findViewById<TextView>(R.id.txtError)
            error.text = "Les données sont incorrectes"

        }
        else if (responseCode == 404)
        {

            val error = findViewById<TextView>(R.id.txtError)
            error.text = "Mauvais Login"
        }
        else if (responseCode == 500)
        {

            val error = findViewById<TextView>(R.id.txtError)
            error.text = "Une erreur s’est produite au niveau du serveur"

        }
    }
    public fun registerNewAccount(view: View)
    {
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }

}