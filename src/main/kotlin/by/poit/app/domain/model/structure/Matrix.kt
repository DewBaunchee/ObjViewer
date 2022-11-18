package by.poit.app.domain.model.structure

import by.poit.app.domain.model.primitive.Vector3


open class Matrix(val values: Array<DoubleArray>) {

    val rows get() = values.size
    val cols get() = values[0].size

    constructor(rows: Int, cols: Int) : this(Array(rows) { DoubleArray(cols) { 0.0 } })
    constructor(first: Vector3, second: Vector3, third: Vector3) : this(
        arrayOf(
            doubleArrayOf(first.x, first.y, first.z, 0.0),
            doubleArrayOf(second.x, second.y, second.z, 0.0),
            doubleArrayOf(third.x, third.y, third.z, 0.0),
            doubleArrayOf(0.0, 0.0, 0.0, 0.0),
        )
    )

    fun multiply(matrix: Matrix): Matrix {
        return Matrix(
            Array(4) { outer ->
                DoubleArray(4) { inner ->
                    (0..3).sumOf {
                        values[outer][it] * matrix.values[it][inner]
                    }
                }
            }
        )
    }

    fun multiply(value: Double): Matrix {
        return Matrix(
            Array(4) { outer ->
                DoubleArray(4) { inner ->
                    values[outer][inner] * value
                }
            }
        )
    }

    fun sign(value: Int): Int {
        return if (value % 2 == 0) 1 else -1
    }

    fun subMatrix(excludingCol: Int, excludingRow: Int): Matrix {
        val mat = Matrix(rows - 1, cols - 1)
        var r = -1
        for (i in 0 until rows) {
            if (i == excludingRow) continue
            r++
            var c = -1
            for (j in 0 until cols) {
                if (j == excludingCol) continue
                mat.values[r][++c] = values[i][j]
            }
        }
        return mat
    }

    fun determinant(): Double {
        if (rows == 1) return values[0][0]
        if (rows == 2) return values[0][0] * values[1][1] - values[0][1] * values[1][0]
        var sum = 0.0
        for (i in 0 until cols) {
            sum += sign(i) * values[0][i] * subMatrix(0, i).determinant()
        }
        return sum
    }

    fun cofactor(): Matrix {
        val mat = Matrix(rows, cols)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                mat.values[i][j] = sign(i) * sign(j) * subMatrix(i, j).determinant()
            }
        }

        return mat
    }

    fun inverted(): Matrix {
        return cofactor().transposed().multiply(1.0 / determinant())
    }

    fun transposed(): Matrix {
        val temp = Array(cols) { DoubleArray(rows) }
        for (i in 0 until rows) for (j in 0 until cols) temp[j][i] = values[i][j]
        return Matrix(temp)
    }
}