package feature.viewer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.w3c.files.File
import org.w3c.files.FileReader

data class ViewerScreen(val file: File) : Screen {

    private val reader = FileReader()

    @Composable
    override fun Content() {

        val viewModel = rememberScreenModel { ViewerModel() }

        val lines = viewModel.lines.collectAsState().value

        LaunchedEffect(Unit) {
            reader.onload = {
                val content = reader.result as String

                viewModel.handle(content)

                null
            }

            reader.readAsText(file)
        }

        if (lines.isNotEmpty()) {
            LazyColumn(Modifier.fillMaxWidth(0.5f)) {
                items(lines) { line ->
                    Row(Modifier.padding(start = (line.indent * 16).dp)) {
                        when (line) {
                            is Line.Array.Start ->  Text("[")
                            is Line.Array.End -> Text("],")
                            is Line.Array.Literal -> Text("${line.index} - ${line.value}")
                            is Line.Object.Start -> Text("{")
                            is Line.Object.End -> Text("},")
                            is Line.Object.Literal -> Text("${line.key} : ${line.value}")
                        }
                    }
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}