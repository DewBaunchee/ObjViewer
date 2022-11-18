package by.poit.app.domain.model.obj

import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix

class Vertex(val x: Double, val y: Double, val z: Double, val w: Double) {

    constructor(x: Number, y: Number, z: Number, w: Number) : this(
        x.toDouble(),
        y.toDouble(),
        z.toDouble(),
        w.toDouble()
    )

    constructor(xyz: Vector3, w: Number) : this(
        xyz.x,
        xyz.y,
        xyz.z,
        w.toDouble()
    )

    fun multiply(matrix: Matrix): Vertex {
        val result = DoubleArray(4) { 0.0 }

        val array = array()

        for (i in 0 until 4) {
            var acc = 0.0
            for (j in 0 until 4) {
                acc += matrix.values[i][j] * array[j]
            }
            result[i] = acc
        }

        return Vertex(result[0], result[1], result[2], result[3])
    }

    override fun toString(): String {
        return String.format("%7f %7f %7f %7f", x, y, z, w)
    }

    fun vector3(): Vector3 {
        return Vector3(x, y, z)
    }

    fun wDivided(): Vertex {
        return Vertex(x / w, y / w, z / w, 1.0)
    }

    fun array(): DoubleArray {
        return doubleArrayOf(x, y, z, w)
    }

    fun isPosition(): Boolean {
        return w == 1.0
    }
}