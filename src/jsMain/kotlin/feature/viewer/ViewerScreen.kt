package feature.viewer

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.w3c.files.File
import org.w3c.files.FileReader

data class ViewerScreen(val file: File) : Screen {

    private val reader = FileReader()

    @Composable
    override fun Content() {

        val viewModel = rememberScreenModel { ViewerModel() }

        val map = viewModel.map.collectAsState().value

        LaunchedEffect(Unit) {
            reader.onload = {
                val content = reader.result as String

                viewModel.handle(content)

                null
            }

            reader.readAsText(file)
        }

        if (map != null) {
            Text("$map")
        } else {
            CircularProgressIndicator()
        }
    }
}