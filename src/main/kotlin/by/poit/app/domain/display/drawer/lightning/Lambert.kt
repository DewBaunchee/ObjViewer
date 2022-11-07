package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.average
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Vertex
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.cos
import java.awt.Color
import kotlin.math.round

class Lambert : Lightning {

    override fun render(obj: Obj, lightning: Vertex, triangle: Face.Triangle, ambient: Double): Color {
        val lighting = lightning.vector3()
        return internalRender(
            obj, triangle, ambient,
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
        ambient: Double,
        cos: (vertexNormal: Pair<Vector3, Vector3>) -> Double
    ): Color {
        val ambientColor =
            Triple(triangle.color.red * ambient, triangle.color.green * ambient, triangle.color.blue * ambient)
        return triangle.vertexNormalsIn(obj)
            .map { vertexNormal ->
                cos(vertexNormal)
                    .coerceAtLeast(0.0)
                    .let {
                        Color(
                            round((triangle.color.red - ambientColor.first) * it + ambientColor.first).toInt(),
                            round((triangle.color.green - ambientColor.second) * it + ambientColor.second).toInt(),
                            round((triangle.color.blue - ambientColor.third) * it + ambientColor.third).toInt(),
                            triangle.color.alpha
                        )
                    }
            }
            .average()
    }
}