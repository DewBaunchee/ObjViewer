package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.dot
import by.poit.app.domain.model.primitive.Vector3.Companion.reflect
import java.awt.Color
import kotlin.math.pow
import kotlin.math.roundToInt

class Phong {

    fun color(
        obj: Obj,
        pixel: Pixel,
        faceColor: Color,
        lightPosition: Vector3,
        lightColor: Color,
        observer: Vector3
    ): Color {
        val pixelWorld = pixel.world
        val light = pixelWorld.minus(lightPosition).normalized()
        val view = observer.minus(pixelWorld).normalized()

        val ambient = obj.kA
        val diffuse = (obj.kD * -dot(light, pixel.normal)).coerceAtLeast(0.0)
        val reflect =
            obj.kS * dot(
                reflect(light, pixel.normal).normalized(),
                view
            ).coerceAtLeast(0.0).pow(obj.shininess)

        if (diffuse < 0 || reflect < 0) throw Exception()

        val ambientColor = faceColor.toVector().multiplied(ambient)
        val diffuseColor = faceColor.toVector().multiplied(diffuse)
        val reflectColor = lightColor.toVector().multiplied(reflect)

        val color = ambientColor.plus(diffuseColor).plus(reflectColor)

        return color.multiplied(1.0 / (3 * 255)).toColor()
    }
}

private fun Color.toVector(): Vector3 {
    return Vector3(red, green, blue)
}

private fun Vector3.toColor(): Color {
    val normalized = multiplied(255.0)
    return Color(normalized.x.roundToInt(), normalized.y.roundToInt(), normalized.z.roundToInt(), 255)
}