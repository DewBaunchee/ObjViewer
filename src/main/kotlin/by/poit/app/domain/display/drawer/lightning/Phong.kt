package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.context.DisplayContext
import by.poit.app.domain.display.drawer.normalized
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.display.drawer.toColor
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.material.Material.Companion.defaultColor
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
        if (context.useMaterial) {
            val material = pixel.triangle.materialIn(obj)

            val pixelWorld = pixel.world
            val light = pixelWorld.minus(context.light.position).normalized()
            val view = context.observer.position.minus(pixelWorld).normalized()

            val diffuse = (-dot(light, pixel.normal)).coerceAtLeast(0.0)
            val reflect =
                (if (context.useMaterial) 1.0 else context.mirrorLightCoefficient) *
                    dot(
                        reflect(light, pixel.normal).normalized(),
                        view
                    ).coerceAtLeast(0.0)
                        .pow(
                            if (context.useMaterial)
                                material.shininess
                            else
                                context.shininess
                        )

            val ambientColor = material.ambientColor.copy()
            val diffuseColor = material.diffuseColor.multiplied(diffuse)
            val reflectColor = (
                if (context.useMaterial)
                    material.specularColor
                else
                    context.light.color.normalized()
                ).multiplied(reflect)

            val color = ambientColor.plus(diffuseColor).plus(reflectColor)

            return color.multiplied(1.0 / 3).toColor()
        } else {
            val pixelWorld = pixel.world
            val light = pixelWorld.minus(context.light.position).normalized()
            val view = context.observer.position.minus(pixelWorld).normalized()

            val ambient = context.ambient
            val diffuse = (context.diffuse * -dot(light, pixel.normal)).coerceAtLeast(0.0)
            val reflect =
                context.mirrorLightCoefficient * dot(
                    reflect(light, pixel.normal).normalized(),
                    view
                ).coerceAtLeast(0.0).pow(context.shininess)

            if (diffuse < 0 || reflect < 0) throw Exception()

            val ambientColor = defaultColor.multiplied(ambient)
            val diffuseColor = defaultColor.multiplied(diffuse)
            val reflectColor = context.light.color.normalized().multiplied(reflect)

            val color = ambientColor.plus(diffuseColor).plus(reflectColor)

            return color.multiplied(1.0 / 3).toColor()
        }
    }
}
