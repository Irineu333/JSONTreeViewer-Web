import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    val fileInput = document.getElementById("fileInput") as HTMLInputElement

    onWasmReady {
        CanvasBasedWindow(canvasElementId = "compose-canvas") {

            var file by remember { mutableStateOf<File?>(null) }

            LaunchedEffect(Unit) {
                fileInput.onchange = {
                    file = fileInput.files?.get(0).also { file = it }
                    null
                }
            }

            MaterialTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(file?.name ?: "no file")

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            fileInput.click()
                        }
                    ) {
                        Text("select")
                    }
                }
            }
        }
    }
}