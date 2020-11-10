import model.GameCard
import model.ImageUtils
import java.sql.*
import java.util.logging.Logger

class Database {
  companion object {
    private const val DATABASE_URL =
      "jdbc:sqlite:C:\\Users\\LowenHerz\\Documents\\Universidad\\Semestre7\\SistemasDistribuidos\\Sesion2\\ServerTest\\files\\files.sqlite"

    val instance: Database = Database()

    fun initialize() {
      val request = """
      CREATE TABLE IF NOT EXISTS GameCards (
        id integer PRIMARY KEY,
        name text NOT NULL,
        image blob
      );
      """.trimIndent()

      connect()?.use {
        val statement: Statement = it.createStatement()
        statement.execute(request)
      }
    }

    private fun connect(): Connection? {
      var connection: Connection? = null
      try {
        connection = DriverManager.getConnection(DATABASE_URL)
      } catch (exception: SQLException) {
        print(exception.message)
      }
      return connection
    }
  }

  fun writeGameCard(gameCard: GameCard) {
    val request = """
      INSERT INTO GameCards(id, name, image) VALUES(?, ?, ?);
    """.trimIndent()

    connect()?.use {
      val statement = it.prepareStatement(request)
      statement.setInt(1, this.getGameCardCount(it))
      statement.setString(2, gameCard.name)
      statement.setBytes(3, ImageUtils.convertImageToByteArray(gameCard.image))
      statement.executeUpdate()
      Logger.getGlobal().info("Statement executed on database!")
    }
  }

  fun searchGame(gameName: String): GameCard? {
    val request = """
      SELECT name, image FROM GameCards WHERE name = ?
    """.trimIndent()

    connect()?.use {
      val statement = it.prepareStatement(request)
      statement.setString(1, gameName)

      val resultSet = statement.executeQuery()
      return try {
        GameCard(
          resultSet.getBytes("image"),
          resultSet.getString("name"),
        )
      } catch (exception: SQLException) {
        null
      }
    }
    return null
  }

  private fun getGameCardCount(connection: Connection): Int {
    val request = """
        SELECT COUNT(*) AS Cards FROM GameCards;
      """.trimIndent()

    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(request)
    return resultSet.getInt("Cards")
  }
}