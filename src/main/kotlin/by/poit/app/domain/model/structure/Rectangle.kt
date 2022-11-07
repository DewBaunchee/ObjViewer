package by.poit.app.domain.model.structure

class Rectangle(val x: Double, val y: Double, val width: Double, val height: Double) {

    val left get() = x
    val top get() = y
    val right get() = x + width
    val bottom get() = y + height

    fun contains(x: Double, y: Double): Boolean {
        return containsX(x) && containsY(y)
    }

    fun containsX(x: Double): Boolean {
        return x in left..right
    }

    fun containsY(y: Double): Boolean {
        return y in top..bottom
    }
}