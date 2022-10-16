package by.poit.app.viewer

import by.poit.app.domain.model.obj.ObjParser
import java.io.File

class ObjSuite {

    companion object {

        private val parser = ObjParser()
        val ar15 by lazy { parser.parse(File("ar_15.obj")) }
        val star by lazy { parser.parse(File("star.obj")) }
        val building by lazy { parser.parse(File("building.obj")) }
        val cube by lazy { parser.parse(File("cube.obj")) }
    }
}