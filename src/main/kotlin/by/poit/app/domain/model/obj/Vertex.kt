package by.poit.app.domain.model.obj

import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix

class Vertex(val x: Double, val y: Double, val z: Double, val w: Double) {

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

//    fun multiply(matrix: Matrix): Vertex {
//        return Vertex(
//            matrix.values[0].sumOf { it * x },
//            matrix.values[1].sumOf { it * y },
//            matrix.values[2].sumOf { it * z },
//            matrix.values[3].sumOf { it * w },
//        )
//    }

    override fun toString(): String {
        return String.format("%7f %7f %7f %7f", x, y, z, w)
    }

    fun vector3(): Vector3 {
        return Vector3(x / w, y / w, z / w)
    }

    fun array(): DoubleArray {
        return doubleArrayOf(x, y, z, w)
    }
}