package by.poit.app.viewer

import by.poit.app.domain.display.Observer
import by.poit.app.domain.display.adapter.ObjAdapter
import by.poit.app.domain.display.adapter.PrintAdapter
import by.poit.app.domain.display.adapter.input.InputAdapter
import by.poit.app.domain.display.adapter.input.Key
import by.poit.app.domain.model.primitive.Vector3
import java.awt.Canvas
import java.awt.Color


class Viewer : Canvas(), Runnable {

    private val inputAdapter = InputAdapter()
    private val printAdapter = PrintAdapter()
    private val objAdapter = ObjAdapter({ width }, { height }, printAdapter)

    private val observer = Observer()
    private var obj = ObjSuite.cube.toObj("Cube (Default)")

    private var running = false

    fun start() {
        running = true
        Thread(this).start()
    }

    override fun run() {
        var lastTime = System.currentTimeMillis()
        init()
        while (running) {
            val currentTime = System.currentTimeMillis()
            update(currentTime - lastTime)
            lastTime = currentTime
            render()
        }
    }

    private fun init() {
        createBufferStrategy(2)
        requestFocus()

        addKeyListener(inputAdapter)

        inputAdapter.subscribe(Key.F1) { printAdapter.generalEnabled = !printAdapter.generalEnabled }
        inputAdapter.subscribe(Key.F2) { printAdapter.verticesEnabled = !printAdapter.verticesEnabled }
        inputAdapter.subscribe(Key.MINUS) { observer.decreaseSpeed() }
        inputAdapter.subscribe(Key.PLUS) { observer.increaseSpeed() }
    }

    private fun render() {
        val graphics = bufferStrategy.drawGraphics

        graphics.color = Color.black
        graphics.fillRect(0, 0, width, height)

        objAdapter.draw(graphics, observer, obj)
        printAdapter.print(graphics, observer, obj)

        graphics.dispose()

        bufferStrategy.show()
    }

    private fun update(delta: Long) {
        inputAdapter.handle {
            on(Key.NUM2) { obj = ObjSuite.cube.toObj("Cube") } ||
                on(Key.NUM3) { obj = ObjSuite.star.toObj("Star") } ||
                on(Key.NUM4) { obj = ObjSuite.building.toObj("Building") } ||
                on(Key.NUM5) { obj = ObjSuite.ar15.toObj("AR 15") }

            on(Key.W) { observer.move(Vector3(0, 0, -delta * observer.speed)) }
            on(Key.S) { observer.move(Vector3(0, 0, delta * observer.speed)) }
            on(Key.A) { observer.move(Vector3(delta * observer.speed, 0, 0)) }
            on(Key.D) { observer.move(Vector3(-delta * observer.speed, 0, 0)) }
            on(Key.LSHIFT) { observer.move(Vector3(0, delta * observer.speed, 0)) }
            on(Key.LCTRL) { observer.move(Vector3(0, -delta * observer.speed, 0)) }

            on(Key.LEFT) { obj.rotate(Vector3(0, delta * observer.rotationSpeed, 0)) }
            on(Key.RIGHT) { obj.rotate(Vector3(0, -delta * observer.rotationSpeed, 0)) }
            on(Key.UP) { obj.rotate(Vector3(-delta * observer.rotationSpeed, 0, 0)) }
            on(Key.DOWN) { obj.rotate(Vector3(delta * observer.rotationSpeed, 0, 0)) }

            on(Key.T) { obj.scale(Vector3(-delta * obj.scaleSpeed, 0, 0)) }
            on(Key.Y) { obj.scale(Vector3(delta * obj.scaleSpeed, 0, 0)) }
            on(Key.G) { obj.scale(Vector3(0, -delta * obj.scaleSpeed, 0)) }
            on(Key.H) { obj.scale(Vector3(0, delta * obj.scaleSpeed, 0)) }
            on(Key.B) { obj.scale(Vector3(0, 0, -delta * obj.scaleSpeed)) }
            on(Key.N) { obj.scale(Vector3(0, 0, delta * obj.scaleSpeed)) }

            on(Key.R) { obj.reset(); observer.reset() }
        }
    }
}