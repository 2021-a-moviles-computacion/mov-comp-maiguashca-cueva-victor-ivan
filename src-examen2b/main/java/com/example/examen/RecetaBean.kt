package com.example.examen

import android.os.Parcel
import android.os.Parcelable

class RecetaBean(var id: Int,
                 var nombre: String)
{
    override fun toString(): String {
        return "" + id + " - "+ nombre
    }
}