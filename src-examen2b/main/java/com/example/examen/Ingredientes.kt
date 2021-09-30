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

class Ingredientes(): AppCompatActivity() {
    var arrIngredientes = arrayListOf<IngredienteBean>()
    var posicionItemSeleccionado: Int = 0
    var aux:Int = 0
    var i=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredientes)
        var miId = Bundle()
        miId= this.intent.extras!!
        var idReceta = miId.getInt("id")
        Log.i("examen", "llego ${idReceta}")

        val receta = findViewById<TextView>(R.id.idIngRec)
        /*var catRec = RecetaBean(0,"")
        catRec = traerRec()
        Log.i("examen", "llego ${catRec}")
        receta.setText(catRec.nombre.toUpperCase())*/

        Log.i("examen", "base ingrediente")
        arrIngredientes = traertodo(idReceta)
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
            val lat = findViewById<EditText>(R.id.txtLat)
            val lon = findViewById<EditText>(R.id.txtLon)
            Log.i("examen", "txt agregar")
            if (ingred.text.equals("") || cant.text.equals("") || unid.text.equals(""))
            {
                Log.i("examen", "txt vacios - ${cant.toString()}")
            }
            else{
                val nuevo = IngredienteBean(0,idReceta,ingred.text.toString(),cant.text.toString().toDouble(),unid.text.toString(),lat.text.toString().toDouble(),lon.text.toString().toDouble())
                Log.i("examen", "objIngrediente")
                if(insertaruno(nuevo))
                {
                    Log.i("examen","insertado")
                    ingred.text.clear()
                    cant.text.clear()
                    unid.text.clear()
                    lat.text.clear()
                    lon.text.clear()
                }
            }
        }

        val editar = findViewById<Button>(R.id.btnEditarI)
        editar.setOnClickListener {

            val ingred = findViewById<EditText>(R.id.txtIngrediente)
            val cant = findViewById<EditText>(R.id.txtCantidad)
            val unid = findViewById<EditText>(R.id.txtUnidad)
            val lat = findViewById<EditText>(R.id.txtLat)
            val lon = findViewById<EditText>(R.id.txtLon)

            aux=arrIngredientes.get(posicionItemSeleccionado).id
            val aux2 = arrIngredientes.get(posicionItemSeleccionado).idReceta

            Log.i("examen","actualizar en id ${aux}")
            val editar = IngredienteBean(aux,aux2,ingred.text.toString(),cant.text.toString().toDouble(),unid.text.toString(),lat.text.toString().toDouble(),lon.text.toString().toDouble())
            if(modificar(editar))
            {
                Log.i("examen","actualizado")
                ingred.text.clear()
                cant.text.clear()
                unid.text.clear()
                lat.text.clear()
                lon.text.clear()
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

            val lati = findViewById<EditText>(R.id.txtLat)
            lati.setText(arrIngredientes.get(posicionItemSeleccionado).lat.toString())

            val longi = findViewById<EditText>(R.id.txtLon)
            longi.setText(arrIngredientes.get(posicionItemSeleccionado).lon.toString())
        }
        if(opciones == "Eliminar"){
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Borrar")
            builder.setMessage("¿seguro?")
            aux=arrIngredientes.get(posicionItemSeleccionado).id
            builder.setPositiveButton("si", DialogInterface.OnClickListener { dialogInterface, i ->
                if(borraruno(aux,arrIngredientes.get(posicionItemSeleccionado).idReceta))
                {
                    Log.i("examen", "borrado")
                }
            }
            )
            builder.setNegativeButton("no",null)
            val dialogo = builder.create()
            dialogo.show()

        }
        if(opciones == "Observar")
        {
            aux=arrIngredientes.get(posicionItemSeleccionado).id
            abrirActividad(Mapa()::class.java)
        }
        return super.onContextItemSelected(item)
    }
    fun borraruno(idIng: Int,idRec:Int):Boolean
    {
        val db = Firebase.firestore
        db.collection("Ingrediente").document("A"+idRec+"I"+idIng).delete()
        return true
    }

    fun traertodo(idReceta:Int): ArrayList<IngredienteBean>
    {
        val arreglo = arrayListOf<IngredienteBean>()
        val db = Firebase.firestore
        db.collection("Ingrediente").whereEqualTo("idReceta","A"+idReceta)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val encontrado = IngredienteBean(0,0,"",0.0,"",0.0,0.0)

                    Log.d("que trajo", "${document.id} => ${document.data}")

                    try{
                        encontrado.id=document.data.toString().substringAfter("idIngrediente=").substringBefore(",").toInt()}
                    catch (ex:NumberFormatException)
                    {encontrado.id=document.data.toString().substringAfter("idIngrediente=").substringBefore("}").toInt()}

                    try{
                        encontrado.idReceta=document.data.toString().substringAfter("A").substringBefore(",").toInt()}
                    catch (ex:NumberFormatException)
                    {encontrado.idReceta=document.data.toString().substringAfter("A").substringBefore("}").toInt()}

                    try{
                        encontrado.nombre=document.data.toString().substringAfter("nombre=").substringBefore(",")}
                    catch (ex:NumberFormatException)
                    {encontrado.nombre=document.data.toString().substringAfter("nombre=").substringBefore("}")}

                    try{
                        encontrado.cantidad=document.data.toString().substringAfter("cantidad=").substringBefore(",").toDouble()}
                    catch (ex:NumberFormatException)
                    {encontrado.cantidad=document.data.toString().substringAfter("cantidad=").substringBefore("}").toDouble()}

                    try{
                        encontrado.unidad=document.data.toString().substringAfterLast("unidad=").substringBefore(",").substringBefore("}")}
                    catch (ex:NumberFormatException)
                    {encontrado.unidad=document.data.toString().substringAfterLast("unidad=").substringBefore("}")}

                    try{
                        encontrado.lat=document.data.toString().substringAfter("latitud=").substringBefore(",").toDouble()}
                    catch (ex:NumberFormatException)
                    {encontrado.lat=document.data.toString().substringAfter("latitud=").substringBefore("}").toDouble()}

                    try{
                        encontrado.lon=document.data.toString().substringAfterLast("longitud=").substringBefore(",").toDouble()}
                    catch (ex:NumberFormatException)
                    {encontrado.lon=document.data.toString().substringAfterLast("longitud=").substringBefore("}").toDouble()}

                    arreglo.add(encontrado)
                    Log.d("que trajo",arreglo.toString())
                }
            }
        return arreglo
    }

    fun abrirActividad(clase:Class<*>){
        val intendExplicito = Intent (
            this,
            clase)
        Log.i("examen","declarado e instanciado intend")
        val miId = Bundle()
        miId.putDouble("lat", arrIngredientes.get(posicionItemSeleccionado).lat)
        miId.putDouble("lon", arrIngredientes.get(posicionItemSeleccionado).lon)
        miId.putString("nombre",arrIngredientes.get(posicionItemSeleccionado).nombre)
        intendExplicito.putExtras(miId)
        startActivity(intendExplicito)
    }

    /*fun traerRec(): RecetaBean
    {
        val arreglo = RecetaBean(0,"")
        val db = Firebase.firestore
        db.collection("Receta").document("A"+aux)
            .get()
            .addOnSuccessListener { document ->
                    arreglo.nombre=document.data.toString().substringAfter("=").substringBefore(",")
                    arreglo.id=document.data.toString().substringAfterLast("=").substringBefore("}").toInt()
                }
        return arreglo
    }*/

    fun insertaruno(nuevo:IngredienteBean): Boolean
    {
        val db = Firebase.firestore
        val referencia = db.collection("Ingrediente").document("A"+nuevo.idReceta+"I"+i.toString())
        val nuevoReceta = hashMapOf<String, Any>(
            "idIngrediente" to i,
            "idReceta" to "A"+nuevo.idReceta,
            "nombre" to nuevo.nombre,
            "cantidad" to nuevo.cantidad,
            "unidad" to nuevo.unidad,
            "latitud" to nuevo.lat,
            "longitud" to nuevo.lon,
        )
        referencia.set(nuevoReceta)
        i++
        return true
    }

    fun modificar(actualizar:IngredienteBean): Boolean
    {
        val db = Firebase.firestore
        val referencia = db.collection("Ingrediente").document("A"+actualizar.idReceta+"I"+actualizar.id)
        val nuevoIngrediente = hashMapOf<String, Any>(
            "nombre" to actualizar.nombre,
            "idReceta" to "A"+actualizar.idReceta,
            "cantidad" to actualizar.cantidad,
            "unidad" to actualizar.unidad,
            "idIngrediente" to actualizar.id,
            "latitud" to actualizar.lat,
            "longitud" to actualizar.lon,
        )
        referencia.set(nuevoIngrediente)
        return true
    }
}