package by.poit.app.domain.model.observer

import by.poit.app.domain.display.transformation.lookAt
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix
import kotlin.math.*

class Observer {

    companion object {
        val translationDefault = Vector3(0, 0, -10)
        val rotationDefault = Vector3(0, 0, 0)
    }

    var speed = 0.01
        set(value) {
            field = min(max(0.01, value), 0.5)
        }

    val rotationSpeed = 0.0025

    val up = Vector3(0, 1, 0)
    var position = translationDefault
        private set
    private var rotation = rotationDefault

    val pitch get() = rotation.x
    val yaw get() = rotation.y
    val roll get() = rotation.z

    val fov = PI / 4
    val zNear = 1.0
    val zFar = 1000.0

    fun direction(): Vector3 {
        return Vector3(
            cos(pitch) * sin(yaw),
            sin(pitch),
            cos(pitch) * cos(yaw)
        )
    }

    fun view(): Matrix {
        return lookAt(
            position,
            position.plus(direction()),
            up
        )
    }

    fun move(on: Vector3) {
        position = position.add(on)
    }

    fun rotate(on: Vector3) {
        rotation = rotation.add(on)
    }

    fun reset() {
        position = translationDefault
        rotation = rotationDefault
    }

    override fun toString(): String {
        return "Observer (speed = $speed) - $position, $rotation"
    }

    fun decreaseSpeed() {
        speed -= 0.01
    }

    fun increaseSpeed() {
        speed += 0.01
    }
}