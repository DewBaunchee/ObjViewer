package by.poit.app

import by.poit.app.viewer.Viewer
import java.awt.BorderLayout
import java.awt.Dimension

import javax.swing.JFrame

const val WIDTH = 400
const val HEIGHT = 300

fun main() {
    val viewer = Viewer()
    viewer.preferredSize = Dimension(WIDTH, HEIGHT)

    val frame = JFrame("3D Viewer")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.layout = BorderLayout()
    frame.add(viewer, BorderLayout.CENTER)
    frame.pack()
    frame.isVisible = true
    frame.extendedState = frame.extendedState or JFrame.MAXIMIZED_BOTH

    viewer.start()
}