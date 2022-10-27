package by.poit.app.domain.display.drawer

import by.poit.app.domain.model.obj.Vertex
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix
import java.awt.Color
import kotlin.math.round
import kotlin.streams.toList

fun <T> List<T>.acquire(index: Int): T {
    if (index == 0) throw NoSuchElementException()
    if (index < 0) return get(size + index % size - 1)
    return get(index - 1)
}

fun <T> List<T>.acquireOrNull(index: Int): T? {
    if (index == 0) return null
    if (index < 0) return get(size + index % size - 1)
    return getOrNull(index - 1)
}

fun List<Vertex>.mapToVector3(transformation: Matrix): List<Vector3> {
    return this.parallelStream()
        .map { vertex -> vertex.multiply(transformation).wDivided().vector3() }
        .toList()
}

fun List<Vector3>.map(transformation: Matrix): List<Vector3> {
    return this.parallelStream()
        .map { vertex -> vertex.multiply(transformation) }
        .toList()
}

fun List<Color>.average(): Color {
    return Color(
        round(this.sumOf { it.red } / this.size.toDouble()).toInt(),
        round(this.sumOf { it.green } / this.size.toDouble()).toInt(),
        round(this.sumOf { it.blue } / this.size.toDouble()).toInt(),
        round(this.sumOf { it.alpha } / this.size.toDouble()).toInt(),
    )
}