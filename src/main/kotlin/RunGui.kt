import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class RunGui : JFrame("Aplicacion CRUD"), ActionListener {
    override fun actionPerformed(e: ActionEvent) {
        val a = e.actionCommand
        when (a) {
            "Autores" -> {
                val autores = GuiAutor()
                autores.defaultCloseOperation = EXIT_ON_CLOSE
            }
            "Libros" -> {
                val libros = GuiLibro()
                libros.defaultCloseOperation = EXIT_ON_CLOSE
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = RunGui()
            main.setDefaultCloseOperation(EXIT_ON_CLOSE)
        }
    }

    init {
        val panelGui0 = JPanel()
        panelGui0.layout = GridLayout(1, 2, 5, 10)
        val autores = JButton("Autores")
        autores.actionCommand = "Autores"
        autores.addActionListener(this)
        val libros = JButton("Libros")
        libros.actionCommand = "Libros"
        libros.addActionListener(this)

        panelGui0.add(autores)
        panelGui0.add(libros)
        val contenedor = this.contentPane
        contenedor.add(panelGui0, BorderLayout.CENTER)
        super.setBounds(0, 0, 440, 240)
        this.isVisible = true
    }
}