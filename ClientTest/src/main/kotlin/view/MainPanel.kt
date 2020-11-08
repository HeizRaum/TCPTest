package view

import java.awt.Color
import java.awt.TextArea
import javax.swing.JPanel

class MainPanel: JPanel() {
  private val textArea: TextArea = TextArea()

  init {
    this.initialize()
  }

  private fun initialize() {
    this.background = Color(100, 100, 100)

    this.add(textArea)
  }
}