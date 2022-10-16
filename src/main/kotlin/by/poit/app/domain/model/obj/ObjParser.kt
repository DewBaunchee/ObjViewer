package by.poit.app.domain.model.obj

import java.io.File

class ObjParser {

    fun parse(file: File): Obj.Source {
        val vertices = mutableListOf<Vertex>()
        val polygons = mutableListOf<Polygon>()
        file.forEachLine { line ->
            if (line.startsWith("#")) return@forEachLine

            val parts = line.split(" ")
            if (parts.isEmpty()) return@forEachLine

            when (parts[0]) {
                "v" -> {
                    vertices.add(
                        Vertex(
                            parts[1].toDouble(),
                            parts[2].toDouble(),
                            parts[3].toDouble(),
                            if (parts.size > 4) parts[4].toDouble() else 1.0
                        )
                    )
                }

                "f" -> {
                    val vertexIndices = mutableListOf<Int>()

                    parts.forEachIndexed { index, part ->
                        if (index == 0) return@forEachIndexed

                        part.split("/").forEachIndexed inner@{ index, value ->
                            if (value.isBlank()) return@inner
                            when (index) {
                                0 -> vertexIndices.add(value.toInt())
                            }
                        }
                    }

                    polygons.add(Polygon(vertexIndices))
                }
            }
        }
        return Obj.Source(vertices, polygons)
    }
}