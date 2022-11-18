package by.poit.app.domain.model.obj

import by.poit.app.domain.display.drawer.context.DisplayContext
import by.poit.app.domain.display.drawer.mapNormals
import by.poit.app.domain.display.drawer.mapToVector3
import by.poit.app.domain.display.transformation.*
import by.poit.app.domain.model.obj.material.Materials
import by.poit.app.domain.model.obj.texture.NormalMap
import by.poit.app.domain.model.obj.texture.Texture
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix

class Obj(private val name: String, val source: Source) {

    var kA = 0.3
    var kD = 1.0
    var kS = 0.0
    var kE = 0.5
    var shininess = 0.0

    private lateinit var translation: Matrix
    private lateinit var scale: Matrix
    private lateinit var fullRotation: Matrix
    private lateinit var view: Matrix
    private lateinit var projection: Matrix
    private lateinit var viewport: Matrix
    lateinit var world: Matrix private set
    lateinit var worldView: Matrix private set
    private lateinit var full: Matrix
    lateinit var viewVertices: List<Vector3> private set
    lateinit var worldVertices: List<Vector3> private set
    lateinit var worldNormals: List<Vector3> private set
    lateinit var vertices: List<Vector3> private set
    lateinit var textureCoordinates: List<Vector3> private set

    fun update(context: DisplayContext, width: Int = 0, height: Int = 0) {
        translation = translation(source.translation)
        scale = scale(source.scale)
        fullRotation = fullRotation(source.rotation)
        view = context.observer.view()
        projection = fovProjection(
            width.toDouble() / height,
            context.observer.fov,
            context.observer.zNear,
            context.observer.zFar
        )
        viewport = viewport(width.toDouble(), height.toDouble())

        world = this.fullRotation.multiply(this.scale).multiply(this.translation)
        worldView = view.multiply(world)
        full = this.viewport.multiply(projection).multiply(worldView)

        worldVertices = source.vertices.mapToVector3(world)
        viewVertices = source.vertices.mapToVector3(worldView)
        worldNormals = source.normals.mapNormals(world)
        vertices = source.vertices.mapToVector3(full)
        textureCoordinates = source.textureCoordinates
    }

    data class Source(
        val vertices: List<Vertex>,
        val faces: List<Face>,
        val normals: List<Vector3>,
        val textureCoordinates: List<Vector3>,
        val materials: Materials,
        val normalMap: NormalMap? = null,
        val diffuseTexture: Texture? = null,
        val specularTexture: Texture? = null,
        val emissionTexture: Texture? = null,
    ) {

        companion object {
            val translationDefault = Vector3(0, 0, 0)
            val scaleDefault = Vector3(1)
            val rotationDefault = Vector3(0)
        }

        val scaleSpeed = 0.001

        var translation = translationDefault.copy()
            private set
        var scale = scaleDefault.copy()
            private set
        var rotation = rotationDefault.copy()
            private set

        fun move(on: Vector3) {
            translation.add(
                on
                    .transformed(xRotation(rotation.x))
                    .transformed(yRotation(rotation.y))
                    .transformed(zRotation(rotation.z))
            )
        }

        fun scale(on: Vector3) {
            scale.add(on)
        }

        fun rotate(on: Vector3) {
            rotation.add(on)
        }

        fun reset() {
            translation = translationDefault.copy()
            scale = scaleDefault.copy()
            rotation = rotationDefault.copy()
        }

        fun toObj(name: String): Obj {
            return Obj(name, this)
        }
    }

    override fun toString(): String {
        return "Object $name [vertices=${source.vertices.size}, polygons=${source.faces.size}] - ${source.translation}"
    }

    fun hasTextures(): Boolean {
        return source.diffuseTexture != null
    }
}