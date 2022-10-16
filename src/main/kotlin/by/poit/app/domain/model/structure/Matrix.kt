package by.poit.app.domain.model.structure


open class Matrix(val values: Array<DoubleArray>) {

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
}