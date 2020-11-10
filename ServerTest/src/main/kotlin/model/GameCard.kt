package model

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

data class GameCard(
  var image: BufferedImage,
  var name: String
) {

  companion object {
    private fun convertBytesToImage(imageBytes: ByteArray): BufferedImage {
      ByteArrayInputStream(imageBytes).use {
        return ImageIO.read(it)
      }
    }
  }

  constructor(imageBytes: ByteArray, name: String): this(convertBytesToImage(imageBytes), name)
}