package by.poit.app.domain.display.drawer.polygon

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.display.drawer.lightning.Lambert
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Polygon
import by.poit.app.domain.model.obj.Vertex
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.primitive.Vector3.Companion.cos
import java.awt.Color
import kotlin.math.abs
import kotlin.math.roundToInt

class FlatShader : Shader() {

    override fun draw(image: Image, obj: Obj, polygon: Polygon) {
        polygon.faces.forEach { face ->
            val observerPosition = obj.observer.position.multiply(obj.worldView)
            val normal = face.normalIn(obj)
            if (cos(obj.observer.direction().multiply(obj.worldView), normal) <= 0) return@forEach

            val lightedColor =
                Lambert(Vertex(observerPosition, 1), 0.3)
                    .render(obj, face)

            drawTriangle(
                image,
                obj.vertices.acquire(face.first.vIndex),
                obj.vertices.acquire(face.second.vIndex),
                obj.vertices.acquire(face.third.vIndex),
                lightedColor
            )
        }
    }

    private fun drawTriangle(image: Image, first: Vector3, second: Vector3, third: Vector3, color: Color) {
        val list = mutableListOf(first, second, third).sortedBy { it.y }
        val top = list[2]
        val middle = list[1]
        val bottom = list[0]
        val x = mutableListOf(first, second, third).sortedBy { it.x }
        val left = x[0]
        val right = x[2]

        drawHorizontal(
            image,
            color,
            bottom,
            bottom.y.roundToInt()..middle.y.roundToInt(),
            axz(bottom, middle),
            axz(bottom, top),
            left.x,
            right.x
        )
        drawHorizontal(
            image,
            color,
            top,
            middle.y.roundToInt()..top.y.roundToInt(),
            axz(top, bottom),
            axz(top, middle),
            left.x,
            right.x
        )
    }

    private fun drawHorizontal(
        image: Image,
        color: Color,
        anchor: Vector3,
        ys: IntRange,
        firstAXZ: Pair<Double, Double>,
        secondAXZ: Pair<Double, Double>,
        minX: Double,
        maxX: Double
    ) {
        for (y in ys) {
            lineDrawer.draw(
                image,
                betweenOnY(y, anchor, firstAXZ).let { Vector3(it.first.coerceIn(minX, maxX), y, it.second) },
                betweenOnY(y, anchor, secondAXZ).let { Vector3(it.first.coerceIn(minX, maxX), y, it.second) },
                color
            )
        }
    }

    private fun betweenOnY(y: Int, first: Vector3, axz: Pair<Double, Double>): Pair<Double, Double> {
        return (first.x + axz.first * (y - first.y)) to (first.z + axz.second * (y - first.y))
    }

    private fun axz(first: Vector3, second: Vector3): Pair<Double, Double> {
        val dy = second.y - first.y
        if (abs(dy) < 1) return 0.0 to 0.0
        return (second.x - first.x) / dy to (second.z - first.z) / dy
    }
}