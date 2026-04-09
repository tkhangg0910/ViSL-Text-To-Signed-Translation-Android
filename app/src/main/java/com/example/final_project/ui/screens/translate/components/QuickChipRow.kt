package com.example.final_project.ui.screens.translate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.R
import com.example.final_project.domain.model.quickChips
import com.example.final_project.ui.theme.Primary
import com.example.final_project.ui.theme.StepWaiting
import com.example.final_project.ui.theme.Surface
import com.example.final_project.ui.theme.TextHint
import com.example.final_project.ui.theme.TextSecondary

@Composable
fun QuickChipRow(
    onClick: (String) -> Unit
) {

    Column() {
        Text(modifier = Modifier.padding(start = 6.dp),
            text = stringResource(R.string.common_sentences).uppercase(),
            fontSize = 13.sp,
            color = TextHint
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickChips.forEachIndexed { index, chip ->
                    QuickChipItemView(
                        icon = stringResource(chip.icon),
                        onClick = {
                            onClick(chip.text)
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun QuickChipItemView(
    icon: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, StepWaiting, RoundedCornerShape(100.dp))
            .clickable { onClick() }
            .padding(horizontal = 13.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon,fontSize = 13.sp, color = Color(0xFF8896B3))
    }
}