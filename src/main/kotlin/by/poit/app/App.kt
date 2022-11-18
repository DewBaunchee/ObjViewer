package by.poit.app

import by.poit.app.viewer.Viewer
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JCheckBox

import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider

const val WIDTH = 400
const val HEIGHT = 300

fun main() {
    val viewer = Viewer()
    viewer.preferredSize = Dimension(WIDTH, HEIGHT)

    val frame = JFrame("3D Viewer")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.layout = BorderLayout()
    frame.add(createPanel(viewer), BorderLayout.NORTH)
    frame.add(viewer, BorderLayout.CENTER)
    frame.pack()
    frame.isVisible = true
    frame.extendedState = frame.extendedState or JFrame.MAXIMIZED_BOTH

    viewer.start()
}

fun createPanel(viewer: Viewer): JPanel {
    return JPanel().apply {
        createCheckbox(this, "With textures") {
            viewer.objDrawer.settings.withTextures = it
        }
        createSlider(this, "Ambient", 0, 100) {
            viewer.objDrawer.settings.ambient = it / 100.0
        }
        createSlider(this, "Mirror", 0, 100) {
            viewer.objDrawer.settings.mirrorLightCoefficient = it / 100.0
        }
        createSlider(this, "Shininess", 0, 128) {
            viewer.objDrawer.settings.shininess = it / 1.0
        }
    }
}

fun createCheckbox(panel: JPanel, label: String, setter: (Boolean) -> Unit) {
    panel.apply {
        val checkbox = JCheckBox(label)
        setter(checkbox.isSelected)
        checkbox.addChangeListener {
            setter(checkbox.isSelected)
        }
        add(checkbox)
    }
}

fun createSlider(panel: JPanel, name: String, min: Int, max: Int, setter: (Int) -> Unit) {
    panel.apply {
        add(JLabel(name))
        val slider = JSlider(min, max)
        setter(slider.value)
        slider.addChangeListener {
            setter(slider.value)
        }
        add(slider)
    }
}