package com.example.examen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.examen.InstanciaBdd.Companion.TablasBdd

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TablasBdd = Bdd(this)

        val botonIrExamen = findViewById<Button>(
            R.id.btnExamen
        )
        botonIrExamen.setOnClickListener{
            Log.i("examen","clic en boton")
            abrirActividad(Recetas::class.java)
        }
    }

    fun abrirActividad(clase:Class<*>){
        val intendExplicito = Intent (
            this,
            clase)
        Log.i("examen","declarado e instanciado intend")
        startActivity(intendExplicito)
    }
}
