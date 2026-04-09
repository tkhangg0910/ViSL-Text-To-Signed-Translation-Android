package com.example.final_project.ui.screens.translate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.R
import com.example.final_project.ui.theme.Border
import com.example.final_project.ui.theme.BorderLight
import com.example.final_project.ui.theme.Primary
import com.example.final_project.ui.theme.Surface
import com.example.final_project.ui.theme.SurfaceVariant
import com.example.final_project.ui.theme.TextDisabled
import com.example.final_project.ui.theme.TextPrimary
import com.example.final_project.ui.theme.TextHint

@Composable
fun InputCard(inputText: String,
              onTextChange: (String) -> Unit,
              deleteText: () -> Unit,
              pickExample: () -> Unit){
    val colors = MaterialTheme.colorScheme
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) colors.primary else colors.outline
    val glowColor = Color(0x1400C896)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(22.dp))
            .drawBehind{
                if (isFocused){
                    drawRoundRect(
                        color = glowColor,
                        size = size,
                        cornerRadius = CornerRadius(22.dp.toPx()))
                }
            }
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            )
    ){
        Column(modifier = Modifier.background(colors.surface)) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0x1400C896))
                        .border(
                            1.dp,
                            Color(0x2600C896),
                            RoundedCornerShape(100.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        text = stringResource(R.string.input_language_vi),
                        color = Primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${inputText.length} / 200",
                    color = TextHint,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            TextField(
                value = inputText,
                onValueChange = { onTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp)
                    .onFocusChanged{isFocused = it.isFocused},
                shape = RoundedCornerShape(22.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp
                ),
                placeholder = {
                    Text(stringResource(R.string.input_hint),
                        color = TextDisabled,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp
                    )},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor   = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    focusedTextColor        = colors.onSurface,
                    unfocusedTextColor      = colors.onSurface,
                    focusedIndicatorColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor             = colors.primary,
                    focusedPlaceholderColor = TextDisabled,
                    unfocusedPlaceholderColor = TextDisabled,
                ),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                ToolChip("🗑️", stringResource(R.string.delete)) {
                    deleteText()
                }

                Spacer(modifier = Modifier.width(8.dp))

                ToolChip("💡", stringResource(R.string.example)) {
                    pickExample()
                }
            }

        }
    }


}

@Composable
fun ToolChip(
    icon: String,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(100.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 11.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon)

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8896B3)
        )
    }
}