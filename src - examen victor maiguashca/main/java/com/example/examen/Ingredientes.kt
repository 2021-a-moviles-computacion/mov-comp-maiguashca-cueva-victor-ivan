package com.example.examen

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.examen.InstanciaBdd.Companion.TablasBdd

class Ingredientes(): AppCompatActivity() {
    var arrIngredientes = arrayListOf<IngredienteBean>()
    var posicionItemSeleccionado: Int = 0
    var aux:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredientes)
        var miId = Bundle()
        miId= this.intent.extras!!
        var idReceta = miId.getInt("id")
        Log.i("examen", "llego ${idReceta}")

        val receta = findViewById<TextView>(R.id.idIngRec)
        var catRec = RecetaBean(0,"")
        catRec = TablasBdd!!.consutarRecetaId(idReceta)
        Log.i("examen", "llego ${catRec}")
        receta.setText(catRec.nombre.toUpperCase())

        Log.i("examen", "base ingrediente")
        arrIngredientes = TablasBdd!!.consutarIngredientes(idReceta)
        Log.i("examen", "consulta base")

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arrIngredientes
        )
        Log.i("examen", "adaptador")

        val lstvwIngrediente = findViewById<ListView>(R.id.lstIngredientes)
        lstvwIngrediente.adapter = adaptador
        Log.i("examen", "listview")
        registerForContextMenu(lstvwIngrediente)
        Log.i("examen", "menucontextual")

        val addIng = findViewById<Button>(R.id.btnAgregarIngrediente)
        addIng.setOnClickListener {
            Log.i("examen", "listener agregar")
            val ingred = findViewById<EditText>(R.id.txtIngrediente)
            val cant = findViewById<EditText>(R.id.txtCantidad)
            val unid = findViewById<EditText>(R.id.txtUnidad)
            Log.i("examen", "txt agregar")
            if (ingred.text.equals("") || cant.text.equals("") || unid.text.equals(""))
            {
                Log.i("examen", "txt vacios - ${cant.toString()}")
            }
            else{
                val nuevo = IngredienteBean(0,idReceta,ingred.text.toString(),cant.text.toString().toDouble(),unid.text.toString())
                Log.i("examen", "objIngrediente")
                if(TablasBdd!!.insertarIngrediente(nuevo))
                {
                    Log.i("examen","insertado")
                }
            }
        }

        val editar = findViewById<Button>(R.id.btnEditarI)
        editar.setOnClickListener {

            val ingred = findViewById<EditText>(R.id.txtIngrediente)
            val cant = findViewById<EditText>(R.id.txtCantidad)
            val unid = findViewById<EditText>(R.id.txtUnidad)

            aux=arrIngredientes.get(posicionItemSeleccionado).id
            val aux2 = arrIngredientes.get(posicionItemSeleccionado).idReceta

            Log.i("examen","actualizar en id ${aux}")
            val editar = IngredienteBean(aux,aux2,ingred.text.toString(),cant.text.toString().toDouble(),unid.text.toString())
            if(TablasBdd!!.actualizarIngrediente(editar))
            {
                Log.i("examen","actualizado")
            }
        }
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
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val opciones = item.toString()

        Log.i("examen", "${opciones} en id = ${posicionItemSeleccionado}")
        if(opciones == "Editar"){
            val editar = findViewById<Button>(R.id.btnEditarI)
            editar.visibility=View.VISIBLE

            val agregar = findViewById<Button>(R.id.btnAgregarIngrediente)
            agregar.visibility=View.INVISIBLE

            val ingrediente = findViewById<EditText>(R.id.txtIngrediente)
            ingrediente.setText(arrIngredientes.get(posicionItemSeleccionado).nombre)

            val cantidad = findViewById<EditText>(R.id.txtCantidad)
            cantidad.setText(arrIngredientes.get(posicionItemSeleccionado).cantidad.toString())

            val unidad = findViewById<EditText>(R.id.txtUnidad)
            unidad.setText(arrIngredientes.get(posicionItemSeleccionado).unidad)
        }
        if(opciones == "Eliminar"){
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Borrar")
            builder.setMessage("¿seguro?")
            aux=arrIngredientes.get(posicionItemSeleccionado).id
            builder.setPositiveButton("si", DialogInterface.OnClickListener { dialogInterface, i ->
                if(TablasBdd?.eliminaringrediente(aux) == true)
                {
                    Log.i("examen", "borrado")
                }
            }
            )
            builder.setNegativeButton("no",null)
            val dialogo = builder.create()
            dialogo.show()
        }
        return super.onContextItemSelected(item)
    }
}