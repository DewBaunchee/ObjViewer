package by.poit.app.domain.display.drawer.polygon

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.acquire
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Polygon

class VertexShader : Shader() {

    override fun draw(image: Image, obj: Obj, polygon: Polygon) {
        val color = polygon.color
        val polygonVertices = polygon.components.map { obj.vertices.acquire(it.vIndex) }
        lineDrawer.draw(image, polygonVertices[0], polygonVertices.last(), color)
        for (i in 1 until polygonVertices.size) {
            lineDrawer.draw(image, polygonVertices[i], polygonVertices[i - 1], color)
        }
    }
}