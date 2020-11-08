import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.logging.Logger

class Server(
    port: Int
) {
  private val serverSocket: ServerSocket = ServerSocket(port)

  fun startServer() {
    Thread {
      Logger.getGlobal().info("Server started at port: ${serverSocket.localPort}")
      while(true) {
        val connection: Socket = this.serverSocket.accept()
        Logger.getGlobal().info("New connection made at: ${connection.localPort}")
        val connectionInput = DataInputStream(connection.getInputStream())
        val connectionOutput = DataOutputStream(connection.getOutputStream())
        while(connection.isConnected) {
          val request: String = connectionInput.readUTF();
          Logger.getGlobal().info("New request at: $request")
          when(request) {
            "steam/prices" -> {
              Logger.getGlobal().info("Server response...")
              connectionOutput.writeUTF("TESV:100us, TBOI:25us")
              break
            }
            else -> {
              Logger.getGlobal().info("Server response error!")
              connectionOutput.writeUTF("Error 404: Page not found.")
            }
          }
        }
      }
    }.start()
  }
}