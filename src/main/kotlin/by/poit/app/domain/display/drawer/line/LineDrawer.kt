package by.poit.app.domain.display.drawer.line

import by.poit.app.domain.display.Image
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color

interface LineDrawer {

    fun draw(image: Image, from: Vector3, to: Vector3, color: Color)
}