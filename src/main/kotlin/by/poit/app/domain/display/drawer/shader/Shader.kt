package by.poit.app.domain.display.drawer.shader

import by.poit.app.domain.display.Image
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Obj

interface Shader {

    fun draw(image: Image, obj: Obj, face: Face)
}