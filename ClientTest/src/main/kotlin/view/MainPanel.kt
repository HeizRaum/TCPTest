package view

import java.awt.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class MainPanel: JPanel() {


  init {
    this.initialize()
  }

  private fun initialize() {
    this.background = Color(100, 100, 100)
    this.layout = GridBagLayout()
    this.add(ButtonMenu())
  }

  inner class ButtonMenu: JPanel() {
    private val gridBagConstraints = GridBagConstraints()

    private val addGameButton = JButton("Add game card...")
    private val searchGameButton = JButton("Search game...")

    init {
      this.layout = GridBagLayout()
      this.gridBagConstraints.gridx = 0
      this.gridBagConstraints.gridy = 0
      this.gridBagConstraints.insets = Insets(5, 5, 5, 5)

      this.add(JButton("Add game...").also {
        it.font = Font("SansSerif", Font.PLAIN, 14)
        it.addActionListener { this.addGame() }
      }, this.gridBagConstraints)

      this.gridBagConstraints.gridy++

      this.add(JButton("Search game...").also {
        it.font = Font("SansSerif", Font.PLAIN, 14)
        it.addActionListener { this.searchGame() }
      }, this.gridBagConstraints)
    }

    private fun addGame() {
      AddGameInnerFrame()
    }

    private fun searchGame() {

    }
  }
}