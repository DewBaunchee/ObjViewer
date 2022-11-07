package by.poit.app.domain.display.drawer.shader

import by.poit.app.domain.display.drawer.shader.vertex.line.SimpleBresenham
import by.poit.app.domain.display.drawer.shader.vertex.line.SimpleLineDrawer

abstract class SimpleShader : Shader {

    var lineDrawer: SimpleLineDrawer = SimpleBresenham()
}