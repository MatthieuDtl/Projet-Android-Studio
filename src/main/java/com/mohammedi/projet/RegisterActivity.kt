package com.mohammedi.projet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

    public fun goToLogin(view: View)
    {
        finish();
    }


    public fun register(view: View){

        val login = findViewById<TextView>(R.id.txtRegisterName).text.toString()
        val password = findViewById<TextView>(R.id.txtRegisterPassword).text.toString()

        reg1 = RegisterData(login, password)
        Api().post<RegisterData>("https://polyhome.lesmoulinsdudev.com/api/users/register", reg1, ::registerSuccess)

    }

    private fun registerSuccess(responseCode: Int)
    {
        if (responseCode == 200)
            finish();

    }
}