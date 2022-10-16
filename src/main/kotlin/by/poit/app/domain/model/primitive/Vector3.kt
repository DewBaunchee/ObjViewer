package by.poit.app.domain.model.primitive

import by.poit.app.domain.model.structure.Matrix
import kotlin.math.pow
import kotlin.math.sqrt

class Vector3(val x: Double, val y: Double, val z: Double) {

    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(all: Number) : this(all.toDouble(), all.toDouble(), all.toDouble())

    fun multiply(matrix: Matrix): Vector3 {
        return Vector3(
            matrix.values[0].sumOf { it * x },
            matrix.values[1].sumOf { it * y },
            matrix.values[2].sumOf { it * z },
        )
    }

    fun add(on: Vector3): Vector3 {
        return Vector3(
            x + on.x,
            y + on.y,
            z + on.z
        )
    }

    fun module(): Double {
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }

    fun normalized(): Vector3 {
        val module = module()
        return Vector3(x / module, y / module, z / module)
    }

    override fun toString(): String {
        return "X: $x, Y: $y, Z: $z"
    }

    fun toString(fixed: Int): String {
        return String.format("%${fixed}f %${fixed}f %${fixed}f", x, y, z)
    }
}