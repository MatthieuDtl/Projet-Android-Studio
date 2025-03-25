package com.mohammedi.projet

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HousesActivity : AppCompatActivity() {
    private val houses: ArrayList<House> = ArrayList()
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_houses)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        token = intent.getStringExtra("TOKEN")
        loadHouses()
    }
    private fun initHousesListView()
    {
        val listView = findViewById<ListView>(R.id.ListHouses)
        listView.adapter = HouseAdapter(this,houses)

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedHouse = houses[position]
            val intent = Intent(this, DeviceActivity::class.java)
            intent.putExtra("HOUSEID", selectedHouse.houseId.toString())
            intent.putExtra("TOKEN", token)
            startActivity(intent)
        }
    }

    private fun loadHouses(){
        Api().get<List<House>>("https://polyhome.lesmoulinsdudev.com/api/houses", ::loadHousesSuccess , intent.getStringExtra("TOKEN" ))
    }
    private fun loadHousesSuccess(responseCode: Int, loadedHouses: List<House>?) {

        if (responseCode == 200 && loadedHouses != null) {
            println("oui $loadedHouses")
            runOnUiThread {
                houses.clear()
                houses.addAll(loadedHouses)
                initHousesListView()
            }
        }
    }
}