package feature.viewer

typealias Property = Pair<String, Element>

sealed interface Line {

    val indent: Int
    val ref : Element

    sealed interface Object : Line {

        data class Start(
            override val indent: Int,
            override val ref: Element.Object,
        ) : Object

        data class End(
            override val indent: Int,
            override val ref: Element.Object,
        ) : Object

        data class Literal(
            override val indent: Int,
            override val ref: Element.Literal,
            val key: String,
            val value: String
        ) : Object
    }


    sealed interface Array : Line {

        data class Start(
            override val indent: Int,
            override val ref: Element.Array
        ) : Array

        data class End(
            override val indent: Int,
            override val ref: Element.Array
        ) : Array

        data class Literal(
            override val indent: Int,
            override val ref: Element.Literal,
            val index: Int,
            val value: String
        ) : Array
    }
}
