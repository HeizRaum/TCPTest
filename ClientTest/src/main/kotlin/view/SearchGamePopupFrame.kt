package view

import controller.ClientController
import model.GameCard
import java.awt.*
import javax.swing.*

class SearchGamePopupFrame : JFrame("Search game") {
  private val searchGamePanel: SearchGamePanel = SearchGamePanel()

  init {
    this.initialize()
  }

  private fun initialize() {
    this.add(this.searchGamePanel)

    this.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    this.isVisible = true
    this.pack()
    this.setLocationRelativeTo(null)
    this.revalidate()
  }

  fun setGameCardShown(gameCard: GameCard) {
    this.searchGamePanel.gameCard = gameCard
    this.searchGamePanel.gameImageLabel.icon = ImageIcon(gameCard.getImage())
    this.searchGamePanel.revalidate()
  }

  private inner class SearchGamePanel : JPanel() {
    private val gridBagConstraints = GridBagConstraints()

    private val searchGameTextField = JTextField()
    private val searchGameButton = JButton("Search game")

    private val gameImagePanel = JPanel()

    val gameImageLabel = JLabel()
    var gameCard = GameCard

    init {
      this.initializeComponents()
      this.initialize()
    }

    private fun initializeComponents() {
      this.gameImagePanel.preferredSize = Dimension(150, 200)
      this.gameImagePanel.background = Color(120, 120, 120)
      this.gameImagePanel.add(this.gameImageLabel)

      this.searchGameTextField.columns = 10
      this.searchGameTextField.font = Font("SansSerif", Font.PLAIN, 16)

      this.searchGameButton.font = Font("SansSerif", Font.PLAIN, 16)
      this.searchGameButton.addActionListener { this.searchGame() }
    }

    private fun initialize() {
      this.layout = GridBagLayout()
      this.gridBagConstraints.gridx = 0
      this.gridBagConstraints.gridy = 0
      this.gridBagConstraints.insets = Insets(10, 10, 10, 10)

      this.add(this.searchGameTextField, this.gridBagConstraints)
      this.gridBagConstraints.gridy++
      this.add(this.searchGameButton, this.gridBagConstraints)
      this.gridBagConstraints.gridy++
      this.add(this.gameImagePanel, this.gridBagConstraints)
    }

    private fun searchGame() {
      ClientController.instance.searchGameInDatabase(this.searchGameTextField.text)
    }
  }
}