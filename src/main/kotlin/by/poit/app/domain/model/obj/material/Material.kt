package by.poit.app.domain.model.obj.material

import by.poit.app.domain.display.drawer.normalized
import by.poit.app.domain.model.obj.texture.NormalMap
import by.poit.app.domain.model.obj.texture.Texture
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color

class Material(
    val name: String,
    val ambientColor: Vector3 = Vector3(0.2),
    val diffuseColor: Vector3 = Vector3(0.8),
    val specularColor: Vector3 = Vector3(1.0),
    val shininess: Double = 0.0,
    val diffuseTexture: Texture? = null,
    val normalMap: NormalMap? = null,
    val specularTexture: Texture? = null,
    val emissionTexture: Texture? = null,
    val d: Double = 1.0,
    val tr: Double = 0.0,
    val illum: Int = 2
) {
    // Tr, Tf, etc.
    init {
        if (name.isBlank())
            throw IllegalArgumentException("Illegal name of material.")

        checkRange("kA", ambientColor)
        checkRange("kD", diffuseColor)
        checkRange("kS", specularColor)
        checkRange("Ns", shininess, 0.0, 1000.0)
        checkRange("d", d, 0.0, 1.0)
        checkRange("tr", tr, 0.0, 1.0)
        checkRange("illum", illum, 0, 10)
    }

    companion object {
        val defaultMaterial = Material("Default")
        val defaultColor = Color.BLUE.normalized()
    }

    private fun checkRange(name: String, vector: Vector3) {
        if (vector.toList().all { it in 0.0..1.0 }) return
        throw IllegalArgumentException("Component $name isn't in range.")
    }

    private fun checkRange(name: String, value: Double, min: Double, max: Double) {
        if (value in min..max) return
        throw IllegalArgumentException("Component $name isn't in range.")
    }

    private fun checkRange(name: String, value: Int, min: Int, max: Int) {
        if (value in min..max) return
        throw IllegalArgumentException("Component $name isn't in range.")
    }

    override fun toString(): String {
        return "Material[$name]"
    }

    class Builder(private val name: String) {

        var ka = Vector3(0.2)
        var kd = Vector3(0.8)
        var ks = Vector3(1)
        var ns = 0.0
        var diffuseTexture: Texture? = null
        var normalMap: NormalMap? = null
        var specularTexture: Texture? = null
        var emissionTexture: Texture? = null
        var d = 1.0
        var tr = 0.0
        var illum = 2

        fun build(): Material {
            return Material(
                name,
                ka,
                kd,
                ks,
                ns,
                diffuseTexture,
                normalMap,
                specularTexture,
                emissionTexture,
                d,
                tr,
                illum
            )
        }
    }
}