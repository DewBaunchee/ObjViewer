package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.average
import by.poit.app.domain.display.drawer.toColor
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Vertex
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.cos
import java.awt.Color

class Lambert {

    fun render(obj: Obj, lightning: Vertex, triangle: Face.Triangle): Color {
        val lighting = lightning.vector3()
        return internalRender(
            obj, triangle,
            if (lightning.isPosition())
                { vertexNormal -> likePosition(vertexNormal, lighting) }
            else
                { vertexNormal -> likeDirection(vertexNormal, lighting) }
        )
    }

    private fun likePosition(vertexNormal: Pair<Vector3, Vector3>, lightning: Vector3): Double {
        val vertex = vertexNormal.first
        val normal = vertexNormal.second
        return cos(normal, lightning.minus(vertex).normalized())
    }

    private fun likeDirection(vertexNormal: Pair<Vector3, Vector3>, lightning: Vector3): Double {
        return cos(vertexNormal.second, lightning.normalized())
    }

    private fun internalRender(
        obj: Obj,
        triangle: Face.Triangle,
        cos: (vertexNormal: Pair<Vector3, Vector3>) -> Double
    ): Color {
        val material = triangle.materialIn(obj)
        return triangle.vertexNormalsIn(obj)
            .map { vertexNormal ->
                material.ambientColor.plus(
                    material.diffuseColor.multiplied(
                        cos(vertexNormal).coerceAtLeast(0.0)
                    )
                ).multiplied(1.0 / 2)
            }
            .average()
            .toColor()
    }
}