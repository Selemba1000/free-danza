import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.selemba.common.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
