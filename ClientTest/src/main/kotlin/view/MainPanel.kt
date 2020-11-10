package view

import java.awt.*
import java.awt.image.BufferedImage
import java.nio.file.WatchEvent
import java.nio.file.WatchService
import java.nio.file.Watchable
import javax.swing.JButton
import javax.swing.JPanel

class MainPanel: JPanel() {
  lateinit var addGamePopupFrame: AddGamePopupFrame
  lateinit var searchGamePopupFrame: SearchGamePopupFrame

  init {
    this.initialize()
  }

  private fun initialize() {
    this.background = Color(100, 100, 100)
    this.layout = GridBagLayout()
    this.add(ButtonMenu())
  }

  private inner class ButtonMenu: JPanel() {
    private val gridBagConstraints = GridBagConstraints()

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
      addGamePopupFrame = AddGamePopupFrame()
    }

    private fun searchGame() {
      searchGamePopupFrame = SearchGamePopupFrame()
    }
  }
}