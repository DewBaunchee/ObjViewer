package by.poit.app.domain.model.obj

import by.poit.app.domain.display.drawer.mapToVector3
import by.poit.app.domain.display.drawer.map
import by.poit.app.domain.display.transformation.*
import by.poit.app.domain.model.observer.Observer
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix

class Obj(val name: String, val observer: Observer, val source: Source) {

    lateinit var translation: Matrix private set
    lateinit var scale: Matrix private set
    lateinit var fullRotation: Matrix private set
    lateinit var view: Matrix private set
    lateinit var projection: Matrix private set
    lateinit var viewport: Matrix private set
    lateinit var world: Matrix private set
    lateinit var worldView: Matrix private set
    lateinit var full: Matrix private set
    lateinit var viewVertices: List<Vector3> private set
    lateinit var viewNormals: List<Vector3> private set
    lateinit var vertices: List<Vector3> private set

    init {
        update()
    }

    fun update(width: Int = 0, height: Int = 0) {
        translation = translation(source.translation)
        scale = scale(source.scale)
        fullRotation = fullRotation(source.rotation)
        view = observer.view()
        projection = fovProjection(width.toDouble() / height, observer.fov, observer.zNear, observer.zFar)
        viewport = viewport(width.toDouble(), height.toDouble())

        world = this.scale.multiply(this.fullRotation).multiply(this.translation)
        worldView = view.multiply(world)
        full = this.viewport.multiply(projection).multiply(worldView)

        viewVertices = source.vertices.mapToVector3(worldView)
        viewNormals = source.normals.map(worldView.inverted().transposed())
        vertices = source.vertices.mapToVector3(full)
    }

    data class Source(
        val vertices: List<Vertex>,
        val polygons: List<Polygon>,
        val normals: List<Vector3>
    ) {

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

        fun toObj(name: String, observer: Observer): Obj {
            return Obj(name, observer, this)
        }
    }

    override fun toString(): String {
        return "Object $name [vertices=${source.vertices.size}, polygons=${source.polygons.size}] - ${source.translation}"
    }
}