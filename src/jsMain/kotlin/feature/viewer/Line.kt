package feature.viewer

typealias Property = Pair<String, Element>

sealed interface Line {

    val indent: Int

    sealed interface Object : Line {

        data class Start(
            override val indent: Int,
        ) : Object

        data class End(
            override val indent: Int,
        ) : Object

        data class Literal(
            override val indent: Int,
            val key: String,
            val value: String
        ) : Object
    }


    sealed interface Array : Line {

        data class Start(
            override val indent: Int,
        ) : Array

        data class End(
            override val indent: Int,
        ) : Array

        data class Literal(
            override val indent: Int,
            val index: Int,
            val value: String
        ) : Array
    }
}
