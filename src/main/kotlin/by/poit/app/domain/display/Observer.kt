package by.poit.app.domain.display

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

    var speed = 0.05
        set(value) {
            field = min(max(0.01, value), 0.5)
        }

    val rotationSpeed = 0.0025

    private var translation = translationDefault
    private var rotation = rotationDefault

    val fov = PI / 4
    val zNear = 1.0
    val zFar = 100.0

    fun view(): Matrix {
        return translation(translation)
            .multiply(zRotation(rotation.z))
            .multiply(yRotation(rotation.y))
            .multiply(xRotation(rotation.x))
//        return zRotation(rotation.z)
//            .multiply(yRotation(rotation.y))
//            .multiply(xRotation(rotation.x))
//            .multiply(translation(translation))
    }

    fun move(on: Vector3) {
        translation = translation.add(
            on
                .multiply(xRotation(rotation.x))
                .multiply(yRotation(rotation.y))
                .multiply(zRotation(rotation.z))
        )
    }

    fun rotate(on: Vector3) {
        rotation = rotation.add(on)
    }

    fun reset() {
        translation = translationDefault
        rotation = rotationDefault
    }

    override fun toString(): String {
        return "Observer (speed = $speed) - $translation"
    }

    fun decreaseSpeed() {
        speed -= 0.01
    }

    fun increaseSpeed() {
        speed += 0.01
    }
}