package by.poit.app.domain.display.drawer.line

import by.poit.app.domain.display.Image
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Line
import by.poit.app.domain.model.structure.Rectangle
import java.awt.Color
import kotlin.math.roundToInt

class Bresenham : LineDrawer {

    private fun sign(x: Double): Int {
        return if (x > 0) 1 else if (x < 0) -1 else 0
    }

    override fun draw(image: Image, from: Vector3, to: Vector3, color: Color) {
        val line = intersection(
            Line(from, to), Rectangle(
                0.0, 0.0,
                image.width.toDouble(),
                image.height.toDouble()
            )
        ) ?: return

        val pdx: Int
        val pdy: Int
        val es: Double
        val el: Double
        var dx = line.to.x - line.from.x
        var dy = line.to.y - line.from.y

        val incx = sign(dx)
        val incy = sign(dy)

        if (dx < 0) dx = -dx
        if (dy < 0) dy = -dy

        if (dx > dy) {
            pdx = incx
            pdy = 0
            es = dy
            el = dx
        } else {
            pdx = 0
            pdy = incy
            es = dx
            el = dy
        }
        val dz = (line.to.z - line.from.z) / el

        var x = line.from.x.roundToInt()
        var y = line.from.y.roundToInt()
        var z = line.from.z
        var err = el / 2
        image.setPixel(x, y, z, color)
        for (i in 0 until el.roundToInt()) {
            err -= es
            if (err < 0) {
                err += el
                x += incx
                y += incy
            } else {
                x += pdx
                y += pdy
            }
            z += dz
            image.setPixel(x, y, z, color)
        }
    }

    private fun intersection(line: Line, screen: Rectangle): Line? {
        if (line.from.z < 0 && line.to.z < 0 || line.from.z > 1 && line.to.z > 1) return null
        return line
        val checkX: (vector: Vector3, asFrom: Boolean) -> Line? =
            check@{ vector, asFrom ->
                if (screen.containsX(vector.x))
                    if (asFrom) return@check Line(vector, line.to)
                    else return@check Line(line.from, vector)
                return@check null
            }
        val checkY: (vector: Vector3, asFrom: Boolean) -> Line? =
            check@{ vector, asFrom ->
                if (screen.containsY(vector.y))
                    if (asFrom) return@check Line(vector, line.to)
                    else return@check Line(line.from, vector)
                return@check null
            }

        if (screen.contains(line.from.x, line.from.y)) {

            if (screen.contains(line.to.x, line.to.y)) return line

            checkY(line.atX(screen.right), false)?.let { return it }
            checkY(line.atX(screen.left), false)?.let { return it }
            checkX(line.atY(screen.top), false)?.let { return it }
            checkX(line.atY(screen.bottom), false)?.let { return it }

        } else {

            if (screen.contains(line.to.x, line.to.y)) {
                checkY(line.atX(screen.right), true)?.let { return it }
                checkY(line.atX(screen.left), true)?.let { return it }
                checkX(line.atY(screen.top), true)?.let { return it }
                checkX(line.atY(screen.bottom), true)?.let { return it }

            } else {

                val intersections = listOfNotNull(
                    checkY(line.atX(screen.right), true),
                    checkY(line.atX(screen.left), true),
                    checkX(line.atY(screen.top), true),
                    checkX(line.atY(screen.bottom), true)
                )

                if (intersections.size >= 2) return Line(intersections[0].from, intersections[1].from)
            }
        }

        return null
    }
}