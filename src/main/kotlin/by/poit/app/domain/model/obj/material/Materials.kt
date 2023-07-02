package by.poit.app.domain.model.obj.material

class Materials(materials: List<Material>, private val defaultMaterial: Material) {

    private val map = mutableMapOf(pairs = materials.map { it.name to it }.toTypedArray())

    fun mergeWith(materials: Materials) {
        map.putAll(materials.map)
    }

    fun get(material: String): Material {
        return map.getOrDefault(material, defaultMaterial)
    }
}