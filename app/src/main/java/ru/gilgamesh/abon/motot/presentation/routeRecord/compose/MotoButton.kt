package ru.gilgamesh.abon.motot.presentation.routeRecord.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.gilgamesh.abon.motot.presentation.routeRecord.compose.ButtonColors.ButtonTextColor
import ru.gilgamesh.abon.motot.presentation.routeRecord.compose.ButtonColors.GreyColor
import ru.gilgamesh.abon.motot.presentation.routeRecord.compose.ButtonColors.Orange
import ru.gilgamesh.abon.motot.presentation.routeRecord.compose.ButtonColors.RedColor

@Composable
fun MotoButton(
    modifier: Modifier,
    text: String,
    style: MotoButtonStyle = MotoButtonStyle.OrangeButton,
    onClick:() -> Unit
) {
    Button(
        modifier = modifier,
        colors = style.buttonColors,
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onClick.invoke()
        },
        content = {
            Text(text = text)
        }
    )
}

@Preview
@Composable
private fun MotoButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        listOf(
            MotoButtonStyle.OrangeButton,
            MotoButtonStyle.RedButton,
            MotoButtonStyle.GreyButton,
            MotoButtonStyle.TextButton
        ).forEach {
            MotoButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Кнопка",
                style = it,
                onClick = {}
            )
        }
    }
}

sealed class MotoButtonStyle(val buttonColors: ButtonColors) {
    object OrangeButton : MotoButtonStyle(
        ButtonColors(
            containerColor = Orange,
            contentColor = ButtonTextColor,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = ButtonTextColor
        )
    )

    object RedButton : MotoButtonStyle(
        ButtonColors(
            containerColor = RedColor,
            contentColor = ButtonTextColor,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = ButtonTextColor
        )
    )

    object GreyButton : MotoButtonStyle(
        ButtonColors(
            containerColor = GreyColor,
            contentColor = ButtonTextColor,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = ButtonTextColor
        )
    )

    object TextButton : MotoButtonStyle(
        ButtonColors(
            containerColor = Color.Unspecified,
            contentColor = RedColor,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = ButtonTextColor
        )
    )

}

object ButtonColors {
    val Orange = Color(0xffff964b)
    val RedColor = Color(0xffff5449)
    val GreyColor = Color(0xff948f99)
    val ButtonTextColor = Color(0xffffffff)
}