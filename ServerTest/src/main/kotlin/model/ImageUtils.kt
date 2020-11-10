package model

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ImageUtils {
  companion object {
    fun convertImageToByteArray(image: BufferedImage): ByteArray {
      ByteArrayOutputStream().use {
        ImageIO.write(image, "png", it)
        return it.toByteArray()
      }
    }
  }
}