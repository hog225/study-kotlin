package org.yg.kotlinspring.openai.dto

data class StripElements(
    val characters: List<String>,
    val props: List<String>,
    val costumes: List<String>,
    val makeups: List<String>,
    val extra: List<String>,
    val specialEffects: List<String>,
) {

    constructor() : this(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())

    fun add(SPR: StripElements?): StripElements {
        return if (SPR != null) {
            StripElements(
                (characters + SPR.characters).distinct(),
                (props + SPR.props).distinct(),
                (costumes + SPR.costumes).distinct(),
                (makeups + SPR.makeups).distinct(),
                (extra + SPR.extra).distinct(),
                (specialEffects + SPR.specialEffects).distinct(),
            )
        } else {
            this
        }
    }
}
