package controller

import model.GameCard
import view.MainFrame
import java.awt.image.BufferedImage
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.logging.Logger
import javax.imageio.ImageIO

class ClientController {
  companion object {
    val instance: ClientController = ClientController()
  }

  private val clientLocalIp: String = InetAddress.getLocalHost().hostAddress
  private var clientSocket: Socket? = null

  fun connectWithServer(port: Int): StatusCodes {
    this.clientSocket = Socket(this.clientLocalIp, port)
    Logger.getGlobal().info("New client launched!")
    return if(this.clientSocket!!.isConnected) {
      StatusCodes.CONNECTION_MADE
    } else {
      StatusCodes.SOCKET_CONNECTION_FAILED
    }
  }

  fun sendGameCardToServer(gameCard: GameCard): StatusCodes {
    this.sendCodeToServer(StatusCodes.SEND_GAME_CARD.toString())
    this.sendImageToServer(gameCard.getImage()!!, gameCard.getImageExtension()!!)
    return this.sendStringToServer(gameCard.name!!)
  }

  fun searchGameInDatabase(gameName: String): StatusCodes {
    this.sendCodeToServer(StatusCodes.SEARCH_GAME.toString())
    when(this.sendStringToServer(gameName)) {
      StatusCodes.RECEIVE_STRING -> {
        this.receiveGameCardFromDatabase()
      }
    }
    return StatusCodes.OK
  }

  private fun sendCodeToServer(code: String) {
    if(this.clientSocket != null) {
      if(this.clientSocket!!.isConnected) {
        val outputStream = DataOutputStream(this.clientSocket!!.getOutputStream())
        outputStream.writeUTF(code)
      }
    }
  }

  private fun sendImageToServer(image: BufferedImage, extension: String): StatusCodes {
    if(this.clientSocket != null) {
      if(this.clientSocket!!.isConnected) {
        val output = this.clientSocket!!.getOutputStream()
        val input = DataInputStream(this.clientSocket!!.getInputStream())
        Logger.getGlobal().info("Sending image to server...")
        ImageIO.write(image, extension, output)
        return StatusCodes.valueOf(input.readUTF())
      }
    }
    return StatusCodes.ERROR
  }

  private fun sendStringToServer(string: String): StatusCodes {
    if(this.clientSocket != null) {
      if(this.clientSocket!!.isConnected) {
        val output = DataOutputStream(this.clientSocket!!.getOutputStream())
        val input = DataInputStream(this.clientSocket!!.getInputStream())
        Logger.getGlobal().info("Sending string to server...")
        output.writeUTF(string)
        return StatusCodes.valueOf(input.readUTF())
      }
    }
    return StatusCodes.ERROR
  }

  private fun receiveGameCardFromDatabase() {
    if(this.clientSocket != null) {
      if(this.clientSocket!!.isConnected) {
        val input = DataInputStream(this.clientSocket!!.getInputStream())
        val output = DataOutputStream(this.clientSocket!!.getOutputStream())
        when(StatusCodes.valueOf(input.readUTF())) {
          StatusCodes.FOUND_GAME -> {
            val gameCard: GameCard? = this.readGameCardFromServer(input, output)
            if(gameCard != null) {
              ViewController.instance.showGameCardInSearchGamePopupFrame(gameCard)
            }
          }
          StatusCodes.GAME_NOT_FOUND -> {
            print("Game not found!")
          }
        }
      }
    }
  }

  private fun readGameCardFromServer(input: DataInputStream, output: DataOutputStream): GameCard? {
    val gameImage: BufferedImage? = this.readImageFromServer(input, output)
    val gameName: String? = this.readStringFromServer(input, output)

    return if (gameImage != null && gameName != null) {
      GameCard.apply {
        this.name = gameName
        this.setImage(gameImage, "png")
      }
    } else {
      null
    }
  }

  private fun readImageFromServer(input: DataInputStream, output: DataOutputStream): BufferedImage? {
    Logger.getGlobal().info("Reading image...")
    return ImageIO.read(input).also {
      this.sendStatusCodeToServer(input, output, StatusCodes.RECEIVE_IMAGE)
    }
  }

  private fun readStringFromServer(input: DataInputStream, output: DataOutputStream): String? {
    Logger.getGlobal().info("Reading string...")
    return input.readUTF().also {
      this.sendStatusCodeToServer(input, output, StatusCodes.RECEIVE_STRING)
    }
  }

  private fun sendStatusCodeToServer(input: DataInputStream, output: DataOutputStream, code: StatusCodes) {
    input.skip(input.available().toLong())
    output.writeUTF(code.toString())
  }
}