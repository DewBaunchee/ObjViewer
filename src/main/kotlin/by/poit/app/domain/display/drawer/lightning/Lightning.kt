package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Polygon
import java.awt.Color

interface Lightning {

    fun render(obj: Obj, face: Polygon.Face): Color
}