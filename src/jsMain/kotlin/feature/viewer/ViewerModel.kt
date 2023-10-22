package feature.viewer

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
                            ref = element
                        )
                    )

                    if (!element.isCollapsed) {
                        element.elements.forEachIndexed { index, element ->
                            addAll(
                                toLines(
                                    element = element,
                                    indent = indent + 1,
                                    Identify.Array(index)
                                )
                            )
                        }
                    }

                    add(
                        Line.Array.End(
                            indent = indent,
                            ref = element
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
                                    value = element.value.toString(),
                                    ref = element
                                )
                            )
                        }

                        is Identify.Object -> {
                            add(
                                Line.Object.Literal(
                                    indent = indent,
                                    key = identify.key,
                                    value = element.value.toString(),
                                    ref = element
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
                            ref = element
                        )
                    )

                    if (!element.isCollapsed) {
                        element.properties.forEach { element ->
                            addAll(
                                toLines(
                                    element = element.second,
                                    indent = indent + 1,
                                    Identify.Object(element.first)
                                )
                            )
                        }
                    }

                    add(
                        Line.Object.End(
                            indent = indent,
                            ref = element
                        )
                    )
                }
            }
        }
    }

    fun toggle(element: Element) {
        _elements.update {
            it?.let {
                toggle(it, element)
            }
        }
    }

    private fun toggle(old: Element, line: Element): Element {
        return when (old) {
            is Element.Array -> {
                if (old == line) {
                    old.copy(isCollapsed = !old.isCollapsed)
                } else {
                    old.copy(
                        elements = old.elements.map {
                            toggle(it, line)
                        }
                    )
                }
            }

            is Element.Object -> {
                if (old == line) {
                    old.copy(isCollapsed = !old.isCollapsed)
                } else {
                    old.copy(
                        properties = old.properties.map {
                            it.first to toggle(it.second, line)
                        }
                    )
                }
            }

            else -> old
        }
    }
}