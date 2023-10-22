package feature.viewer

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import org.w3c.files.File
import org.w3c.files.FileReader

data class ViewerScreen(val file: File) : Screen {

    private val reader = FileReader()

    @Composable
    override fun Content() {

        var content by remember { mutableStateOf<String?>(null) }

        reader.onload = {
            content = reader.result as String

            null
        }

        LaunchedEffect(Unit) {
            reader.readAsText(file)
        }

        if (content != null) {
            SelectionContainer {
                Text(content!!)
            }
        } else {
            CircularProgressIndicator()
        }
    }
}