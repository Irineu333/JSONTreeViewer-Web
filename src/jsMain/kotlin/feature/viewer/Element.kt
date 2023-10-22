package feature.viewer

import kotlinx.serialization.json.JsonPrimitive

sealed class Element {

    data class Literal(
        val value: JsonPrimitive
    ) : Element()

    data class Object(
        val properties: List<Property>,
        val isCollapsed: Boolean = false
    ) : Element()


    data class Array(
        val elements: List<Element>,
        val isCollapsed: Boolean = false
    ) : Element()
}