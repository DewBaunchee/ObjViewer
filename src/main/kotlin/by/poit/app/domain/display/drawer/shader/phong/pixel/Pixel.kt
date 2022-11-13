package by.poit.app.domain.display.drawer.shader.phong.pixel

import by.poit.app.domain.model.primitive.Vector3
import kotlin.math.max
import kotlin.math.min

class Pixel(
    val screen: Vector3,
    val world: Vector3,
    val worldView: Vector3,
    val normal: Vector3
) {

    fun copy(): Pixel {
        return Pixel(screen.copy(), world.copy(), worldView.copy(), normal.copy())
    }

    fun delta(pixel: Pixel): Pixel {
        return Pixel(
            pixel.screen.minus(screen),
            pixel.world.minus(world),
            pixel.worldView.minus(worldView),
            pixel.normal.minus(normal),
        )
    }

    fun multiply(value: Double) {
        world.multiply(value)
        worldView.multiply(value)
        normal.multiply(value)
    }

    fun add(delta: Pixel) {
        world.add(delta.world)
        worldView.add(delta.worldView)
        normal.add(delta.normal)
    }

    override fun toString(): String {
        return "Pixel {Screen=$screen; WorldView=$worldView; Normal=$normal}"
    }

    fun isBetween(from: Pixel, to: Pixel): Boolean {
        return (screen.x in min(from.screen.x, to.screen.x)..max(from.screen.x, to.screen.x))
            && (screen.y in min(from.screen.y, to.screen.y)..max(from.screen.y, to.screen.y))
    }
}