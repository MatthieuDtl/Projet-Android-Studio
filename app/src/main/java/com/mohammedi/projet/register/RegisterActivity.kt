package com.mohammedi.projet.register

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mohammedi.projet.Api
import com.mohammedi.projet.R

//activité qui permet de s'inscrire
class RegisterActivity : AppCompatActivity() {
    private var reg1 = RegisterData("","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //revient à l'activité de connexion
    public fun goToLogin(view: View)
    {
        finish();
    }


    //envoie les données du nouveau compte à l'API
    public fun register(view: View){

        val login = findViewById<TextView>(R.id.txtRegisterName).text.toString()
        val password = findViewById<TextView>(R.id.txtRegisterPassword).text.toString()

        reg1 = RegisterData(login, password)
        Api().post<RegisterData>("https://polyhome.lesmoulinsdudev.com/api/users/register", reg1, ::registerSuccess)

    }

    //succès de l'envoi des données
    private fun registerSuccess(responseCode: Int)
    {
        if (responseCode == 200)
            finish()
        else{
            val txtError = findViewById<TextView>(R.id.txtError)
            txtError.text =  "Une erreur est survenue durant la création du compte"
        }
    }
}