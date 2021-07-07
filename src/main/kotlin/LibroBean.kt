class LibroBean {
    var idAutor = 0
    var strNombreLibro: String? = null
    var dateFecha: String? = null
    var fltPrecio = 0f
    var intGenero = 0

    constructor(idAutor: Int, strNombreLibro: String?, dateFecha: String?, fltPrecio: Float, intGenero: Int) {
        this.idAutor = idAutor
        this.strNombreLibro = strNombreLibro
        this.dateFecha = dateFecha
        this.fltPrecio = fltPrecio
        this.intGenero = intGenero
    }

    override fun toString(): String {
        return ""+ idAutor + ";" +
                strNombreLibro + ';' +
                dateFecha + ';' +
                fltPrecio + ';' +
                intGenero+"\n"
    }
}