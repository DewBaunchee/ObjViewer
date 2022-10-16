package by.poit.app.domain.display.adapter

import by.poit.app.domain.display.Observer
import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class PrintAdapter {

    private val font = Font("Courier New", Font.PLAIN, 16)

    var generalEnabled = true
    var verticesEnabled = false

    fun print(graphics: Graphics, observer: Observer, obj: Obj?) {
        if (!generalEnabled) return

        graphics.font = font
        graphics.color = Color.WHITE
        graphics.drawString(observer.toString(), 5, 15)
        graphics.drawString(obj?.toString() ?: "", 5, 35)
    }

    fun printVertices(graphics: Graphics, vertices: List<Vector3>) {
        if (!verticesEnabled) return

        graphics.font = font
        graphics.color = Color.WHITE
        var y = 55
        vertices.forEach {
            graphics.drawString(it.toString(3), 5, y)
            y += 20
        }
    }
}