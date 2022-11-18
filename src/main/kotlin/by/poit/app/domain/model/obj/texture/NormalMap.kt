package by.poit.app.domain.model.obj.texture

import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.domain.model.structure.Matrix
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.IIOException
import javax.imageio.ImageIO

class NormalMap(image: BufferedImage) : Texture(image) {

    companion object {

        fun from(file: File?): NormalMap? {
            return try {
                file?.let { ImageIO.read(file) }?.let { NormalMap(it) }
            } catch (ignored: IIOException) {
                null
            }
        }
    }

    fun tbn(obj: Obj, triangle: Face.Triangle): Matrix? {
        val v1 = triangle.first.worldVertexIn(obj)
        val v2 = triangle.second.worldVertexIn(obj)
        val v3 = triangle.third.worldVertexIn(obj)

        val t1 = triangle.first.textureCoordinateIn(obj)!!
        val t2 = triangle.second.textureCoordinateIn(obj)!!
        val t3 = triangle.third.textureCoordinateIn(obj)!!

        val edge1 = v2.minus(v1)
        val edge2 = v3.minus(v1)

        val dt1 = t2.minus(t1)
        val dt2 = t3.minus(t1)

        val f = 1.0 / (dt1.x * dt2.y - dt2.x * dt1.y).let { if (it == 0.0) return null else it }

        val tangent = Vector3(
            f * (dt2.y * edge1.x - dt1.y * edge2.x),
            f * (dt2.y * edge1.y - dt1.y * edge2.y),
            f * (dt2.y * edge1.z - dt1.y * edge2.z),
        ).normalized()

//        val bitangent = Vector3(
//            f * (-dt2.x * edge1.x + dt1.x * edge2.x),
//            f * (-dt2.x * edge1.y + dt1.x * edge2.y),
//            f * (-dt2.x * edge1.z + dt1.x * edge2.z),
//        ).transformed(obj.world).normalized()

        val normal = triangle.worldNormalIn(obj)
        val bitangent = normal.cross(tangent)

        return Matrix(tangent, bitangent, normal)
    }

    fun get(pixel: Pixel): Vector3 {
        return get(pixel.texel.x, pixel.texel.y)
    }

    private fun get(x: Double, y: Double): Vector3 {
        return value(x, y)
            .minus(0.5)
            .multiplied(2.0)
            .normalized()
    }
}