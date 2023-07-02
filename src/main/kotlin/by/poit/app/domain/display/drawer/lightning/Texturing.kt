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
        val material = pixel.triangle.materialIn(obj)

        val light = pixel.world.minus(context.light.position).normalized()
        val view = context.observer.position.minus(pixel.world).normalized()

        val diffuse = (material.diffuseTexture ?: obj.source.diffuseTexture)!!.value(pixel)
        val normal = getNormal(obj, pixel)
        val specular = (material.specularTexture ?: obj.source.specularTexture)?.value(pixel)
        val emission = (material.emissionTexture ?: obj.source.emissionTexture)?.value(pixel)

        val reflected = Vector3.reflect(light, normal).normalized()

        val iA = diffuse.multiplied(context.ambient)
        val iD = diffuse.multiplied((1.0 * -dot(light, normal)).coerceAtLeast(0.0))
        val iS =
            context.light.color.normalized().multiplied(
                (specular?.module() ?: 1.0) *
                    context.mirrorLightCoefficient * dot(reflected, view)
                    .coerceAtLeast(0.0)
                    .pow(context.shininess)
            )
        val iE = emission ?: Vector3(0)

        return (
            if (iE.module() != 0.0)
                iE
            else
                iA.add(iD).add(iS).multiply(1 / 3.0)
            ).toColor()
    }

    private fun getNormal(obj: Obj, pixel: Pixel): Vector3 {
        val normalMap = pixel.triangle.materialIn(obj).normalMap ?: obj.source.normalMap
        val tbn = pixel.triangle.tbn
        return if (normalMap == null || tbn == null)
            pixel.normal
        else
            normalMap.get(pixel).transformed(tbn)
    }
}