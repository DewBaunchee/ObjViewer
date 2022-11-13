package by.poit.app.domain.display.drawer.shader.phong

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.display.drawer.lightning.Phong
import by.poit.app.domain.display.drawer.shader.Shader
import by.poit.app.domain.display.drawer.shader.phong.line.Bresenham
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.PixelLine
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.floor

class PhongShader : Shader {

    private val phong = Phong()

    private val bresenham = Bresenham()

    override fun draw(image: Image, obj: Obj, face: Face) {
        face.triangles.forEach { triangle ->
            val observerPosition = obj.observer.position
            val faceNormal = triangle.worldNormalIn(obj)
            if (triangle.normalIn(obj).z > 0) return@forEach

            val color: (Pixel) -> Color = color@{ pixel ->
                phong.color(
                    obj,
                    pixel,
                    triangle.color,
                    observerPosition,
                    Color.RED,
                    observerPosition
                )
            }

            val list = mutableListOf(
                getPixel(obj, triangle.first, triangle.first.worldNormalIn(obj) ?: faceNormal),
                getPixel(obj, triangle.second, triangle.second.worldNormalIn(obj) ?: faceNormal),
                getPixel(obj, triangle.third, triangle.third.worldNormalIn(obj) ?: faceNormal),
            ).sortedBy { it.screen.y }

            val top = list[2]
            val middle = list[1]
            val bottom = list[0]

            drawHorizontal(
                image,
                color,
                ceil(bottom.screen.y).toInt()..floor(middle.screen.y).toInt(),
                PixelLine(bottom, middle),
                PixelLine(bottom, top)
            )
            drawHorizontal(
                image,
                color,
                ceil(middle.screen.y).toInt()..floor(top.screen.y).toInt(),
                PixelLine(top, bottom),
                PixelLine(top, middle)
            )
        }
    }

    private fun drawHorizontal(
        image: Image,
        color: (Pixel) -> Color,
        ys: IntRange,
        firstSide: PixelLine,
        secondSide: PixelLine
    ) {
        for (y in ys) {
            bresenham.draw(
                image,
                firstSide.atY(y.toDouble()),
                secondSide.atY(y.toDouble()),
                color
            )
        }
    }

    private fun getPixel(obj: Obj, faceComponent: Face.Component, faceNormal: Vector3): Pixel {
        return Pixel(
            obj.vertices.acquire(faceComponent.vIndex),
            obj.worldVertices.acquire(faceComponent.vIndex),
            obj.viewVertices.acquire(faceComponent.vIndex),
            faceComponent.worldNormalIn(obj) ?: faceNormal
        )
    }
}