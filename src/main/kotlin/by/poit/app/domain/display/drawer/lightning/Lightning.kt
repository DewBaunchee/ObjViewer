package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Face
import by.poit.app.domain.model.obj.Vertex
import java.awt.Color

interface Lightning {

    fun render(obj: Obj, lightning: Vertex, triangle: Face.Triangle, ambient: Double): Color
}