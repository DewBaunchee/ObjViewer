package by.poit.app.domain.model.obj.material

class Materials(val materials: List<Material>) {

    fun merged(materials: Materials): Materials {
        return Materials(this.materials + materials.materials)
    }
}