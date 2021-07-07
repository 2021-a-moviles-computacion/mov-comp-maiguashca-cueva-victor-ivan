import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.*
import java.util.*
import javax.swing.*

class GuiLibro : JFrame("Aplicacion CRUD"), ActionListener {
    private val lblNombreAutor: JLabel
    private val lblNombreLibro: JLabel
    private val lblGenero: JLabel
    private val lblFechaPub: JLabel
    private val lblPrecio: JLabel

    private val txtNombreLibro: JTextField
    private val txtFechaPub: JTextField
    private val txtPrecio: JTextField

    private val cboGenero: JComboBox<*>
    private val cboNombreAutor: JComboBox<*>

    private val txaMostrar: JTextArea

    private val btnRegistrar: JButton
    private val btnGuardar: JButton
    private val btnActualizar: JButton
    private val btnEliminar: JButton
    private val btnMostrar: JButton
    private val btnCancelar: JButton
    private val btnSalir: JButton

    private val auxCbo0 = ArrayList<String>()
    private val listaCbo = ArrayList<String>()
    private val auxCbo1 = ArrayList<String>()
    private val listaEdit = ArrayList<String>()

    private var editar = 0
    private var j = 0
    private var i = 0
    private var nuevo = 0
    private val strGenero =
        arrayOf("Seleccione un Genero","Romantico", "Realista", "Terror", "Aventura", "Bélico", "Fantastico", "Historico", "Humoristico")

    private var line: String? = null

    var ruta = "C:\\Users\\AdminBeta\\Documents\\moviles\\"

