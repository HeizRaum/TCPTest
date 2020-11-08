package view

import controller.StatusCodes
import controller.ViewController
import java.awt.*
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.TitledBorder

class ConnectionPanel: JPanel() {

  init {
    this.initialize()
  }

  private fun initialize() {
    this.background = Color(100, 100, 100)
    this.layout = GridBagLayout()
    this.add(PortInput())
  }

  inner class PortInput: JPanel() {
    private val gridBagConstraints = GridBagConstraints()
    private val portTextField = JTextField(20)

    init {
      this.layout = GridBagLayout()
      this.gridBagConstraints.gridx = 0
      this.gridBagConstraints.gridy = 0
      this.gridBagConstraints.insets = Insets(5, 5, 5, 5)

      this.portTextField.font = Font("SansSerif", Font.PLAIN, 14)

      this.add(
        JLabel("Port").also {
          it.font = Font("SansSerif", Font.BOLD, 16)
        },
        this.gridBagConstraints
      )

      this.gridBagConstraints.gridy++

      this.add(
        this.portTextField,
        this.gridBagConstraints
      )

      this.gridBagConstraints.gridy++

      this.add(JButton("Connect").also {
        it.font = Font("SansSerif", Font.PLAIN, 14)
        it.addActionListener { this.connectToServer() }
      }, this.gridBagConstraints)
    }

    private fun connectToServer() {
      when (ViewController.instance.connectToServer(this.portTextField.text)) {
        StatusCodes.BAD_PORT_STRING -> {
          JOptionPane.showMessageDialog(
            this,
            "The formatting of the written port is not correct.",
            "Bad port formatting",
            JOptionPane.ERROR_MESSAGE)
        }
        StatusCodes.CONNECTION_MADE -> {
          JOptionPane.showMessageDialog(
            this,
            "Connection reached with the server!",
            "Connection reached",
            JOptionPane.INFORMATION_MESSAGE
          )
          ViewController.instance.changeToMainPanel()
        }
        StatusCodes.SOCKET_CONNECTION_FAILED -> {
          JOptionPane.showMessageDialog(
            this,
            "The connection with the written port couldn't be made. Check the port or the server.",
            "Port connection failure",
            JOptionPane.ERROR_MESSAGE)
        }
        else -> {
          Logger.getGlobal().warning("Status code unknown!")
        }
      }
    }
  }
}