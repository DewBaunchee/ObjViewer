package by.poit.app.domain.display.drawer.shader.phong

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.display.drawer.acquireOrNull
import by.poit.app.domain.display.drawer.lightning.Phong
import by.poit.app.domain.display.drawer.shader.Shader
import by.poit.app.domain.display.drawer.shader.phong.line.Bresenham
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class PhongShader : Shader {

    private val phong = Phong()

    private val bresenham = Bresenham()

    override fun draw(image: Image, obj: Obj, face: Face) {
        face.triangles.forEach { triangle ->
            val observerPosition = obj.observer.position.multiply(obj.worldView)
            val faceNormal = triangle.normalIn(obj)
            if (Vector3.cos(
                    observerPosition.minus(obj.viewVertices.acquire(triangle.third.vIndex)),
                    faceNormal
                ) <= 0
            ) return@forEach

            val lightPosition = observerPosition

            val color: (Pixel) -> Color = color@{ pixel ->
                phong.color(
                    obj,
                    pixel,
                    triangle.color,
                    lightPosition,
                    Color.RED,
                    observerPosition
                )
            }

            val list = mutableListOf(
                getPixel(obj, triangle.first, faceNormal),
                getPixel(obj, triangle.second, faceNormal),
                getPixel(obj, triangle.third, faceNormal),
            ).sortedBy { it.screen.y }
            val top = list[2]
            val middle = list[1]
            val bottom = list[0]

            drawHorizontal(
                image,
                color,
                bottom,
                ceil(bottom.screen.y).toInt()..floor(middle.screen.y).toInt(),
                step(bottom, middle),
                step(bottom, top)
            )
            drawHorizontal(
                image,
                color,
                top,
                ceil(middle.screen.y).toInt()..floor(top.screen.y).toInt(),
                step(top, bottom),
                step(top, middle)
            )
        }
    }

    private fun drawHorizontal(
        image: Image,
        color: (Pixel) -> Color,
        anchor: Pixel,
        ys: IntRange,
        firstStep: Pixel,
        secondStep: Pixel
    ) {
        for (y in ys) {
            bresenham.draw(
                image,
                betweenOnY(y, anchor, firstStep),
                betweenOnY(y, anchor, secondStep),
                color
            )
        }
    }

    private fun getPixel(obj: Obj, faceComponent: Face.Component, faceNormal: Vector3): Pixel {
        return Pixel(
            obj.vertices.acquire(faceComponent.vIndex),
            obj.viewVertices.acquire(faceComponent.vIndex),
            obj.viewNormals.acquireOrNull(faceComponent.vnIndex) ?: faceNormal
        )
    }

    private fun betweenOnY(y: Int, first: Pixel, step: Pixel): Pixel {
        val dy = y - first.screen.y
        return Pixel(
            Vector3(first.screen.x + step.screen.x * dy, y, first.screen.z + step.screen.z * dy),
            first.worldView.plus(step.worldView.multiply(dy)),
            first.normal.plus(step.normal.multiply(dy)),
        )
    }

    private fun step(first: Pixel, second: Pixel): Pixel {
        val dy = second.screen.y - first.screen.y
        if (abs(dy) < 1)
            return Pixel(
                Vector3(0),
                Vector3(0),
                Vector3(0),
            )
        return Pixel(
            second.screen.minus(first.screen).multiply(1.0 / dy),
            second.worldView.minus(first.worldView).multiply(1.0 / dy),
            second.normal.minus(first.normal).multiply(1.0 / dy),
        )
    }
}