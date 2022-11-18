package by.poit.app.domain.model.observer

import by.poit.app.domain.display.transformation.translation
import by.poit.app.domain.display.transformation.xRotation
import by.poit.app.domain.display.transformation.yRotation
import by.poit.app.domain.display.transformation.zRotation
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min

class Observer {

    companion object {
        val translationDefault = Vector3(0, 0, -10)
        val rotationDefault = Vector3(0, PI, PI)
    }

    var speed = 0.01
        set(value) {
            field = min(max(0.01, value), 0.5)
        }

    val rotationSpeed = 0.0025

    var position = translationDefault.copy()
    private var rotation = rotationDefault.copy()

    val fov = PI / 4
    val zNear = 1.0
    val zFar = 2000.0

    fun view(): Matrix {
        return translation(position)
            .multiply(zRotation(rotation.z))
            .multiply(yRotation(rotation.y))
            .multiply(xRotation(rotation.x))
    }

    fun move(on: Vector3) {
        position.add(
            on
                .transformed(xRotation(rotation.x))
                .transformed(yRotation(rotation.y))
                .transformed(zRotation(rotation.z))
        )
    }

    fun rotate(on: Vector3) {
        rotation.add(on)
    }

    fun reset() {
        position = translationDefault.copy()
        rotation = rotationDefault.copy()
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