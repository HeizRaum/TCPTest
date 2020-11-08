package controller

import view.MainFrame
import java.net.ConnectException

class ViewController private constructor() {
  companion object {
    val instance = ViewController()
  }

  private val mainFrame: MainFrame = MainFrame("Client Test")

  fun connectToServer(rawPortString: String): StatusCodes {
    return try {
      val port = rawPortString.toInt()
      ClientController.instance.connectWithServer(port)
    } catch (exception: NumberFormatException) {
      StatusCodes.BAD_PORT_STRING
    } catch (exception: ConnectException) {
      StatusCodes.SOCKET_CONNECTION_FAILED
    }
  }

  fun changeToMainPanel() {
    mainFrame.remove(mainFrame.getConnectionPanel())
    mainFrame.add(mainFrame.getMainPanel())
  }
}