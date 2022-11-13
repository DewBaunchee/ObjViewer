package by.poit.app.domain.model.obj

import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.display.drawer.acquireOrNull
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color

class Face(val components: List<Component>, val color: Color = Color.BLUE) {

    val triangles = (1 until components.size - 1).map {
        Triangle(
            components[0],
            components[it],
            components[it + 1],
            color
        )
    }

    class Component(val vIndex: Int, val vtIndex: Int, val vnIndex: Int) {

        fun viewVertexIn(obj: Obj): Vector3 {
            return obj.viewVertices.acquire(vIndex)
        }

        fun worldVertexIn(obj: Obj): Vector3 {
            return obj.worldVertices.acquire(vIndex)
        }

        fun viewNormalIn(obj: Obj): Vector3? {
//            return obj.worldNormals.acquireOrNull(vnIndex)
            TODO()
        }

        fun worldNormalIn(obj: Obj): Vector3? {
            return null
        }
    }

    class Triangle(val first: Component, val second: Component, val third: Component, val color: Color) {

        fun vertexNormalsIn(obj: Obj): List<Pair<Vector3, Vector3>> {
            val normal = viewNormalIn(obj)
            return listOf(first, second, third).map {
                it.viewVertexIn(obj) to (it.worldNormalIn(obj) ?: normal)
            }
        }

        fun viewNormalIn(obj: Obj): Vector3 {
            return Vector3.normal(
                first.viewVertexIn(obj),
                second.viewVertexIn(obj),
                third.viewVertexIn(obj),
            )
        }

        fun worldNormalIn(obj: Obj): Vector3 {
            return Vector3.normal(
                first.worldVertexIn(obj),
                second.worldVertexIn(obj),
                third.worldVertexIn(obj),
            )
        }

        fun normalIn(obj: Obj): Vector3 {
            return Vector3.normal(
                obj.vertices.acquire(first.vIndex),
                obj.vertices.acquire(second.vIndex),
                obj.vertices.acquire(third.vIndex)
            )
        }
    }
}