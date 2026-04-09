package com.example.final_project.ui.screens.translate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.final_project.domain.model.Dialect
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.ui.theme.DialectCentral
import com.example.final_project.ui.theme.DialectNorth
import com.example.final_project.ui.theme.DialectSouth
import com.example.final_project.ui.theme.GlossSBG
import com.example.final_project.ui.theme.GlossSBorder
import com.example.final_project.ui.theme.GlossTimeBG
import com.example.final_project.ui.theme.GlossTimeBorder
import com.example.final_project.ui.theme.StepWaiting
import com.example.final_project.ui.theme.SurfaceVariant
import com.example.final_project.ui.theme.TextDisabled
import com.example.final_project.ui.theme.TextHint

@Composable
fun DialectDropdown(
    selectedDialect: Dialect,
    onDialectSelected: (Dialect) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.5.dp, StepWaiting, RoundedCornerShape(100))
                .clickable { expanded = !expanded }
                .padding(
                    top = 6.dp,
                    bottom = 8.dp,
                    start = 12.dp,
                    end = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDialect.icon,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = stringResource(selectedDialect.displayName),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = getDialectColor(selectedDialect).first
            )

            Spacer(modifier = Modifier.width(4.dp))


            Text(
                text = if (expanded) "▴" else "▾",
                fontSize = 10.sp,
                color = TextDisabled
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(160.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Dialect.entries.forEach { dialect ->
                val isSelected = dialect == selectedDialect

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(dialect.icon, fontSize = 18.sp)

                            Spacer(Modifier.width(10.dp))

                            Column(Modifier.weight(1f)) {
                                Text(
                                    stringResource(dialect.displayName) ,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = getDialectColor(dialect).first
                                )
                                Text(
                                    dialect.subLabel,
                                    fontSize = 12.sp,
                                    color = TextHint
                                )
                            }

                            if (isSelected) {
                                Text(
                                    "✓",
                                    color = getDialectColor(dialect).first,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    onClick = {
                        onDialectSelected(dialect)
                        expanded = false
                    }
                )
            }
        }
    }
}
fun getDialectColor(dialect: Dialect): Triple<Color, Color, Color> {
    return when (dialect) {
        Dialect.NORTH ->  Triple(DialectNorth, Color(0x1AFF6B6B), Color(0x33FF6B6B))
        Dialect.CENTRAL -> Triple(DialectCentral,GlossTimeBG, GlossTimeBorder)
        Dialect.SOUTH -> Triple(DialectSouth,GlossSBG, GlossSBorder)
    }
}