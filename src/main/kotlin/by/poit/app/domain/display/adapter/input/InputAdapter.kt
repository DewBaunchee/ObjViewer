package by.poit.app.domain.display.adapter.input

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class InputAdapter : KeyAdapter() {

    private val pressedKeySet = HashSet<Int>()

    private val subscribers = HashMap<Int, MutableList<() -> Unit>>()

    override fun keyPressed(e: KeyEvent?) {
        super.keyPressed(e)
        if (e == null) return

        pressedKeySet.add(e.keyCode)
        subscribers.getOrDefault(e.keyCode, listOf()).forEach { it() }
    }

    override fun keyReleased(e: KeyEvent?) {
        super.keyReleased(e)
        if (e == null) return

        pressedKeySet.remove(e.keyCode)
    }

    fun handle(function: InputAdapter.() -> Unit) {
        function()
    }

    fun on(key: Int, handler: () -> Unit): Boolean {
        if (isPressed(key)) {
            handler()
            return true
        }
        return false
    }

    fun subscribe(key: Int, handler: () -> Unit) {
        subscribers.compute(key) { _, list ->
            if (list == null) return@compute mutableListOf(handler)
            list.add(handler)
            return@compute list
        }
    }

    private fun isPressed(keyCode: Int): Boolean {
        return pressedKeySet.contains(keyCode)
    }
}
