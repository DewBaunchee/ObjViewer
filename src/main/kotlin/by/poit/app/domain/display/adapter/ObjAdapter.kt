package by.poit.app.domain.display.adapter

import by.poit.app.domain.display.Observer
import by.poit.app.domain.display.transformation.*
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.streams.toList


class ObjAdapter(
    private val width: () -> Int,
    private val height: () -> Int,
    private val printer: PrintAdapter
) {

    fun draw(graphics: Graphics, observer: Observer, obj: Obj?) {
        if (obj == null) return

        val translation = translation(obj.translation)
        val scale = scale(obj.scale)
        val fullRotation = fullRotation(obj.rotation)
        val view = observer.view()
        val projection = fovProjection(width().toDouble() / height(), observer.fov, observer.zNear, observer.zFar)
        val viewport = viewport(width().toDouble(), height().toDouble())

        val vertices = IntStream.range(0, obj.source.vertices.size)
            .parallel()
            .mapToObj { obj.source.vertices[it] }
            .map { vertex ->
                vertex
                    .multiply(translation)
                    .multiply(scale)
                    .multiply(fullRotation)
                    .multiply(view)
                    .multiply(projection)
                    .multiply(viewport)
                    .vector3()
            }
            .toList()

        val pixels = BufferedImage(width(), height(), TYPE_INT_RGB)
        obj.source.polygons.parallelStream()
            .forEach { polygon ->
                drawClosed(pixels, polygon.vertexIndices.map { vertices[it - 1] })
            }
        graphics.drawImage(pixels, 0, 0, null)

        printer.printVertices(graphics, vertices)
    }

    private fun drawClosed(pixels: BufferedImage, vertices: List<Vector3>) {
        drawLine(
            pixels,
            vertices[0].x.toInt(),
            vertices[0].y.toInt(),
            vertices.last().x.toInt(),
            vertices.last().y.toInt()
        )
        for (i in 1 until vertices.size) {
            drawLine(
                pixels,
                vertices[i].x.toInt(),
                vertices[i].y.toInt(),
                vertices[i - 1].x.toInt(),
                vertices[i - 1].y.toInt()
            )
        }
    }

    private fun drawLine(pixels: BufferedImage, x1: Int, y1: Int, x2: Int, y2: Int) {
        val dx: Int = abs(x2 - x1)
        val sx = if (x1 < x2) 1 else -1
        val dy: Int = -abs(y2 - y1)
        val sy = if (y1 < y2) 1 else -1
        var err = dx + dy
        var x = x1
        var y = y1
        while (true) {
            if (x >= 0 && y >= 0 && x < pixels.width && y < pixels.height) pixels.setRGB(x, y, green)

            if (x == x2 && y == y2) {
                return
            }
            val err2 = err * 2
            if (err2 > dy) {
                err += dy
                x += sx
            }
            if (err2 <= dx) {
                err += dx
                y += sy
            }
        }
    }

    private val green = Color.GREEN.let {
        (it.red shl 16) + (it.green shl 8) + it.blue
    }
}