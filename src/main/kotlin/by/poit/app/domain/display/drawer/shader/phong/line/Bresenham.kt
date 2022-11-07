package by.poit.app.domain.display.drawer.shader.phong.line

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.PixelLine
import java.awt.Color
import kotlin.math.abs
import kotlin.math.round

class Bresenham {

    fun draw(
        image: Image,
        from: Pixel,
        to: Pixel,
        color: (Pixel) -> Color
    ) {
        val line = image.intersection(PixelLine(from, to)) ?: return

        val delta = line.from.delta(line.to)
        val sameX = abs(delta.screen.x) < abs(delta.screen.y)

        val toX = round(line.to.screen.x)
        val toY = round(line.to.screen.y)

        val current = line.from.copy()
        current.screen.x = round(line.from.screen.x)
        current.screen.y = round(line.from.screen.y)

        val dx = abs(toX - current.screen.x)
        val dy = abs(toY - current.screen.y)
        val dz = abs(delta.screen.z)

        val sx = if (line.from.screen.x < line.to.screen.x) 1 else -1
        val sy = if (line.from.screen.y < line.to.screen.y) 1 else -1
        val sz = if (line.from.screen.z < line.to.screen.z) 1 else -1

        val deltaNormal: Vector3 = if (sameX) {
            delta.normal.multiply(1.0 / dy)
        } else {
            delta.normal.multiply(1.0 / dx)
        }

        var error = dx - dy

        while (current.screen.x != toX || current.screen.y != toY) {
            image.setPixel(current, color)

            val doubledError = error * 2
            if (doubledError > -dy) {
                current.screen.x += sx
                error -= dy

                if (!sameX) {
                    current.normal.add(deltaNormal)
                }
            }

            if (doubledError < dx) {
                current.screen.y += sy
                current.screen.z += sz * dz
                error += dx

                if (sameX) {
                    current.normal.add(deltaNormal)
                }
            }
        }
        image.setPixel(current, color)
    }
}