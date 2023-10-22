package feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.viewer.ViewerScreen
import org.w3c.dom.HTMLInputElement
import org.w3c.files.get

data class HomeScreen(
    val fileInput: HTMLInputElement,
    val openFilePicker: () -> Unit
) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            fileInput.onchange = {
                val file = fileInput.files?.get(0)

                if (file != null) {
                    navigator.push(ViewerScreen(file))
                }

                null
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("JSON Tree Viewer", fontSize = 18.sp)

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    openFilePicker()
                }
            ) {
                Text("select")
            }
        }
    }
}