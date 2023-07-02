package by.poit.app.viewer

import by.poit.app.domain.model.obj.Obj
import by.poit.app.domain.model.obj.ObjParser
import by.poit.app.domain.model.obj.material.Material
import by.poit.app.domain.model.obj.material.Materials
import java.io.File

class ObjSuite {

    companion object {

        private val parser = ObjParser()
        val ar15 by lazy { parser.parse(File("models/ar15/ar_15.obj")) }
        val star by lazy { parser.parse(File("models/star/star.obj")) }
        val building by lazy { parser.parse(File("models/building.obj")) }
        val cube by lazy { parser.parse(File("models/cube.obj")) }
        val triangle by lazy { parser.parse(File("models/triangle/triangle.obj")) }
        val icosphere by lazy { parser.parse(File("models/icosphere.obj")) }
        val wheel by lazy { parser.parse(File("models/wheel/wheel.obj")) }
        val rubix by lazy { parser.parse(File("models/rubix/rubix.obj")) }
        val lamp by lazy { parser.parse(File("models/lamp/lamp.obj")) }
        val meat by lazy { parser.parse(File("models/meat/meat.obj")) }
        val seeds by lazy { parser.parse(File("models/seeds/seeds.obj")) }
        val xenos by lazy { parser.parse(File("models/xenos/xenos.obj")) }

        val empty by lazy {
            Obj.Source(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                Materials(emptyList(), Material.defaultMaterial)
            )
        }
    }
}