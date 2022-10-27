package by.poit.app.domain.display.drawer.obj

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.polygon.FlatShader
import by.poit.app.domain.display.drawer.polygon.Shader
import by.poit.app.domain.display.drawer.polygon.VertexShader
import by.poit.app.domain.model.obj.Obj


class ObjDrawer {

    var shader: Shader = FlatShader()

    fun draw(image: Image, obj: Obj) {
        obj.update(image.width, image.height)

        obj.source.polygons.stream().forEach { polygon ->
            shader.draw(image, obj, polygon)
            VertexShader().draw(image, obj, polygon)
        }
    }
}