package by.poit.app.domain.display.drawer.polygon

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.line.Bresenham
import by.poit.app.domain.display.drawer.line.LineDrawer
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.Polygon

abstract class Shader {

    var lineDrawer: LineDrawer = Bresenham()

    abstract fun draw(image: Image, obj: Obj, polygon: Polygon)
}