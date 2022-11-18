package by.poit.app.domain.display.drawer.context

import by.poit.app.domain.model.light.Light
import by.poit.app.domain.model.observer.Observer
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color

class DisplayContext {

    var light  = Light(Vector3(0), Color.RED)
    var observer = Observer()
}