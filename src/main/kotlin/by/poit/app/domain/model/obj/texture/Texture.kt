package by.poit.app.domain.model.obj.texture

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.IIOException
import javax.imageio.ImageIO
import kotlin.math.roundToInt


open class Texture(private val image: BufferedImage) {

    private val width = image.width
    private val height = image.height

    companion object {

        fun from(file: File?): Texture? {
            return try {
                file?.let { ImageIO.read(file) }?.let { Texture(it) }
            } catch (ignored: IIOException) {
                null
            }
        }
    }

    fun color(pixel: Pixel): Color {
        return color(pixel.texel.x, pixel.texel.y)
    }

    fun color(x: Double, y: Double): Color {
        return Color(
            image.getRGB(
                coerce((x * width).roundToInt(), width),
                coerce(((1 - y) * height).roundToInt(), height)
            )
        )
    }

    fun value(pixel: Pixel): Vector3 {
        return value(pixel.texel.x, pixel.texel.y)
    }

    fun value(x: Double, y: Double): Vector3 {
        val rgb = image.getRGB(
            coerce((x * width).roundToInt(), width),
            coerce(((1 - y) * height).roundToInt(), height)
        )
        return Vector3(
            ((rgb shr 16) and 0xFF) / 256.0,
            ((rgb shr 8) and 0xFF) / 256.0,
            (rgb and 0xFF) / 256.0,
        )
    }

    private fun coerce(value: Int, range: Int): Int {
        return (value % range + range) % range
    }
}