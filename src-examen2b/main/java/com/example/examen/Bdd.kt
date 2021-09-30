package com.example.examen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Bdd(contexto: Context?)
    : SQLiteOpenHelper(contexto,"examen2",null,1)
{

    override fun onCreate(db: SQLiteDatabase?) {
        val sciptCrearTabla1=
            """
            CREATE TABLE RECETA (
            IDRECETA INTEGER PRIMARY KEY AUTOINCREMENT,
            RECETA VARCHAR(20) NOT NULL)
            """.trimIndent()
        val sciptCrearTabla2="""
            CREATE TABLE INGREDIENTE (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            IDRECETA INTEGER NOT NULL,
            INGREDIENTE VARCHAR(20) NOT NULL,
            CANTIDAD FLOAT NOT NULL,
            UNIDAD VARCHAR(10) NOT NULL)
        """.trimIndent()
        db?.execSQL(sciptCrearTabla1)
        db?.execSQL(sciptCrearTabla2)
    }
    fun insertarReceta(nombre: String): Boolean{
        val conexionEscritura = writableDatabase
        val valoresInsertar = ContentValues()
        valoresInsertar.put("RECETA",nombre)
        val resultado: Long = conexionEscritura.insert(
            "RECETA",
            null,
            valoresInsertar
        )
        conexionEscritura.close()
        return if(resultado.toInt() == -1) false else true
    }

    fun consutarRecetas(): ArrayList<RecetaBean> {
        val scripConsultaReceta = "SELECT * FROM RECETA"
        val lecturaBdd = readableDatabase
        val resultado = lecturaBdd.rawQuery(
            scripConsultaReceta,
            null
        )
        val existe = resultado.moveToFirst()
        val arreglo = arrayListOf<RecetaBean>()

        var i =0
        if (existe){
            do {
                val id = resultado.getInt(0)
                val nombre = resultado.getString(1)
                val encontrado = RecetaBean(0,"")
                if(id!=null){
                    encontrado.id=id
                    encontrado.nombre=nombre
                    arreglo.add(encontrado)
                }
            }while (resultado.moveToNext())
        }
        resultado.close()
        lecturaBdd.close()
        return arreglo
    }

    fun consutarRecetaId(id: Int): RecetaBean{
        val scripConsultaReceta = "SELECT * FROM RECETA WHERE IDRECETA = ${id}"
        val lecturaBdd = readableDatabase
        val resultado = lecturaBdd.rawQuery(
            scripConsultaReceta,
            null
        )
        val existe = resultado.moveToFirst()
        val encontrado = RecetaBean(0,"")

        if (existe){
            do {
                val id = resultado.getInt(0)
                val nombre = resultado.getString(1)
                if(id!=null){
                    encontrado.id=id
                    encontrado.nombre=nombre
                }
            } while (resultado.moveToNext())
        }
        resultado.close()
        lecturaBdd.close()
        return encontrado
    }

    fun eliminarReceta(id: Int): Boolean{
        val conexionEscritura = writableDatabase
        val resultdo = conexionEscritura.delete("RECETA",
            "IDRECETA=?",
            arrayOf(id.toString()
            )
        )
        conexionEscritura.close()
        return if(resultdo.toInt() == -1) false else true

    }
    fun actualizarReceta(id: Int, nombre: String): Boolean{
        val conexion = writableDatabase
        val actualizar = ContentValues()
        actualizar.put("RECETA",nombre)
        val resultado = conexion.update("RECETA",
            actualizar,
            "IDRECETA=?",
            arrayOf(
                id.toString()
            )
        )
        conexion.close()
        return if(resultado.toInt() == -1) false else true
    }

    fun insertarIngrediente(insertar: IngredienteBean): Boolean{
        val conexionEscritura = writableDatabase
        val valoresInsertar = ContentValues()
        valoresInsertar.put("IDRECETA",insertar.idReceta)
        valoresInsertar.put("INGREDIENTE",insertar.nombre)
        valoresInsertar.put("CANTIDAD",insertar.cantidad)
        valoresInsertar.put("UNIDAD",insertar.unidad)
        val resultado: Long = conexionEscritura.insert(
            "INGREDIENTE",
            null,
            valoresInsertar
        )
        conexionEscritura.close()
        return if(resultado.toInt() == -1) false else true
    }

    fun consutarIngredientes(id:Int): ArrayList<IngredienteBean> {
        val scripConsultaIngrediente = "SELECT * FROM INGREDIENTE WHERE IDRECETA =${id}"
        val lecturaBdd = readableDatabase
        val resultado = lecturaBdd.rawQuery(
            scripConsultaIngrediente,
            null
        )
        val existe = resultado.moveToFirst()
        val arreglo = arrayListOf<IngredienteBean>()

        if (existe){
            do {
                val id = resultado.getInt(0)
                val idrec = resultado.getInt(1)
                val nombre = resultado.getString(2)
                val cantidad = resultado.getFloat(3)
                val unidad = resultado.getString(4)
                var encontrado = IngredienteBean(0,0,"",0.1,"",0.0,0.0)
                if(id!=null){
                    encontrado.id=id
                    encontrado.idReceta=idrec
                    encontrado.nombre=nombre
                    encontrado.cantidad= cantidad.toDouble()
                    encontrado.unidad=unidad
                    arreglo.add(encontrado)
                }
            }while (resultado.moveToNext())
        }
        resultado.close()
        lecturaBdd.close()
        return arreglo
    }

    fun eliminaringrediente(id: Int): Boolean{
        val conexionEscritura = writableDatabase
        val resultdo = conexionEscritura.delete("INGREDIENTE",
            "id=?",
            arrayOf(id.toString()
            )
        )
        conexionEscritura.close()
        return if(resultdo.toInt() == -1) false else true

    }

    fun actualizarIngrediente(edicion:IngredienteBean): Boolean{
        val conexion = writableDatabase
        val actualizar = ContentValues()
        actualizar.put("INGREDIENTE",edicion.nombre)
        actualizar.put("CANTIDAD",edicion.cantidad)
        actualizar.put("UNIDAD",edicion.unidad)
        val resultado = conexion.update("INGREDIENTE",
            actualizar,
            "ID=?",
            arrayOf(
                edicion.id.toString()
            )
        )
        conexion.close()
        return if(resultado.toInt() == -1) false else true
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}