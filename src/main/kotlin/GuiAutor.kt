import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.*
import java.util.*
import javax.swing.*

class GuiAutor : JFrame("Aplicacion CRUD"), ActionListener {
    private val lblNombreAutor: JLabel
    private val lblIdAutor: JLabel
    private val lblPremios: JLabel
    private val txtNombreAutor: JTextField
    private val txtIdAutor: JTextField
    private val txaMostrar: JTextArea
    private val btnRegistrar: JButton
    private val btnGuardar: JButton
    private val btnActualizar: JButton
    private val btnEliminar: JButton
    private val btnMostrar: JButton
    private val btnCancelar: JButton
    private val btnSalir: JButton
    private val rdbSi: JRadioButton
    private val rdbNo: JRadioButton
    private val btgPremios: ButtonGroup

    private var editar = 0
    private var j = 0
    private var i = 0
    private var nuevo = 0

    private val listaEdit = ArrayList<String>()
    private val auxID = ArrayList<String>()

    private var line: String? = null

    var ruta = "C:\\Users\\AdminBeta\\Documents\\moviles\\"

    override fun actionPerformed(e: ActionEvent)
    {
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
                    txtIdAutor.text = auxID.last().toString()
                    txtIdAutor.isEnabled = false
                }
                else if(nuevo == 1)
                {
                    j = 0
                    var file = File(ruta+ "baseAutores.txt")
                    file.delete()
                    file.createNewFile()
                    listaEdit[i-1]=txtIdAutor.getText()
                    listaEdit[i]=txtNombreAutor.getText()
                    listaEdit[i+1]=rdbSi.isSelected.toString()
                    while (j < listaEdit.size)
                    {
                        var objAutor = AutorBean(
                            listaEdit.get(j).toInt(),
                            listaEdit.get(j+1),
                            listaEdit.get(j+2).toBoolean()
                        )
                        try {
                            val file = File(ruta+ "baseAutores.txt")
                            val fw = FileWriter(file, true)
                            val bw = BufferedWriter(fw)
                            bw.write(objAutor.toString())
                            bw.close()
                        } catch (ioException: IOException) {
                            ioException.printStackTrace()
                        }
                        j += 3
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
                else if ((txtNombreAutor.text == "") and (editar == 1)) {
                    JOptionPane.showMessageDialog(null, "Ingrese el nombre a buscar")
                }
                else if ((txtNombreAutor.text != "") and (editar == 1))
                {
                    listaEdit.clear()
                    try {
                        BufferedReader(FileReader(ruta + "baseAutores.txt")).use { br ->
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
                        if (txtNombreAutor.text == listaEdit[i]) break
                        i++
                    }
                    if (i == listaEdit.size) {
                        JOptionPane.showMessageDialog(null, "Autor no registrado")
                    } else {
                        txtIdAutor.text = listaEdit[i-1]
                        txtNombreAutor.text = listaEdit[i]
                        rdbSi.isSelected = listaEdit[i+1].toBoolean()
                        rdbNo.isSelected = !listaEdit[i+1].toBoolean()
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
                var file = File(ruta+ "baseAutores.txt")
                file.delete()
                file.createNewFile()
                listaEdit.removeAt(i-1)
                listaEdit.removeAt(i-1)
                listaEdit.removeAt(i-1)
                while (j < listaEdit.size)
                {
                    var objAutor = AutorBean(
                        listaEdit.get(j).toInt(),
                        listaEdit.get(j+1),
                        listaEdit.get(j+2).toBoolean()
                    )
                    try {
                        val file = File(ruta+ "baseAutores.txt")
                        val fw = FileWriter(file, true)
                        val bw = BufferedWriter(fw)
                        bw.write(objAutor.toString())
                        bw.close()
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                    j += 3
                }
            }
            "Mostrar" -> {
                try {
                    BufferedReader(FileReader(ruta + "baseAutores.txt")).use { br ->
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
                if (txtNombreAutor.text == "" ||
                    txtIdAutor.text == "" ||
                    btgPremios.selection == null
                )
                    JOptionPane.showMessageDialog(null, "Datos faltantes")
                else {
                    var objAuthor = AutorBean(
                        txtIdAutor.getText().toInt(),
                        txtNombreAutor.getText(),
                        rdbSi.isSelected
                    )
                    try {
                        val file = File(ruta + "baseAutores.txt")
                        val fw = FileWriter(file, true)
                        val bw = BufferedWriter(fw)
                        bw.write(objAuthor.toString())
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

        txtNombreAutor.text = ""
        txtIdAutor.text = ""
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
            val application = GuiAutor() //declarando e instanciando
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
        panelGui3.layout = GridLayout(3, 2, 5, 10)
        panelGui1.add(panelGui3)
        val panelGui4 = JPanel()
        panelGui4.layout = GridLayout(1, 2, 5, 10)
        val panelGui2 = JPanel()
        panelGui2.layout = GridLayout(1, 3, 5, 10)

        try {
            BufferedReader(FileReader(ruta + "baseAutores.txt")).use { br ->
                while (br.readLine().also { line = it } != null) {
                    val st = StringTokenizer(line, ";")
                    while (st.hasMoreTokens()) {
                        auxID.add(st.nextToken())
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        auxID.removeLast()
        auxID.removeLast()
        auxID[auxID.size-1]=(auxID.last().toInt()+1).toString()

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
        panelGui0.add(btnEliminar)
        btnMostrar = JButton("Mostrar")
        btnMostrar.actionCommand = "Mostrar"
        btnMostrar.addActionListener(this)
        panelGui0.add(btnMostrar)
        lblIdAutor = JLabel("Id Autor:")
        panelGui3.add(lblIdAutor)
        txtIdAutor = JTextField(10)
        txtIdAutor.isEnabled = false
        panelGui3.add(txtIdAutor)
        lblNombreAutor = JLabel("Nombre Autor:")
        panelGui3.add(lblNombreAutor)
        txtNombreAutor = JTextField(10)
        panelGui3.add(txtNombreAutor)
        lblPremios = JLabel("Premios:")
        panelGui3.add(lblPremios)
        rdbSi = JRadioButton("Uno o varios")
        panelGui4.add(rdbSi)
        rdbNo = JRadioButton("Ninguno")
        panelGui4.add(rdbNo)
        panelGui3.add(panelGui4)
        btgPremios = ButtonGroup()
        btgPremios.add(rdbSi)
        btgPremios.add(rdbNo)
        btgPremios.clearSelection()
        txaMostrar = JTextArea()
        panelGui1.add(txaMostrar)
        btnGuardar = JButton("Guardar")
        btnGuardar.actionCommand = "Guardar"
        btnGuardar.addActionListener(this)
        panelGui2.add(btnGuardar)
        btnCancelar = JButton("Cancelar")
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
        super.setBounds(100, 200, 480, 350)
        this.isVisible = true
    }
}