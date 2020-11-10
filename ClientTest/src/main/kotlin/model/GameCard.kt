package model

import java.awt.image.BufferedImage

object GameCard {
  private var image: BufferedImage? = null
  private var imageExtension: String? = null
  var name: String? = null

  fun setImage(image: BufferedImage, imageExtension: String) {
    this.image = image
    this.imageExtension = imageExtension
  }

  fun getImage(): BufferedImage? {
    return image
  }

  fun getImageExtension(): String? {
    return imageExtension
  }

  fun isEmpty(): Boolean {
    return this.image == null || this.name == null
  }
}