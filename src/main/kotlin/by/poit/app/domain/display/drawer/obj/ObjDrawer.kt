package by.poit.app.domain.display.drawer.obj

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.lightning.Phong
import by.poit.app.domain.display.drawer.lightning.Texturing
import by.poit.app.domain.display.drawer.shader.phong.PhongShader
import by.poit.app.domain.model.obj.Obj

class ObjDrawer {

    var shader = PhongShader()

    private val phong = Phong()
    private val texturing = Texturing()

    fun draw(image: Image, obj: Obj) {
        obj.update(image.context, image.width, image.height)
        shader.colorizer = if (image.context.withTextures && obj.hasTextures()) texturing else phong
        image.context.light.position.set(image.context.observer.position)

        obj.source.faces.parallelStream().forEach { face ->
            shader.draw(image, obj, face)
        }
    }
}