package by.poit.app.domain.model.obj

import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.display.drawer.acquireOrNull
import by.poit.app.domain.model.obj.material.Material
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix

class Face(
    val components: List<Component>,
    val materialName: String,
) {

    val triangles = (1 until components.size - 1).map {
        Triangle(
            components[0],
            components[it],
            components[it + 1],
            this
        )
    }

    class Component(val vIndex: Int, private val vtIndex: Int, private val vnIndex: Int) {

        fun viewVertexIn(obj: Obj): Vector3 {
            return obj.viewVertices.acquire(vIndex)
        }

        fun vertexIn(obj: Obj): Vector3 {
            return obj.vertices.acquire(vIndex)
        }

        fun worldVertexIn(obj: Obj): Vector3 {
            return obj.worldVertices.acquire(vIndex)
        }

        fun worldNormalIn(obj: Obj): Vector3? {
            return obj.worldNormals.acquireOrNull(vnIndex)
        }

        fun textureCoordinateIn(obj: Obj): Vector3? {
            return obj.textureCoordinates.acquireOrNull(vtIndex)
        }
    }

    class Triangle(val first: Component, val second: Component, val third: Component, val face: Face) {

        var tbn: Matrix? = null

        val material get() = face.materialName

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

        fun updateTbn(obj: Obj) {
            tbn = obj.source.normalMap?.tbn(obj, this)
        }

        fun materialIn(obj: Obj): Material {
            return obj.source.materials.get(material)
        }
    }
}