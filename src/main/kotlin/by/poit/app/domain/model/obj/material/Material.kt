package by.poit.app.domain.model.obj.material

import by.poit.app.domain.model.primitive.Vector3

class Material(
    val name: String,
    val ka: Vector3,
    val kd: Vector3,
    val ks: Vector3,
    val ns: Double,
    val d: Double,
    val tr: Double,
    val illum: Int
) {
    // Tr, Tf, etc.
    init {
        if (name.isBlank())
            throw IllegalArgumentException("Illegal name of material.")

        checkRange("kA", ka)
        checkRange("kD", kd)
        checkRange("kS", ks)
        checkRange("Ns", ns, 0.0, 1000.0)
        checkRange("d", d, 0.0, 1.0)
        checkRange("tr", tr, 0.0, 1.0)
        checkRange("illum", illum, 0, 10)
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

        var ka = Vector3(0.2, 0.2, 0.2)
        var kd = Vector3(0.8, 0.8, 0.8)
        var ks = Vector3(1, 1, 1)
        var ns = 0.0
        var d = 1.0
        var tr = 0.0
        var illum = 2

        fun build(): Material {
            return Material(name, ka, kd, ks, ns, d, tr, illum)
        }
    }
}