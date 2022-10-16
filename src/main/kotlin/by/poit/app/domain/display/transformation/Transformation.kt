package by.poit.app.domain.display.transformation

import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun translation(on: Vector3): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0, on.x),
            doubleArrayOf(0.0, 1.0, 0.0, on.y),
            doubleArrayOf(0.0, 0.0, 1.0, on.z),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}


fun scale(on: Vector3): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(on.x, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, on.y, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, on.z, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}

fun xRotation(on: Double): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, cos(on), -sin(on), 0.0),
            doubleArrayOf(0.0, sin(on), cos(on), 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}

fun yRotation(on: Double): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(cos(on), 0.0, sin(on), 0.0),
            doubleArrayOf(0.0, 1.0, 0.0, 0.0),
            doubleArrayOf(-sin(on), 0.0, cos(on), 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}

fun zRotation(on: Double): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(cos(on), -sin(on), 0.0, 0.0),
            doubleArrayOf(sin(on), cos(on), 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}

fun orthogonalProjection(zNear: Double, zFar: Double, height: Double, width: Double): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(-2 / width, 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, -2 / height, 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, 1 / (zNear - zFar), zNear / (zNear - zFar)),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}

fun fovProjection(aspect: Double, fov: Double, zNear: Double, zFar: Double): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(1 / (aspect * tan(fov / 2)), 0.0, 0.0, 0.0),
            doubleArrayOf(0.0, 1 / tan(fov / 2), 0.0, 0.0),
            doubleArrayOf(0.0, 0.0, zFar / (zNear - zFar), zNear * zFar / (zNear - zFar)),
            doubleArrayOf(0.0, 0.0, -1.0, 0.0)
        )
    )
}


fun viewport(width: Double, height: Double): Matrix {
    return Matrix(
        arrayOf(
            doubleArrayOf(width / 2, 0.0, 0.0, width / 2),
            doubleArrayOf(0.0, -height / 2, 0.0, height / 2),
            doubleArrayOf(0.0, 0.0, 1.0, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0)
        )
    )
}

fun fullRotation(rotation: Vector3): Matrix {
    return xRotation(rotation.x)
        .multiply(yRotation(rotation.y))
        .multiply(zRotation(rotation.z))
}