package feature.viewer

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class ViewerModel : ScreenModel {

    private val _map = MutableStateFlow<JsonElement?>(null)
    val map = _map.asStateFlow()

    private val json = Json

    fun handle(content: String) = coroutineScope.launch {
        _map.value = withContext(Dispatchers.Default) {
            json.parseToJsonElement(content)
        }
    }
}