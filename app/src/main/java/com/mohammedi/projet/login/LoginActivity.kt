package com.mohammedi.projet.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mohammedi.projet.Api
import com.mohammedi.projet.R
import com.mohammedi.projet.menu.MenuActivity
import com.mohammedi.projet.register.RegisterActivity

//activité qui permet de se connecter
class MainActivity : AppCompatActivity() {
    private var userdata = LoginData("","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
    }

    //connexion d'un utilisateur à l'API
    public fun login(view: View)
    {
        val login = findViewById<TextView>(R.id.txtLogin).text.toString()
        val pw = findViewById<TextView>(R.id.txtPassword).text.toString()

        userdata = LoginData(login,pw)
        Api().post<LoginData, TokenData>("https://polyhome.lesmoulinsdudev.com/api/users/auth",userdata,::loginSuccess)

    }

    //succès de la connexion ou erreur
    private fun loginSuccess(responseCode:Int,token: TokenData?)
    {
        if (responseCode==200 && token?.token != null)
        {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("TOKEN", token.token)
            startActivity(intent)
        }
        else
        {
            val error = findViewById<TextView>(R.id.txtError)
            error.text = "Une erreur s'est produite"

        }
    }

    //chargement de l'activié pour créer un compte
    public fun registerNewAccount(view: View)
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}