package presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T> EventHandler(
    effects: Flow<T>,
    handleEffect: (T, Navigator) -> Unit,
) {
    val navController = LocalNavigator.currentOrThrow
    LaunchedEffect(key1 = Unit) {
        effects.collectLatest { effect ->
            handleEffect(effect, navController)
        }
    }
}