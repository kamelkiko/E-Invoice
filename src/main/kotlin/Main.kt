import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import di.AppModule
import org.koin.core.context.startKoin
import presentation.app.App
import java.awt.Dimension

fun main() = application {
    val state: WindowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition(Alignment.Center),
    )
    Window(
        title = "EInvoice",
        state = state,
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(1200, 700)
        startKoin {
            modules(AppModule)
            createEagerInstances()
        }
        App()
    }
}