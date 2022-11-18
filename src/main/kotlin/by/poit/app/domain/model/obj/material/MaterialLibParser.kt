package by.poit.app.domain.model.obj.material

import by.poit.app.domain.model.primitive.Vector3
import java.io.File

class MaterialLibParser {

    fun parse(file: File): Materials {
        val materials = mutableListOf<Material>()

        var currentBuilder: Material.Builder? = null

        file.forEachLine { line ->
            if (line.startsWith("#")) return@forEachLine

            val parts = line.split(" ").filter { it.isNotBlank() }.map { it.trim() }
            if (parts.isEmpty()) return@forEachLine

            when (parts[0]) {
                "newmtl" -> {
                    currentBuilder?.let { materials.add(it.build()) }
                    currentBuilder = Material.Builder(parts[1])
                }

                "Ka" -> {
                    currentBuilder?.ka = parseVector3(parts)
                }

                "Kd" -> {
                    currentBuilder?.kd = parseVector3(parts)
                }

                "Ks" -> {
                    currentBuilder?.ks = parseVector3(parts)
                }

                "Ns" -> {
                    currentBuilder?.ns = parts[1].toDouble()
                }

                "d" -> {
                    currentBuilder?.d = parts[1].toDouble()
                }

                "Tr" -> {
                    currentBuilder?.tr = parts[1].toDouble()
                }

                "illum" -> {
                    currentBuilder?.illum = parts[1].toInt()
                }
            }
        }

        currentBuilder?.let { materials.add(it.build()) }

        return Materials(materials)
    }

    private fun parseVector3(parts: List<String>): Vector3 {
        return Vector3(parts[1].toDouble(), parts[2].toDouble(), parts[3].toDouble())
    }
}