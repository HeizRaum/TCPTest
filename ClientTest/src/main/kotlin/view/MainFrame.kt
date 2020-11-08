package view

import java.awt.Component
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.UIManager
import javax.swing.WindowConstants

class MainFrame(title: String) : JFrame(title) {
  private val connectionPanel: ConnectionPanel = ConnectionPanel()
  private val mainPanel: MainPanel = MainPanel()

  init {
    this.initialize()
  }

  fun getConnectionPanel() = this.connectionPanel
  fun getMainPanel() = this.mainPanel

  override fun add(comp: Component?): Component {
    return super.add(comp).also {
      this.revalidate()
    }
  }

  private fun initialize() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    this.setSize(500, 500)
    this.add(this.connectionPanel)

    this.setLocationRelativeTo(null)
    this.isVisible = true

    this.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
  }
}