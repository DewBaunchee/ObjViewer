package by.poit.app.domain.model.obj

import by.poit.app.domain.model.primitive.Vector3
import java.io.File

class ObjParser {

    fun parse(file: File): Obj.Source {
        val vertices = mutableListOf<Vertex>()
        val polygons = mutableListOf<Polygon>()
        val normals = mutableListOf<Vector3>()
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
                    val vertexTextureIndices = mutableListOf<Int>()
                    val vertexNormalsIndices = mutableListOf<Int>()

                    parts.forEachIndexed { partIndex, part ->
                        if (partIndex == 0 || part.isBlank()) return@forEachIndexed

                        part.split("/")
                            .let { parts -> parts.filter { it.isNotBlank() } }
                            .let {
                                vertexIndices.add(it[0].toInt())
                                vertexTextureIndices.add(it.getOrElse(1) { "0" }.toInt())
                                vertexNormalsIndices.add(it.getOrElse(2) { "0" }.toInt())
                            }
                    }


                    polygons.add(Polygon(
                        vertexIndices.mapIndexed {index, value ->
                            Polygon.Component(value, vertexTextureIndices[index], vertexNormalsIndices[index])
                        }
                    ))
                }

                "vn" -> {
                    normals.add(
                        Vector3(
                            parts[1].toDouble(),
                            parts[2].toDouble(),
                            parts[3].toDouble(),
                        )
                    )
                }
            }
        }
        return Obj.Source(
            vertices,
            polygons,
            normals
        )
    }
}