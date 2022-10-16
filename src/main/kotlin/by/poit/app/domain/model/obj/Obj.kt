package by.poit.app.domain.model.obj

import by.poit.app.domain.display.transformation.xRotation
import by.poit.app.domain.display.transformation.yRotation
import by.poit.app.domain.display.transformation.zRotation
import by.poit.app.domain.model.primitive.Vector3

class Obj(val name: String, val source: Source) {

    data class Source(val vertices: List<Vertex>, val polygons: List<Polygon>) {

        fun toObj(name: String = ""): Obj {
            return Obj(name, this)
        }
    }

    companion object {
        val translationDefault = Vector3(0, 0, 0)
        val scaleDefault = Vector3(1)
        val rotationDefault = Vector3(0)
    }

    val scaleSpeed = 0.001

    var translation = translationDefault
        private set
    var scale = scaleDefault
        private set
    var rotation = rotationDefault
        private set

    fun move(on: Vector3) {
        translation = translation.add(
            on
                .multiply(xRotation(rotation.x))
                .multiply(yRotation(rotation.y))
                .multiply(zRotation(rotation.z))
        )
    }

    fun scale(on: Vector3) {
        scale = scale.add(on)
    }

    fun rotate(on: Vector3) {
        rotation = rotation.add(on)
    }

    fun reset() {
        translation = translationDefault
        scale = scaleDefault
        rotation = rotationDefault
    }

    override fun toString(): String {
        return "Object $name [vertices=${source.vertices.size}, polygons=${source.polygons.size}] - $translation"
    }
}