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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Recetas : AppCompatActivity() {
    private var posicionItemSeleccionado: Int = 0
    private var aux:Int = 0
    private var arrRecetas = arrayListOf<RecetaBean>()
    private var i=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)

        arrRecetas = traertodo()

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
            if(modificar("A"+aux,receta.text.toString(),aux))
            {
                Log.i("examen","actualizado")
                receta.text.clear()
            }
        }

        val addRec = findViewById<Button>(R.id.btnAgregarReceta)
        addRec.setOnClickListener {
            val receta = findViewById<EditText>(R.id.txtNombreReceta)
            if (receta.text.toString().equals(""))
            {
            }
            else{
                if(insertaruno(receta.text.toString()))
                {
                    Log.i("examen","insertado Firestore")
                    receta.text.clear()
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
                if(borraruno("A"+aux) == true)
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

    fun borraruno(idDoc: String):Boolean
    {
        val db = Firebase.firestore
        db.collection("Receta").document(idDoc).delete()
        return true
    }

    fun traertodo(): ArrayList<RecetaBean>
    {
        val arreglo = arrayListOf<RecetaBean>()
        val db = Firebase.firestore
        db.collection("Receta")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val encontrado = RecetaBean(0,"")

                    Log.d("que trajo", "${document.id} " +
                            "=> ${document.data.toString().substringAfter("=").substringBefore(",")}" +
                            " => ${document.data.toString().substringAfterLast("=")}")

                    encontrado.nombre=document.data.toString().substringAfter("=").substringBefore(",")
                    encontrado.id=document.data.toString().substringAfterLast("=").substringBefore("}").toInt()
                    arreglo.add(encontrado)
                }
            }
        return arreglo
    }

    fun insertaruno(recetatxt:String): Boolean
    {
        val db = Firebase.firestore
        val referencia = db.collection("Receta").document("A"+i.toString())
        val nuevoReceta = hashMapOf<String, Any>(
            "nombre" to recetatxt,
            "idReceta" to i,
        )
        referencia.set(nuevoReceta)
        i++
        return true
    }
    fun modificar(idDoc:String,nomRec:String,idRec:Int): Boolean
    {
        val db = Firebase.firestore
        val referencia = db.collection("Receta").document(idDoc)
        val nuevoReceta = hashMapOf<String, Any>(
            "nombre" to nomRec,
            "idReceta" to idRec,
        )
        referencia.set(nuevoReceta)
        return true
    }
}
