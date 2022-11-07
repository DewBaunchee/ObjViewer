package by.poit.app.domain.model.structure

import by.poit.app.domain.model.primitive.Vector3

class VectorLine(val from: Vector3, val to: Vector3) {

    fun atY(y: Double): Vector3 {
        return Vector3(
            from.x + (y - from.y) * (to.x - from.x) / (to.y - from.y),
            y,
            (from.z + (y - from.y) * (to.z - from.z) / (to.y - from.y))
        )
    }

    fun atX(x: Double): Vector3 {
        return Vector3(
            x,
            from.y + (x - from.x) * (to.y - from.y) / (to.x - from.x),
            from.z + (x - from.x) * (to.z - from.z) / (to.x - from.x)
        )
    }
}