package by.poit.app.viewer

import by.poit.app.domain.display.Image
import by.poit.app.domain.display.drawer.context.DisplayContext
import by.poit.app.domain.display.drawer.obj.ObjDrawer
import by.poit.app.domain.display.printer.DebugPrinter
import by.poit.app.domain.model.primitive.Vector3
import by.poit.app.viewer.input.InputAdapter
import by.poit.app.viewer.input.Key
import java.awt.Canvas
import java.awt.Color


class Viewer : Canvas(), Runnable {

    private val inputAdapter = InputAdapter()
    private val debugPrinter = DebugPrinter()
    val context = DisplayContext()
    val objDrawer = ObjDrawer()

    private val observer get() = context.observer

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

        inputAdapter.subscribe(Key.F1) { debugPrinter.generalEnabled = !debugPrinter.generalEnabled }
        inputAdapter.subscribe(Key.F2) { debugPrinter.verticesEnabled = !debugPrinter.verticesEnabled }
        inputAdapter.subscribe(Key.MINUS) { context.observer.decreaseSpeed() }
        inputAdapter.subscribe(Key.PLUS) { observer.increaseSpeed() }
    }

    private fun render() {
        val graphics = bufferStrategy.drawGraphics

        graphics.color = Color.black
        graphics.fillRect(0, 0, width, height)

        val image = Image(context, width, height)
        objDrawer.draw(image, obj)
        graphics.drawImage(image.drawable, 0, 0, null)
        debugPrinter.print(graphics, observer, obj)
        graphics.dispose()

        bufferStrategy.show()
    }

    private fun update(delta: Long) {
        inputAdapter.handle {
            on(Key.NUM1) { obj = ObjSuite.cube.toObj("Cube") } ||
                on(Key.NUM2) {
                    obj = ObjSuite.star.toObj("Star")
                    observer.position = Vector3(0, 0, -1000)
                } ||
                on(Key.NUM3) { obj = ObjSuite.meat.toObj("Meat") } ||
                on(Key.NUM4) {
                    obj = ObjSuite.wheel.toObj("Wheel")
                    observer.position = Vector3(0, 0, -300)
                } ||
                on(Key.NUM5) {
                    obj = ObjSuite.xenos.toObj("Xenomorf")
                    observer.position = Vector3(0, 0, -1000)
                } ||
                on(Key.NUM6) { obj = ObjSuite.icosphere.toObj("Icosphere") } ||
                on(Key.NUM7) { obj = ObjSuite.rubix.toObj("Rubix") } ||
                on(Key.NUM8) { obj = ObjSuite.lamp.toObj("Lamp") } ||
                on(Key.NUM9) { obj = ObjSuite.empty.toObj("Nothing") } ||
                on(Key.NUM0) { obj = ObjSuite.triangle.toObj("Triangle") }

            doMovement(delta)

            on(Key.LEFT) { obj.source.rotate(Vector3(0, -delta * observer.rotationSpeed, 0)) }
            on(Key.RIGHT) { obj.source.rotate(Vector3(0, delta * observer.rotationSpeed, 0)) }
            on(Key.UP) { obj.source.rotate(Vector3(delta * observer.rotationSpeed, 0, 0)) }
            on(Key.DOWN) { obj.source.rotate(Vector3(-delta * observer.rotationSpeed, 0, 0)) }

            on(Key.T) { obj.source.scale(Vector3(-delta * obj.source.scaleSpeed, 0, 0)) }
            on(Key.Y) { obj.source.scale(Vector3(delta * obj.source.scaleSpeed, 0, 0)) }
            on(Key.G) { obj.source.scale(Vector3(0, -delta * obj.source.scaleSpeed, 0)) }
            on(Key.H) { obj.source.scale(Vector3(0, delta * obj.source.scaleSpeed, 0)) }
            on(Key.B) { obj.source.scale(Vector3(0, 0, -delta * obj.source.scaleSpeed)) }
            on(Key.N) { obj.source.scale(Vector3(0, 0, delta * obj.source.scaleSpeed)) }

            on(Key.R) { obj.source.reset(); observer.reset() }
        }
    }

    private fun doMovement(delta: Long) {
        inputAdapter.handle {
            val speed = observer.speed * if (isPressed(Key.LSHIFT)) 2 else 1
            val range = delta * speed
            var shift = Vector3(0)
            if (isPressed(Key.W))
                shift = shift.plus(Vector3(0, 0, -range))
            if (isPressed(Key.S))
                shift = shift.plus(Vector3(0, 0, range))
            if (isPressed(Key.A))
                shift = shift.plus(Vector3(range, 0, 0))
            if (isPressed(Key.D))
                shift = shift.plus(Vector3(-range, 0, 0))
            if (isPressed(Key.SPACE))
                shift = shift.plus(Vector3(0, range, 0))
            if (isPressed(Key.LCTRL))
                shift = shift.plus(Vector3(0, -range, 0))

            observer.move(shift)
        }
    }
}