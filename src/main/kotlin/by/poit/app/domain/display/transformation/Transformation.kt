package by.poit.app.domain.display.transformation

import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.dot
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

fun pitchYawRoll(pitch: Double, yaw: Double, roll: Double): Matrix {
    val halfPitch = 0.5 * pitch
    val halfYaw = 0.5 * yaw
    val halfRoll = 0.5 * roll

    val sinPitch = sin(halfPitch)
    val cosPitch = cos(halfPitch)
    val sinYaw = sin(halfYaw)
    val cosYaw = cos(halfYaw)
    val sinRoll = sin(halfRoll)
    val cosRoll = cos(halfRoll)

    val x = cosYaw * sinPitch * cosRoll + sinYaw * cosPitch * sinRoll
    val y = sinYaw * cosPitch * cosRoll - cosYaw * sinPitch * sinRoll
    val z = sinYaw * cosPitch * sinRoll - sinYaw * sinPitch * cosRoll
    val w = cosYaw * cosPitch * cosRoll + sinYaw * sinPitch * sinRoll

    val xx = x * x
    val yy = y * y
    val zz = z * z
    val xy = x * y
    val wz = w * z
    val xz = x * z
    val wy = w * y
    val yz = y * z
    val wx = w * x

    return Matrix(
        arrayOf(
            doubleArrayOf(1 - 2 * (yy + zz), 2 * (xy + wz), 2 * (xz - wy), 0.0),
            doubleArrayOf(2 * (xy - wz), 1 - 2 * (zz + xx), 2 * (yz + wx), 0.0),
            doubleArrayOf(2 * (xz + wy), 2 * (yz - wx), 1 - 2 * (yy + xx), 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0),
        )
    )
}

fun lookAt(eye: Vector3, target: Vector3, up: Vector3): Matrix {
    val z = eye.minus(target).normalized()
    val x = up.cross(z).normalized()
    return Matrix(
        arrayOf(
            doubleArrayOf(x.x, x.y, x.z, -dot(x, eye)),
            doubleArrayOf(up.x, up.y, up.z, -dot(up, eye)),
            doubleArrayOf(z.x, z.y, z.z, -dot(z, eye)),
            doubleArrayOf(0.0, 0.0, 0.0, 1.0),
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