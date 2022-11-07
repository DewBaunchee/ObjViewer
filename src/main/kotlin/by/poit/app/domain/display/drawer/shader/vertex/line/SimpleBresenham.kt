package by.poit.app.domain.display.drawer.shader.vertex.line

import by.poit.app.domain.display.Image
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.VectorLine
import java.awt.Color
import kotlin.math.roundToInt

class SimpleBresenham : SimpleLineDrawer {

    private fun sign(x: Double): Int {
        return if (x > 0) 1 else if (x < 0) -1 else 0
    }

    override fun draw(image: Image, from: Vector3, to: Vector3, color: Color) {
        val line = image.intersection(VectorLine(from, to)) ?: return

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
}