package com.mohammedi.projet.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mohammedi.projet.Api
import com.mohammedi.projet.R
import com.mohammedi.projet.user.UserActivity
import com.mohammedi.projet.device.DeviceActivity
import com.mohammedi.projet.register.RegisterActivity
import com.mohammedi.projet.settings.SettingsActivity

//activité qui permet la gestion principale de l'application en choissiant entre
//l'envoi des commandes, la gestion des utilisateurs et les paramètres
class MenuActivity : AppCompatActivity() {
    private val houseData = ArrayList<HouseData>()
    private var selectedHouseData: HouseData? = null
    private var token: String? = null
    private lateinit var btnPeriph: Button
    private lateinit var btnUsers: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        token = intent.getStringExtra("TOKEN")
        btnPeriph = findViewById(R.id.btnPeriph)
        btnUsers = findViewById(R.id.btnUsers)
        btnSettings = findViewById<Button>(R.id.btnSettings)


        btnPeriph.setOnClickListener(View.OnClickListener { v: View? ->
            navigateTo(DeviceActivity::class.java)
        })
        btnUsers.setOnClickListener(View.OnClickListener { v: View? ->
            navigateTo(UserActivity::class.java)
        })

        loadHouses()
    }

    //initialisation de l'adapter
    //si une maison est sélectionnée, on rend cliquable le bouton périphérique
    //et si cette maison est celle du propriétaire, on rend cliquable le bouton Users
    private fun initHousesListView() {
        val listView = findViewById<ListView>(R.id.ListHouses)
        listView.adapter = HouseAdapter(this, houseData)

        listView.onItemClickListener =
            OnItemClickListener { _, _, position: Int, _ ->
                selectedHouseData = houseData[position]
                btnPeriph.isEnabled = true
                btnPeriph.alpha = 1.0f

                if (houseData[position].owner) {
                    btnUsers.isEnabled = true
                    btnUsers.alpha = 1.0f
                }
                else{
                    btnUsers.isEnabled = false
                    btnUsers.alpha = 0.5f
                }
            }
    }

    //chargement des maisons
    private fun loadHouses() {
        Api().get<List<HouseData>?>("https://polyhome.lesmoulinsdudev.com/api/houses", ::loadHousesSuccess, token)
    }

    //succès du chargement
    private fun loadHousesSuccess(responseCode: Int, loadedHouseData: List<HouseData>?) {
        if (responseCode == 200 && loadedHouseData != null) {
            runOnUiThread {
                houseData.clear()
                houseData.addAll(loadedHouseData)
                initHousesListView()
            }
        }
    }

    //navigation entre une activité plaçée en paramètre seulement si une maison est sélectionnée
    private fun navigateTo(activityClass: Class<*>) {
        if (selectedHouseData != null) {
            val intent = Intent(this, activityClass)
            intent.putExtra("HOUSEID", selectedHouseData!!.houseId.toString())
            intent.putExtra("TOKEN", token)
            startActivity(intent)
        }
    }

    //déconnexion
    fun logout(view:View){
        finish()
    }

    //chargement de l'activité settings
    fun goToSettings(view:View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }


}