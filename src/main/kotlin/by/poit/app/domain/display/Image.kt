package by.poit.app.domain.display

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import kotlin.math.round

class Image(val width: Int, val height: Int) {

    val zMap = Array(width) { DoubleArray(height) { Double.MAX_VALUE } }
    val drawable = BufferedImage(width, height, TYPE_INT_RGB)

    fun setPixel(x: Int, y: Int, z: Double, color: Color): Boolean {
        if (x < 0 || y < 0 || x >= width || y >= height) return false
        if (z < zMap[x][y]) {
            zMap[x][y] = z
            drawable.setRGB(x, y, color.rgb)
        }
        return true
    }

    fun setPixel(x: Double, y: Double, z: Double, color: Color): Boolean {
        return setPixel(round(x).toInt(), round(y).toInt(), z, color)
    }
}