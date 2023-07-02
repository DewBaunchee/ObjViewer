package by.poit.app.domain.model.structure

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.primitive.Vector3

class PixelLine(val from: Pixel, val to: Pixel) {

    private val stepY = createStep(to.screen.y - from.screen.y)
    private val stepX = createStep(to.screen.x - from.screen.x)

    fun atY(y: Double): Pixel {
        val dy = y - from.screen.y
        return Pixel(
            Vector3(from.screen.x + stepY.screen.x * dy, y, from.screen.z + stepY.screen.z * dy),
            from.world.plus(stepY.world.multiplied(dy)),
            from.texel.plus(stepY.texel.multiplied(dy)),
            from.normal.plus(stepY.normal.multiplied(dy)),
            stepY.triangle
        )
    }

    fun atX(x: Double): Pixel {
        val dx = x - from.screen.x
        return Pixel(
            Vector3(x, from.screen.y + stepX.screen.y * dx, from.screen.z + stepX.screen.z * dx),
            from.world.plus(stepX.world.multiplied(dx)),
            from.texel.plus(stepX.texel.multiplied(dx)),
            from.normal.plus(stepX.normal.multiplied(dx)),
            stepX.triangle
        )
    }

    private fun createStep(diff: Double): Pixel {
        return Pixel(
            to.screen.minus(from.screen).multiplied(1.0 / diff),
            to.world.minus(from.world).multiplied(1.0 / diff),
            to.texel.minus(from.texel).multiplied(1.0 / diff),
            to.normal.minus(from.normal).multiplied(1.0 / diff),
            to.triangle
        )
    }

    fun pixelDelta(): Pixel {
        return from.delta(to)
    }
}