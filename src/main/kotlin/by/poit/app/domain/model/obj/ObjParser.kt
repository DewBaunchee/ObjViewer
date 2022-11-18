package by.poit.app.domain.model.obj

import by.poit.app.domain.model.obj.material.MaterialLibParser
import by.poit.app.domain.model.obj.material.Materials
import by.poit.app.domain.model.obj.texture.NormalMap
import by.poit.app.domain.model.obj.texture.Texture
import by.poit.app.domain.model.primitive.Vector3
import java.io.File
import java.nio.file.Paths

class ObjParser {

    private val materialLibParser = MaterialLibParser()

    fun parse(file: File): Obj.Source {
        val vertices = mutableListOf<Vertex>()
        val faces = mutableListOf<Face>()
        val normals = mutableListOf<Vector3>()
        val textureCoordinates = mutableListOf<Vector3>()

        var materials = Materials(emptyList())

        var currentMaterial = ""

        file.forEachLine { line ->
            if (line.startsWith("#")) return@forEachLine

            val parts = line.split(" ").filter { it.isNotBlank() }.map { it.trim() }
            if (parts.isEmpty()) return@forEachLine

            when (parts[0]) {
                "mtllib" -> {
                    File(file.parentFile.toPath().toAbsolutePath().toString() + "/" + parts[1]).let {
                        if (it.exists())
                            materials = materials.merged(materialLibParser.parse(it))
                    }
                }

                "usemtl" -> {
                    currentMaterial = parts[1]
                }

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


                    faces.add(
                        Face(
                            vertexIndices.mapIndexed { index, value ->
                                Face.Component(value, vertexTextureIndices[index], vertexNormalsIndices[index])
                            },
                            currentMaterial
                        )
                    )
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

                "vt" -> {
                    textureCoordinates.add(
                        Vector3(
                            parts[1].toDouble(),
                            parts[2].toDouble(),
                            parts.getOrElse(3) { "0" }.toInt().toDouble(),
                        )
                    )
                }
            }
        }

        return Obj.Source(
            vertices,
            faces,
            normals,
            textureCoordinates,
            materials,
            NormalMap.from(getImage(file, "normal.png")),
            Texture.from(getImage(file, "diffuse.png")),
            Texture.from(getImage(file, "specular.png")),
            Texture.from(getImage(file, "emission.png")),
        )
    }

    private fun getImage(file: File, name: String): File? {
        return Paths.get(
            file.toPath().toAbsolutePath().parent.toString(),
            name
        ).toFile()
    }
}