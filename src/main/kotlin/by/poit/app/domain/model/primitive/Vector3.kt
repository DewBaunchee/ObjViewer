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

        fun reflect(vector: Vector3, normal: Vector3): Vector3 {
            return vector.minus(normal.multiplied(dot(vector, normal) * 2.0))
        }
    }

    fun copy(): Vector3 {
        return Vector3(x, y, z)
    }

    fun transformed(matrix: Matrix): Vector3 {
        return Vector3(
            x * matrix.values[0][0] + y * matrix.values[0][1] + z * matrix.values[0][2] + matrix.values[0][3],
            x * matrix.values[1][0] + y * matrix.values[1][1] + z * matrix.values[1][2] + matrix.values[1][3],
            x * matrix.values[2][0] + y * matrix.values[2][1] + z * matrix.values[2][2] + matrix.values[2][3]
        )
    }

    fun transformedNormal(matrix: Matrix): Vector3 {
        return Vector3(
            x * matrix.values[0][0] + y * matrix.values[0][1] + z * matrix.values[0][2],
            x * matrix.values[1][0] + y * matrix.values[1][1] + z * matrix.values[1][2],
            x * matrix.values[2][0] + y * matrix.values[2][1] + z * matrix.values[2][2]
        )
    }

    fun multiplied(value: Double): Vector3 {
        return Vector3(x * value, y * value, z * value)
    }

    fun multiply(value: Double): Vector3 {
        x *= value
        y *= value
        z *= value
        return this
    }

    fun add(on: Vector3): Vector3 {
        x += on.x
        y += on.y
        z += on.z
        return this
    }

    fun minus(vector: Vector3): Vector3 {
        return Vector3(
            x - vector.x,
            y - vector.y,
            z - vector.z
        )
    }

    fun minus(value: Double): Vector3 {
        return Vector3(
            x - value,
            y - value,
            z - value
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

    fun set(vector: Vector3) {
        x = vector.x
        y = vector.y
        z = vector.z
    }

    fun normalized(): Vector3 {
        val module = module()
        if (module == 0.0 || module == 1.0) return this
        return Vector3(x / module, y / module, z / module)
    }

    fun distance(vector: Vector3): Double {
        return this.minus(vector).module()
    }

    override fun toString(): String {
        return "X: $x, Y: $y, Z: $z"
    }

    fun toString(fixed: Int): String {
        return String.format("%${fixed}f %${fixed}f %${fixed}f", x, y, z)
    }

    fun toList(): List<Double> {
        return listOf(x, y, z)
    }
}