package view

import controller.ClientController
import model.GameCard
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class AddGamePopupFrame : JFrame("Add game") {
  init {
    this.initialize()
  }

  private fun initialize() {
    this.add(AddGamePanel())

    this.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    this.isVisible = true
    this.pack()
    this.setLocationRelativeTo(null)
    this.revalidate()
  }

  private inner class AddGamePanel : JPanel(), MouseListener {
    private val gridBagConstraints = GridBagConstraints()

    private val gameCard = GameCard

    private val gameImagePanel = JPanel()
    private val gameNameTextField = JTextField()
    private val addGameButton = JButton("Add game")

    init {
      this.initializeComponents()
      this.initialize()
    }

    override fun mouseClicked(e: MouseEvent?) {
      this.loadGameImage()
    }

    override fun mousePressed(e: MouseEvent?) { }

    override fun mouseReleased(e: MouseEvent?) { }

    override fun mouseEntered(e: MouseEvent?) { }

    override fun mouseExited(e: MouseEvent?) { }

    private fun initializeComponents() {
      this.gameImagePanel.preferredSize = Dimension(150, 200)
      this.gameImagePanel.background = Color(120, 120, 120)
      this.gameImagePanel.addMouseListener(this)

      this.gameNameTextField.columns = 10
      this.gameNameTextField.font = Font("SansSerif", Font.PLAIN, 16)

      this.addGameButton.font = Font("SansSerif", Font.PLAIN, 16)
      this.addGameButton.addActionListener { this.addGame() }
    }

    private fun initialize() {
      this.layout = GridBagLayout()
      this.gridBagConstraints.gridx = 0
      this.gridBagConstraints.gridy = 0
      this.gridBagConstraints.insets = Insets(10, 10, 10, 10)

      this.add(this.gameImagePanel, this.gridBagConstraints)
      this.gridBagConstraints.gridy++
      this.add(this.gameNameTextField, this.gridBagConstraints)
      this.gridBagConstraints.gridy++
      this.add(this.addGameButton, this.gridBagConstraints)
    }

    private fun loadGameImage() {
      val fileChooser = JFileChooser()
      fileChooser.fileFilter = FileNameExtensionFilter(
        "PNG images",
        "png"
      )
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        this.gameCard.setImage(
          ImageIO.read(fileChooser.selectedFile),
          fileChooser.selectedFile.extension
        )
        this.gameImagePanel.add(JLabel(ImageIcon(this.gameCard.getImage())))
        this.gameImagePanel.revalidate()
      }
    }

    private fun addGame() {
      this.gameCard.name = this.gameNameTextField.text
      if(!this.gameCard.isEmpty()) {
        ClientController.instance.sendGameCardToServer(this.gameCard)
      }
    }
  }
}