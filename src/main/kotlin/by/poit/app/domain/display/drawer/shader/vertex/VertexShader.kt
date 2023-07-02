package by.poit.app.domain.display.drawer.shader.vertex

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.display.drawer.shader.SimpleShader
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Obj
import java.awt.Color

class VertexShader : SimpleShader() {

    override fun draw(image: Image, obj: Obj, face: Face) {
        val color = Color.BLUE
        val polygonVertices = face.components.map { obj.vertices.acquire(it.vIndex) }
        lineDrawer.draw(image, polygonVertices[0], polygonVertices.last(), color)
        for (i in 1 until polygonVertices.size) {
            lineDrawer.draw(image, polygonVertices[i], polygonVertices[i - 1], color)
        }
    }
}