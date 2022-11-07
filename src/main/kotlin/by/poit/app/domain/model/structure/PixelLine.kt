package by.poit.app.domain.model.structure

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.primitive.Vector3

class PixelLine(val from: Pixel, val to: Pixel) {

    private val stepY = (to.screen.y - from.screen.y).let {
        Pixel(
            to.screen.minus(from.screen).multiply(1.0 / it),
            to.worldView.minus(from.worldView).multiply(1.0 / it),
            to.normal.minus(from.normal).multiply(1.0 / it),
        )
    }

    private val stepX = (to.screen.x - from.screen.x).let {
        Pixel(
            to.screen.minus(from.screen).multiply(1.0 / it),
            to.worldView.minus(from.worldView).multiply(1.0 / it),
            to.normal.minus(from.normal).multiply(1.0 / it),
        )
    }

    fun atY(y: Double): Pixel {
        val dy = y - from.screen.y
        return Pixel(
            Vector3(from.screen.x + stepY.screen.x * dy, y, from.screen.z + stepY.screen.z * dy),
            from.worldView.plus(stepY.worldView.multiply(dy)),
            from.normal.plus(stepY.normal.multiply(dy)),
        )
    }

    fun atX(x: Double): Pixel {
        val dx = x - from.screen.x
        return Pixel(
            Vector3(x, from.screen.y + stepX.screen.y * dx, from.screen.z + stepX.screen.z * dx),
            from.worldView.plus(stepX.worldView.multiply(dx)),
            from.normal.plus(stepX.normal.multiply(dx)),
        )
    }
}