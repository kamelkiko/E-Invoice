package design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.roboto_medium
import com.abapps.e_invoice.generated.resources.roboto_regular
import org.jetbrains.compose.resources.Font

@Composable
fun headlineLarge(): TextStyle {
    return TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.4.sp,
        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun headline(): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun titleLarge(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun title(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun titleMedium(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(Res.font.roboto_medium)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun body(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(Res.font.roboto_regular)),
        fontWeight = FontWeight.W400,
    )
}

@Composable
fun caption(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(Res.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
    )
}