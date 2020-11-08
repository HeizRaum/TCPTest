package controller

import java.net.InetAddress
import java.net.Socket
import java.util.logging.Logger

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
}