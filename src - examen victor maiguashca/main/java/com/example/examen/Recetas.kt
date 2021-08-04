package com.example.examen

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.examen.InstanciaBdd.Companion.TablasBdd

class Recetas : AppCompatActivity() {
    var posicionItemSeleccionado: Int = 0
    var aux:Int = 0
    var arrRecetas = arrayListOf<RecetaBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)

        arrRecetas = TablasBdd!!.consutarRecetas()

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arrRecetas
        )


        val lstvwRecetas = findViewById<ListView>(R.id.lstRecetas)
        lstvwRecetas.adapter = adaptador
        registerForContextMenu(lstvwRecetas)

        val editar = findViewById<Button>(R.id.btnEditar)
        editar.setOnClickListener {
            val receta = findViewById<EditText>(R.id.txtNombreReceta)
            aux=arrRecetas.get(posicionItemSeleccionado).id
            Log.i("examen","actualizar en id ${aux}")
            if(TablasBdd!!.actualizarReceta(aux,receta.text.toString()))
            {

                Log.i("examen","actualizado")
            }
        }

        val addRec = findViewById<Button>(R.id.btnAgregarReceta)
        addRec.setOnClickListener {
            val receta = findViewById<EditText>(R.id.txtNombreReceta)
            if (receta.text.equals(""))
            {
            }
            else{
                if(TablasBdd!!.insertarReceta(receta.text.toString()))
                {
                    Log.i("examen","insertado")
                }
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val opciones = item.toString()

        Log.i("examen", "${opciones} en id = ${posicionItemSeleccionado}")
        if(opciones == "Editar"){
            val editar = findViewById<Button>(R.id.btnEditar)
            editar.visibility=View.VISIBLE

            val agregar = findViewById<Button>(R.id.btnAgregarReceta)
            agregar.visibility=View.INVISIBLE

            val receta = findViewById<EditText>(R.id.txtNombreReceta)
            receta.setText(arrRecetas.get(posicionItemSeleccionado).nombre)
        }
        if(opciones == "Eliminar"){
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Borrar")
            builder.setMessage("¿seguro?")
            aux=arrRecetas.get(posicionItemSeleccionado).id
            builder.setPositiveButton("si",DialogInterface.OnClickListener { dialogInterface, i ->
                if(TablasBdd?.eliminarReceta(aux) == true)
                {
                    Log.i("examen", "borrado")
                }
            }
            )
            builder.setNegativeButton("no",null)
            val dialogo = builder.create()
            dialogo.show()
        }
        if(opciones == "Observar"){
            aux=arrRecetas.get(posicionItemSeleccionado).id
            abrirActividad(Ingredientes()::class.java)
        }

        return super.onContextItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu,v,menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItemSeleccionado = id
    }

    fun abrirActividad(clase:Class<*>){
        val intendExplicito = Intent (
            this,
            clase)
        Log.i("examen","declarado e instanciado intend")
        val miId = Bundle()
        miId.putInt("id", aux)
        intendExplicito.putExtras(miId)
        startActivity(intendExplicito)
    }
}