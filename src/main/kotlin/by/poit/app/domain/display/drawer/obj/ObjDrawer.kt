package by.poit.app.domain.display.drawer.obj

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.shader.Shader
import by.poit.app.domain.display.drawer.shader.phong.PhongShader
import by.poit.app.domain.model.obj.Obj
import by.poit.app.viewer.settings.ObjDrawerSettings


class ObjDrawer {

    val settings = ObjDrawerSettings()

    var shader: Shader = PhongShader()

    fun draw(image: Image, obj: Obj) {
        obj.update(image.width, image.height)

        obj.kA = settings.ambient
        obj.kS = settings.mirrorLightCoefficient
        obj.shininess = settings.shininess

        obj.source.faces.parallelStream().forEach { face ->
            shader.draw(image, obj, face)
        }
    }
}