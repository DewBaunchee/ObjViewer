package by.poit.app.domain.display.printer

import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.observer.Observer
import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class DebugPrinter {

    private val font = Font("Courier New", Font.PLAIN, 16)

    var generalEnabled = true
    var verticesEnabled = false

    fun print(graphics: Graphics, observer: Observer, obj: Obj) {
        printGeneral(graphics, observer, obj)
        printVertices(graphics, obj)
    }

    private fun printGeneral(graphics: Graphics, observer: Observer, obj: Obj) {
        if (!generalEnabled) return

        graphics.font = font
        graphics.color = Color.WHITE
        graphics.drawString(observer.toString(), 5, 15)
        graphics.drawString(obj.toString(), 5, 35)
    }

    private fun printVertices(graphics: Graphics, obj: Obj) {
        if (!verticesEnabled) return

        graphics.font = font
        graphics.color = Color.WHITE
        var y = 55
        obj.viewVertices.forEach {
            graphics.drawString(it.toString(3), 5, y)
            y += 20
        }
    }
}