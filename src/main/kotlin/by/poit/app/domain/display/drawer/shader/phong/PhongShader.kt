package by.poit.app.domain.display.drawer.shader.phong

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.lightning.Colorizer
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

    lateinit var colorizer: Colorizer

    private val bresenham = Bresenham()

    override fun draw(image: Image, obj: Obj, face: Face) {
        face.triangles.forEach { triangle ->
            val faceNormal = triangle.worldNormalIn(obj)
            if (triangle.normalIn(obj).z > 0) return@forEach

            triangle.updateTbn(obj)

            val color: (Pixel) -> Color = color@{ pixel ->
                colorizer.render(
                    image.context,
                    obj,
                    pixel,
                )
            }

            val list = listOf(
                triangle.first, triangle.second, triangle.third
            ).map {
                getPixel(obj, face, triangle, it, it.worldNormalIn(obj) ?: faceNormal)
            }.sortedBy { it.screen.y }

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

    private fun getPixel(
        obj: Obj,
        face: Face,
        triangle: Face.Triangle,
        faceComponent: Face.Component,
        faceNormal: Vector3
    ): Pixel {
        return Pixel(
            faceComponent.vertexIn(obj),
            faceComponent.worldVertexIn(obj),
            faceComponent.textureCoordinateIn(obj) ?: Vector3(0),
            faceComponent.worldNormalIn(obj) ?: faceNormal,
            face,
            triangle
        )
    }
}