package by.poit.app.domain.display.drawer.context

import by.poit.app.domain.model.light.Light
import by.poit.app.domain.model.observer.Observer
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Color

class DisplayContext {

    var light  = Light(Vector3(0), Color.WHITE)
    var observer = Observer()

    var useMaterial = false
    var withTextures = false

    val diffuse = 1.0
    var ambient = 0.0
    var mirrorLightCoefficient = 0.0
    var shininess = 0.0
}