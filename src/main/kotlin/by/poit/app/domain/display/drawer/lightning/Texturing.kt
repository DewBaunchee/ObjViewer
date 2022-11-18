package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.context.DisplayContext
import by.poit.app.domain.display.drawer.normalized
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.display.drawer.toColor
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.dot
import java.awt.Color
import kotlin.math.pow

class Texturing : Colorizer {

    override fun render(context: DisplayContext, obj: Obj, pixel: Pixel): Color {
        val light = pixel.world.minus(context.light.position).normalized()
        val view = context.observer.position.minus(pixel.world).normalized()

        val diffuse = obj.source.diffuseTexture!!.value(pixel)
        val normal = obj.source.normalMap?.get(pixel)?.transformed(pixel.triangle.tbn!!) ?: pixel.normal
        val specular = obj.source.specularTexture?.value(pixel)
        val emission = obj.source.emissionTexture?.value(pixel)

        val reflected = Vector3.reflect(light, normal).normalized()
        if (normal.z < 0)
            normal.x
        val iA = diffuse.multiplied(obj.kA)
        val iD = diffuse.multiplied((obj.kD * -dot(light, normal)).coerceAtLeast(0.0))
        val iS =
            context.light.color.normalized().multiplied(
                (specular?.module() ?: 1.0) *
                    obj.kS * dot(reflected, view)
                    .coerceAtLeast(0.0)
                    .pow(obj.shininess)
            )
        val iE = emission?.multiplied(obj.kE) ?: Vector3(0)

        return (
            if (iE.module() != 0.0)
                iE
            else
                iA.add(iD).add(iS).add(iE).multiply(1 / 4.0)
            ).toColor()
    }
}