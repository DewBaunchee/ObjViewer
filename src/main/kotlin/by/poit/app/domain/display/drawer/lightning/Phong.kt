package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.context.DisplayContext
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.display.drawer.toColor
import by.poit.app.domain.display.drawer.toVector
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3.Companion.dot
import by.poit.app.domain.model.primitive.Vector3.Companion.reflect
import java.awt.Color
import kotlin.math.pow

class Phong : Colorizer {

    override fun render(
        context: DisplayContext,
        obj: Obj,
        pixel: Pixel,
    ): Color {
        val pixelWorld = pixel.world
        val light = pixelWorld.minus(context.light.position).normalized()
        val view = context.observer.position.minus(pixelWorld).normalized()

        val ambient = obj.kA
        val diffuse = (obj.kD * -dot(light, pixel.normal)).coerceAtLeast(0.0)
        val reflect =
            obj.kS * dot(
                reflect(light, pixel.normal).normalized(),
                view
            ).coerceAtLeast(0.0).pow(obj.shininess)

        if (diffuse < 0 || reflect < 0) throw Exception()

        val ambientColor = pixel.face.color.toVector().multiplied(ambient)
        val diffuseColor = pixel.face.color.toVector().multiplied(diffuse)
        val reflectColor = context.light.color.toVector().multiplied(reflect)

        val color = ambientColor.plus(diffuseColor).plus(reflectColor)

        return color.multiplied(1.0 / (3 * 255)).toColor()
    }
}
