package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.dot
import java.awt.Color
import kotlin.math.abs
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
        val light = lightPosition.minus(pixel.worldView).normalized()
        val view = observer.minus(pixel.worldView).normalized()

        val ambient = obj.kA
        val diffuse = obj.kD * dot(light, pixel.normal).coerceAtLeast(0.0)
        val reflect =
            obj.kS * abs(dot(
                Vector3.reflect(light, pixel.normal).normalized(),
                view
            )).coerceAtLeast(0.0)
                .pow(obj.shininess)

        val ambientColor = faceColor.toVector().multiply(ambient)
        val diffuseColor = faceColor.toVector().multiply(diffuse)
        val reflectColor = lightColor.toVector().multiply(reflect)

        val color = ambientColor.plus(diffuseColor).plus(reflectColor)

        return color.multiply(1.0 / (3 * 255)).toColor()
    }
}

private fun Color.toVector(): Vector3 {
    return Vector3(red, green, blue)
}

private fun Vector3.toColor(): Color {
    val normalized = multiply(255.0)
    return Color(normalized.x.roundToInt(), normalized.y.roundToInt(), normalized.z.roundToInt(), 255)
}