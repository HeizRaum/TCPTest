import model.GameCard
import java.awt.image.BufferedImage
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.logging.Logger
import javax.imageio.ImageIO

class Server(
  port: Int
) {
  private val serverSocket: ServerSocket = ServerSocket(port)

  fun startServer() {
    Database.initialize()

    Thread {
      Logger.getGlobal().info("Server started at port: ${serverSocket.localPort}")
      while (true) {
        val connection: Socket = this.serverSocket.accept()
        Logger.getGlobal().info("New connection made at: ${connection.localPort}")
        val connectionInput = DataInputStream(connection.getInputStream())
        val connectionOutput = DataOutputStream(connection.getOutputStream())
        while (connection.isConnected) {
          when (connectionInput.readUTF()) {
            StatusCodes.SEND_GAME_CARD.toString() -> {
              val gameCard: GameCard? = this.readGameCardFromClient(connectionInput, connectionOutput)
              if (gameCard != null) {
                Database.instance.writeGameCard(gameCard)
              }
            }
            StatusCodes.SEARCH_GAME.toString() -> {
              val gameName: String? = this.readStringFromClient(connectionInput, connectionOutput)
              if (gameName != null) {
                this.searchGameInDatabase(connectionInput, connectionOutput, gameName)
              }
            }
          }
        }
      }
    }.start()
  }

  private fun readGameCardFromClient(input: DataInputStream, output: DataOutputStream): GameCard? {
    val gameImage: BufferedImage? = this.readImageFromClient(input, output)
    val gameName: String? = this.readStringFromClient(input, output)

    return if (gameImage != null && gameName != null) {
      GameCard(gameImage, gameName)
    } else {
      null
    }
  }

  private fun readImageFromClient(input: DataInputStream, output: DataOutputStream): BufferedImage? {
    Logger.getGlobal().info("Reading image...")
    return ImageIO.read(input).also {
      this.sendStatusCodeToClient(input, output, StatusCodes.RECEIVE_IMAGE)
    }
  }

  private fun readStringFromClient(input: DataInputStream, output: DataOutputStream): String? {
    Logger.getGlobal().info("Reading string...")
    return input.readUTF().also {
      this.sendStatusCodeToClient(input, output, StatusCodes.RECEIVE_STRING)
    }
  }

  private fun searchGameInDatabase(input: DataInputStream, output: DataOutputStream, gameName: String) {
    val gameSearched: GameCard? = Database.instance.searchGame(gameName)
    if (gameSearched != null) {
      this.sendStatusCodeToClient(input, output, StatusCodes.FOUND_GAME)
      this.sendImageToClient(input, output, gameSearched.image)
      this.sendStringToClient(input, output, gameSearched.name)
    } else {
      this.sendStatusCodeToClient(input, output, StatusCodes.GAME_NOT_FOUND)
    }
  }

  private fun sendStatusCodeToClient(input: DataInputStream, output: DataOutputStream, code: StatusCodes) {
    input.skip(input.available().toLong())
    output.writeUTF(code.toString())
  }

  private fun sendImageToClient(input: DataInputStream, output: DataOutputStream, image: BufferedImage): StatusCodes {
    return try {
      ImageIO.write(image, "png", output)
      return StatusCodes.valueOf(input.readUTF())
    } catch (exception: IOException) {
      StatusCodes.ERROR
    }
  }

  private fun sendStringToClient(input: DataInputStream, output: DataOutputStream, string: String): StatusCodes {
    return try {
      output.writeUTF(string)
      return StatusCodes.valueOf(input.readUTF())
    } catch (exception: IOException) {
      StatusCodes.ERROR
    }
  }
}