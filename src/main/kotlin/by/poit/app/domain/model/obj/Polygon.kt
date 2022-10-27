package by.poit.app.domain.model.obj

import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color

class Polygon(val components: List<Component>, val color: Color = Color.PINK) {

    val faces = (1 until components.size - 1).map {
        Face(
            components[0],
            components[it],
            components[it + 1],
            color
        )
    }

    class Component(val vIndex: Int, val vtIndex: Int, val vnIndex: Int)

    class Face(val first: Component, val second: Component, val third: Component, val color: Color) {

        fun vertexNormalsIn(obj: Obj): List<Pair<Vector3, Vector3>> {
            val normal = normalIn(obj)
            return listOf(first, second, third).map {
                obj.viewVertices.acquire(it.vIndex) to (/*obj.worldNormals.acquireOrNull(it.vnIndex) ?:*/ normal)
            }
        }

        fun normalIn(obj: Obj): Vector3 {
            return Vector3.normal(
                obj.viewVertices.acquire(first.vIndex),
                obj.viewVertices.acquire(second.vIndex),
                obj.viewVertices.acquire(third.vIndex)
            )
        }
    }
}