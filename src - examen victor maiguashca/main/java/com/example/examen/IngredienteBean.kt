package com.example.examen

class IngredienteBean(
    var id:Int,
    var idReceta: Int,
    var nombre: String,
    var cantidad: Double,
    var unidad: String
)
{
    override fun toString(): String {
        return nombre + " - " + cantidad + " - "  + unidad
    }
}