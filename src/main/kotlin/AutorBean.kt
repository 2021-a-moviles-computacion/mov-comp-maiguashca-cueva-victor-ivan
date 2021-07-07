class AutorBean
{
        var idAutor = 0
        var strNombreAutor: String? = null
        var premios: Boolean? = null

    constructor(idAutor: Int, strNombreAutor: String?, premios: Boolean) {
        this.idAutor = idAutor
        this.strNombreAutor = strNombreAutor
        this.premios = premios
    }


    override fun toString(): String {
            return ""+ idAutor + ";" +
                    strNombreAutor+";"+
                    premios+"\n"
        }
}
