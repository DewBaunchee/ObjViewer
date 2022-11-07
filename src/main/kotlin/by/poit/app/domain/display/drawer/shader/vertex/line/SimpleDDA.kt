package by.poit.app.domain.display.drawer.shader.vertex.line

import by.poit.app.domain.display.Image
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color
import kotlin.math.abs
import kotlin.math.max

class SimpleDDA : SimpleLineDrawer {

    override fun draw(image: Image, from: Vector3, to: Vector3, color: Color) {
        val l = max(abs(to.x - from.x), abs(to.y - from.y)).toInt()
        if (l == 0) return

        var x = from.x
        var y = from.y
        var z = from.z
        val dx = (to.x - x) / l
        val dy = (to.y - y) / l
        val dz = (to.z - z) / l

        var wasInImage = image.setPixel(x, y, z, color)
        for (i in 1..l) {
            x += dx
            y += dy
            z += dz
            val inImage = image.setPixel(x, y, z, color)

            if (wasInImage && !inImage) break

            wasInImage = inImage
        }
    }
}