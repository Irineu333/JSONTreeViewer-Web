package feature.viewer

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*

class ViewerModel : ScreenModel {

    private val _elements = MutableStateFlow<Element?>(null)

    val lines = _elements.map { element ->
        element?.let {
            toLines(element)
        } ?: emptyList()
    }.stateIn(
        scope = coroutineScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed()
    )

    private val json = Json

    fun handle(content: String) = coroutineScope.launch {
        _elements.value = withContext(Dispatchers.Default) {
            json.parseToJsonElement(content).toElement()
        }
    }

    private fun JsonElement.toElement(): Element {
        return when (this) {
            is JsonPrimitive -> {
                Element.Literal(this)
            }

            is JsonObject -> {
                Element.Object(
                    properties = map {
                        it.key to it.value.toElement()
                    }
                )
            }

            is JsonArray -> {
                Element.Array(
                    elements = map {
                        it.toElement()
                    }
                )
            }
        }
    }

    sealed class Identify {
        data class Array(
            val index: Int
        ) : Identify()

        data class Object(
            val key: String
        ) : Identify()
    }

    private fun toLines(
        element: Element,
        indent: Int = 0,
        identify: Identify? = null
    ): List<Line> {
        return buildList {
            when (element) {
                is Element.Array -> {
                    add(
                        Line.Array.Start(
                            indent = indent,
                        )
                    )

                    element.elements.forEachIndexed { index, element ->
                        addAll(
                            toLines(
                                element = element,
                                indent = indent + 1,
                                Identify.Array(index)
                            )
                        )
                    }

                    add(
                        Line.Array.End(
                            indent = indent,
                        )
                    )
                }

                is Element.Literal -> {
                    when (identify) {
                        is Identify.Array -> {
                            add(
                                Line.Array.Literal(
                                    indent = indent,
                                    index = identify.index,
                                    value = element.value.toString()
                                )
                            )
                        }

                        is Identify.Object -> {
                            add(
                                Line.Object.Literal(
                                    indent = indent,
                                    key = identify.key,
                                    value = element.value.toString()
                                )
                            )
                        }

                        else -> error("invalid origin")
                    }
                }

                is Element.Object -> {
                    add(
                        Line.Object.Start(
                            indent = indent,
                        )
                    )

                    element.properties.forEach { element ->
                        addAll(
                            toLines(
                                element = element.second,
                                indent = indent + 1,
                                Identify.Object(element.first)
                            )
                        )
                    }

                    add(
                        Line.Object.End(
                            indent = indent,
                        )
                    )
                }
            }
        }
    }
}