    override fun actionPerformed(e: ActionEvent) {
        val a = e.actionCommand
        when (a)
        {
            "Nuevo" ->
            {
                if(nuevo==0)
                {
                    limpiarGui()
                    btnActualizar.isEnabled = false
                    btnEliminar.isEnabled = false
                    btnGuardar.isEnabled = true
                }
                else if(nuevo == 1)
                {
                    j = 0
                    var file = File(ruta+ "baseLibros.txt")
                    file.delete()
                    file.createNewFile()
                    listaEdit[i-1]=cboNombreAutor.selectedIndex.toString()
                    listaEdit[i]=txtNombreLibro.getText()
                    listaEdit[i+1]=txtFechaPub.getText()
                    listaEdit[i+2]=txtPrecio.getText()
                    listaEdit[i+3]= cboGenero.selectedIndex.toString()
                    while (j < listaEdit.size)
                    {
                        var objLibro = LibroBean(
                            listaEdit.get(j).toInt(),
                            listaEdit.get(j+1),
                            listaEdit.get(j+2),
                            listaEdit.get(j+3).toFloat(),
                            listaEdit.get(j+4).toInt()
                        )
                        try {
                            val file = File(ruta+ "baseLibros.txt")
                            val fw = FileWriter(file, true)
                            val bw = BufferedWriter(fw)
                            bw.write(objLibro.toString())
                            bw.close()
                        } catch (ioException: IOException) {
                            ioException.printStackTrace()
                        }
                        j += 5
                    }
                    nuevo--
                    btnRegistrar.text = "Nuevo"
                }
            }
            "Editar" ->
            {
                if (editar == 0) {
                    editar++
                    nuevo++
                    btnActualizar.text = "Buscar"
                    btnRegistrar.isEnabled = true
                    btnRegistrar.text = "Actualizar"
                    btnEliminar.isEnabled = false
                }
                else if ((txtNombreLibro.text == "") and (editar == 1)) {
                    JOptionPane.showMessageDialog(null, "Ingrese el nombre a buscar")
                }
                else if ((txtNombreLibro.text != "") and (editar == 1))
                {
                    listaEdit.clear()
                    try {
                        BufferedReader(FileReader(ruta + "baseLibros.txt")).use { br ->
                            while (br.readLine().also { line = it } != null) {
                                val st = StringTokenizer(line, ";")
                                while (st.hasMoreTokens())
                                {
                                    listaEdit.add(st.nextToken())
                                }
                            }
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    i = 0
                    while (i < listaEdit.size) {
                        if (txtNombreLibro.text == listaEdit[i]) break
                        i++
                    }
                    if (i == listaEdit.size) {
                        JOptionPane.showMessageDialog(null, "libro no registrado")
                    } else {
                        cboNombreAutor.selectedIndex = listaEdit[i-1].toInt()
                        txtNombreLibro.text = listaEdit[i]
                        cboGenero.selectedIndex = listaEdit[i+3].toInt()
                        txtFechaPub.text = listaEdit[i+1]
                        txtPrecio.text = listaEdit[i+2]
                        editar--
                        btnActualizar.text = "Editar"
                        btnEliminar.isEnabled = true
                        btnGuardar.isEnabled = true
                    }
                }
            }
            "Eliminar" ->
            {
                j = 0
                var file = File(ruta+ "baseLibros.txt")
                file.delete()
                file.createNewFile()
                listaEdit.removeAt(i-1)
                listaEdit.removeAt(i-1)
                listaEdit.removeAt(i-1)
                listaEdit.removeAt(i-1)
                listaEdit.removeAt(i-1)
                while (j < listaEdit.size)
                {
                    var objLibro = LibroBean(
                        listaEdit.get(j).toInt(),
                        listaEdit.get(j+1),
                        listaEdit.get(j+2),
                        listaEdit.get(j+3).toFloat(),
                        listaEdit.get(j+4).toInt()
                    )
                    try {
                        val file = File(ruta+ "baseLibros.txt")
                        val fw = FileWriter(file, true)
                        val bw = BufferedWriter(fw)
                        bw.write(objLibro.toString())
                        bw.close()
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                    j += 5
                }
            }
            "Mostrar" -> {
                try {
                    BufferedReader(FileReader(ruta + "baseLibros.txt")).use { br ->
                        while (br.readLine().also { line = it } != null) {
                            txaMostrar.append(line + "\n")
                        }
                    }
                } catch (fileNotFoundException: FileNotFoundException) {
                    fileNotFoundException.printStackTrace()
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                }
            }
            "Guardar" ->
            {
                if (txtNombreLibro.text == "" ||
                    txtPrecio.text == "" ||
                    cboGenero.selectedIndex == 0 ||
                    txtFechaPub.text == "" ||
                    cboNombreAutor.selectedIndex == 0
                )
                    JOptionPane.showMessageDialog(null, "Datos faltantes")
                else {
                    var objLibero = LibroBean(
                        cboNombreAutor.selectedIndex,
                        txtNombreLibro.getText(),
                        txtFechaPub.getText(),
                        txtPrecio.getText().toFloat(),
                        cboGenero.selectedIndex
                    )
                    try {
                        val file = File(ruta + "baseLibros.txt")
                        val fw = FileWriter(file, true)
                        val bw = BufferedWriter(fw)
                        bw.write(objLibero.toString())
                        bw.close()
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                }
            }
            "Cancelar" ->
            {
                limpiarGui()
            }
            "Salir" ->
            {
                System.exit(1)
            }
        }
    }
    fun limpiarGui() {
        cboGenero.setSelectedIndex(0)
        txtPrecio.text = ""
        txtFechaPub.text = ""
        txtNombreLibro.text = ""
        cboNombreAutor.setSelectedIndex(0)
        btnEliminar.isEnabled = false
        btnGuardar.isEnabled = false
        editar = 0
        i=0
        txaMostrar.text=""
        btnRegistrar.isEnabled = true
        btnActualizar.isEnabled = true
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val application = GuiLibro()
            application.defaultCloseOperation = EXIT_ON_CLOSE
        }
    }

    init {
        val contenedor = contentPane
        contenedor.setBounds(10, 200, 100, 100)
        contenedor.background = Color.lightGray
        val panelGui0 = JPanel()
        panelGui0.layout = GridLayout(1, 4, 5, 10)
        val panelGui1 = JPanel()
        panelGui1.layout = GridLayout(2, 1, 5, 10)
        val panelGui3 = JPanel()
        panelGui3.layout = GridLayout(5, 2, 5, 10)
        panelGui1.add(panelGui3)
        val panelGui2 = JPanel()
        panelGui2.layout = GridLayout(1, 3, 5, 10)

        //lblNombre = new JLabel("Autor - Libro");
        btnRegistrar = JButton("Nuevo")
        btnRegistrar.actionCommand = "Nuevo"
        btnRegistrar.addActionListener(this)
        panelGui0.add(btnRegistrar)
        btnActualizar = JButton("Editar")
        btnActualizar.actionCommand = "Editar"
        btnActualizar.addActionListener(this)
        panelGui0.add(btnActualizar)
        btnEliminar = JButton("Eliminar")
        btnEliminar.actionCommand = "Eliminar"
        btnEliminar.addActionListener(this)
        btnEliminar.isEnabled = false
        panelGui0.add(btnEliminar)
        btnMostrar = JButton("Mostrar")
        btnMostrar.actionCommand = "Mostrar"
        btnMostrar.addActionListener(this)
        panelGui0.add(btnMostrar)
        lblNombreAutor = JLabel("Nombre Autor:")
        panelGui3.add(lblNombreAutor)
        try {
            BufferedReader(FileReader(ruta + "baseAutores.txt")).use { br ->
                while (br.readLine().also { line = it } != null) {
                    val st = StringTokenizer(line, ";")
                    while (st.hasMoreTokens()) {
                        auxCbo0.add(st.nextToken())
                        listaCbo.add(st.nextToken())
                        auxCbo1.add(st.nextToken())
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        cboNombreAutor = JComboBox<Any?>()
        cboNombreAutor.maximumRowCount = 5
        cboNombreAutor.addItem("Seleccione un Autor.")
        for (i in listaCbo.indices) {
            cboNombreAutor.addItem(listaCbo[i])
        }
        cboNombreAutor.addActionListener { // TODO Auto-generated method stub
            cboNombreAutor.selectedIndex
        }
        panelGui3.add(cboNombreAutor)
        lblNombreLibro = JLabel("Nombre Libro:")
        panelGui3.add(lblNombreLibro)
        txtNombreLibro = JTextField(10)
        panelGui3.add(txtNombreLibro)
        lblGenero = JLabel("Genero:")
        panelGui3.add(lblGenero)
        cboGenero = JComboBox<Any?>(strGenero)
        cboGenero.addActionListener { // TODO Auto-generated method stub
            cboGenero.selectedIndex
        }
        panelGui3.add(cboGenero)
        lblFechaPub = JLabel("Fecha de Publicacion:")
        panelGui3.add(lblFechaPub)
        txtFechaPub = JTextField(10)
        panelGui3.add(txtFechaPub)
        lblPrecio = JLabel("Precio")
        panelGui3.add(lblPrecio)
        txtPrecio = JTextField(10)
        panelGui3.add(txtPrecio)
        txaMostrar = JTextArea()
        panelGui1.add(txaMostrar)
        btnGuardar = JButton("Guardar")
        btnGuardar.actionCommand = "Guardar"
        btnGuardar.addActionListener(this)
        btnGuardar.isEnabled = false
        panelGui2.add(btnGuardar)
        btnCancelar = JButton("limpiar")
        btnCancelar.actionCommand = "Cancelar"
        btnCancelar.addActionListener(this)
        panelGui2.add(btnCancelar)
        btnSalir = JButton("Salir")
        btnSalir.actionCommand = "Salir"
        btnSalir.addActionListener(this)
        panelGui2.add(btnSalir)
        contenedor.add(panelGui0, BorderLayout.NORTH)
        contenedor.add(panelGui1, BorderLayout.CENTER)
        contenedor.add(panelGui2, BorderLayout.SOUTH)
        super.setBounds(100, 200, 480, 500)
        this.isVisible = true
    }
}