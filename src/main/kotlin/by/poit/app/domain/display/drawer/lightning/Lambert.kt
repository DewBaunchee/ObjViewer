package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.average
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Polygon
import by.poit.app.domain.model.obj.Vertex
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.cos
import java.awt.Color
import kotlin.math.round

class Lambert(
    private val lightning: Vertex,
    private val ambient: Double = 0.0
) : Lightning {

    override fun render(obj: Obj, face: Polygon.Face): Color {
        val lighting = this.lightning.vector3()
        return internalRender(
            obj, face,
            if (this.lightning.isPosition())
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
        face: Polygon.Face,
        cos: (vertexNormal: Pair<Vector3, Vector3>) -> Double
    ): Color {
        val ambientColor =
            Triple(face.color.red * ambient, face.color.green * ambient, face.color.blue * ambient)
        return face.vertexNormalsIn(obj)
            .map { vertexNormal ->
                cos(vertexNormal)
                    .coerceAtLeast(0.0)
                    .let {
                        Color(
                            round((face.color.red - ambientColor.first) * it + ambientColor.first).toInt(),
                            round((face.color.green - ambientColor.second) * it + ambientColor.second).toInt(),
                            round((face.color.blue - ambientColor.third) * it + ambientColor.third).toInt(),
                            face.color.alpha
                        )
                    }
            }
            .average()
    }
}