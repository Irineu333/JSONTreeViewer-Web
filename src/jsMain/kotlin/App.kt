import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import feature.home.HomeScreen
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLInputElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    val fileInput = document.getElementById("fileInput") as HTMLInputElement

    onWasmReady {
        CanvasBasedWindow(canvasElementId = "compose-canvas") {
            MaterialTheme {
                Navigator(
                    HomeScreen(
                        fileInput = fileInput,
                        openFilePicker = {
                            fileInput.click()
                        }
                    )
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        Alignment.Center
                    ) {
                        CurrentScreen()
                    }
                }
            }
        }
    }
}