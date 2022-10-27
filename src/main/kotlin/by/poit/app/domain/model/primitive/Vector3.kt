package by.poit.app.domain.model.primitive

import by.poit.app.domain.model.structure.Matrix
import kotlin.math.pow
import kotlin.math.sqrt

class Vector3(var x: Double, var y: Double, var z: Double) {

    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(all: Number) : this(all.toDouble(), all.toDouble(), all.toDouble())

    companion object {
        fun normal(first: Vector3, second: Vector3, third: Vector3): Vector3 {
            return third.minus(first)
                .cross(second.minus(first))
                .normalized()
        }

        fun dot(first: Vector3, second: Vector3): Double {
            return first.x * second.x + first.y * second.y + first.z * second.z
        }

        fun cos(first: Vector3, second: Vector3): Double {
            return dot(first, second) / (first.module() * second.module())
        }
    }

    fun multiply(matrix: Matrix): Vector3 {
        return Vector3(
            matrix.values[0].sumOf { it * x },
            matrix.values[1].sumOf { it * y },
            matrix.values[2].sumOf { it * z },
        )
    }

    fun multiply(value: Double): Vector3 {
        return Vector3(x * value, y * value, z * value)
    }

    fun add(on: Vector3): Vector3 {
        return Vector3(
            x + on.x,
            y + on.y,
            z + on.z
        )
    }

    fun minus(vector: Vector3): Vector3 {
        return Vector3(
            x - vector.x,
            y - vector.y,
            z - vector.z
        )
    }

    fun plus(vector: Vector3): Vector3 {
        return Vector3(
            x + vector.x,
            y + vector.y,
            z + vector.z
        )
    }

    fun cross(with: Vector3): Vector3 {
        return Vector3(
            with.y * z - with.z * y,
            with.z * x - with.x * z,
            with.x * y - with.y * x
        )
    }

    fun module(): Double {
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }

    fun normalized(): Vector3 {
        val module = module()
        if (module == 0.0 || module == 1.0) return this
        return Vector3(x / module, y / module, z / module)
    }

    override fun toString(): String {
        return "X: $x, Y: $y, Z: $z"
    }

    fun toString(fixed: Int): String {
        return String.format("%${fixed}f %${fixed}f %${fixed}f", x, y, z)
    }
}