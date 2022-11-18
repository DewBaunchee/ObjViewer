package by.poit.app.domain.display.drawer.lightning

import by.poit.app.domain.display.drawer.context.DisplayContext
import by.poit.app.domain.display.drawer.shader.phong.pixel.Pixel
import by.poit.app.domain.model.obj.Obj
import java.awt.Color

interface Colorizer {

    fun render(context: DisplayContext, obj: Obj, pixel: Pixel): Color
}