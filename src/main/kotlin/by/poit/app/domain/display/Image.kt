package by.poit.app.domain.display

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.structure.PixelLine
import by.poit.app.domain.model.structure.Rectangle
import by.poit.app.domain.model.structure.VectorLine
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import kotlin.math.roundToInt

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
        return setPixel(x.roundToInt(), y.roundToInt(), z, color)
    }

    fun setPixel(pixel: Pixel, color: (Pixel) -> Color) {
        setPixel(
            pixel.screen.x,
            pixel.screen.y,
            pixel.screen.z,
            color(pixel)
        )
    }

    fun intersection(line: PixelLine): PixelLine? {
        if (line.from.screen.z < 0 && line.to.screen.z < 0 || line.from.screen.z > 1 && line.to.screen.z > 1) return null

        val screen = Rectangle(0.0, 0.0, width.toDouble(), height.toDouble())

        val from = line.from to screen.contains(line.from.screen.x, line.from.screen.y)
        val to = line.to to screen.contains(line.to.screen.x, line.to.screen.y)
        val left = line.atX(screen.left).let { it to screen.containsY(it.screen.y) }
        val top = line.atY(screen.top).let { it to screen.containsX(it.screen.x) }
        val right = line.atX(screen.right).let { it to screen.containsY(it.screen.y) }
        val bottom = line.atY(screen.bottom).let { it to screen.containsX(it.screen.x) }

        if (from.second) {
            if (to.second) return line
            if (from.first.screen.x < to.first.screen.x) {
                if (right.second) return PixelLine(from.first, right.first)
            } else {
                if (left.second) return PixelLine(from.first, left.first)
            }
            if (from.first.screen.y < to.first.screen.y) {
                if (bottom.second) return PixelLine(from.first, bottom.first)
            } else {
                if (top.second) return PixelLine(from.first, top.first)
            }
        } else {
            if (to.second) {
                if (from.first.screen.x > to.first.screen.x) {
                    if (right.second) return PixelLine(to.first, right.first)
                } else {
                    if (left.second) return PixelLine(to.first, left.first)
                }
                if (from.first.screen.y > to.first.screen.y) {
                    if (bottom.second) return PixelLine(to.first, bottom.first)
                } else {
                    if (top.second) return PixelLine(to.first, top.first)
                }
            } else {
                val contained =
                    listOf(left, top, right, bottom)
                        .filter { it.second && it.first.isBetween(from.first, to.first) }
                        .map { it.first }

                if (contained.size < 2)
                    return null

                return PixelLine(contained[0], contained[1])
            }
        }

        return null
    }

    fun intersection(line: VectorLine): VectorLine? {
        if (line.from.z < 0 && line.to.z < 0 || line.from.z > 1 && line.to.z > 1) return null

        val screen = Rectangle(0.0, 0.0, width.toDouble(), height.toDouble())

        val from = line.from to screen.contains(line.from.x, line.from.y)
        val to = line.to to screen.contains(line.to.x, line.to.y)
        val left = line.atX(screen.left).let { it to screen.containsY(it.y) }
        val top = line.atY(screen.top).let { it to screen.containsX(it.x) }
        val right = line.atX(screen.right).let { it to screen.containsY(it.y) }
        val bottom = line.atY(screen.bottom).let { it to screen.containsX(it.x) }

        if (from.second) {
            if (to.second) return line
            if (from.first.x < to.first.x) {
                if (right.second) return VectorLine(from.first, right.first)
            } else {
                if (left.second) return VectorLine(from.first, left.first)
            }
            if (from.first.y < to.first.y) {
                if (bottom.second) return VectorLine(from.first, bottom.first)
            } else {
                if (top.second) return VectorLine(from.first, top.first)
            }
        } else {
            if (to.second) {
                if (from.first.x > to.first.x) {
                    if (right.second) return VectorLine(to.first, right.first)
                } else {
                    if (left.second) return VectorLine(to.first, left.first)
                }
                if (from.first.y > to.first.y) {
                    if (bottom.second) return VectorLine(to.first, bottom.first)
                } else {
                    if (top.second) return VectorLine(to.first, top.first)
                }
            } else {
                val contained =
                    listOf(left, top, right, bottom)
                        .filter { it.second }
                        .map { it.first }

                if (contained.size < 2) return null

                return VectorLine(contained[0], contained[1])
            }
        }

        return null
    }
}