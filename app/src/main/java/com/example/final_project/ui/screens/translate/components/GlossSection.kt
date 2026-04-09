package com.example.final_project.ui.screens.translate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.R
import com.example.final_project.domain.model.GlossRole
import com.example.final_project.domain.model.GlossToken
import com.example.final_project.ui.theme.Border
import com.example.final_project.ui.theme.GlossTime
import com.example.final_project.ui.theme.GlossV
import com.example.final_project.ui.theme.GlossS
import com.example.final_project.ui.theme.GlossO
import com.example.final_project.ui.theme.GlossOBG
import com.example.final_project.ui.theme.GlossOBorder
import com.example.final_project.ui.theme.GlossPlace
import com.example.final_project.ui.theme.GlossPlaceBG
import com.example.final_project.ui.theme.GlossPlaceBorder
import com.example.final_project.ui.theme.GlossSBG
import com.example.final_project.ui.theme.GlossSBorder
import com.example.final_project.ui.theme.GlossTimeBG
import com.example.final_project.ui.theme.GlossTimeBorder
import com.example.final_project.ui.theme.GlossVBG
import com.example.final_project.ui.theme.GlossVBorder
import com.example.final_project.ui.theme.StepWaiting
import com.example.final_project.ui.theme.Surface
import com.example.final_project.ui.theme.TextHint

@Composable
fun GlossSection(glossTokens: List<GlossToken>){
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = colors.outline,
                shape = RoundedCornerShape(22.dp)
            )
            .background(colors.surface,RoundedCornerShape(22.dp))
    ) {
        Text(modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            text = stringResource(R.string.gloss_section).uppercase(),
            fontSize = 13.sp,
            color = TextHint
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp,6.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            glossTokens.forEachIndexed { index, glossToken ->
                GlossCard(glossToken.role, glossToken.label)

            }
        }

    }
}
fun roleToColor(role: GlossRole): Triple<Color, Color, Color> {
    return when (role) {
        GlossRole.S -> Triple(GlossS, GlossSBG, GlossSBorder)
        GlossRole.V -> Triple(GlossV, GlossVBG, GlossVBorder)
        GlossRole.O -> Triple(GlossO, GlossOBG, GlossOBorder)
        GlossRole.PLACE -> Triple(GlossPlace, GlossPlaceBG, GlossPlaceBorder)
        GlossRole.TIME -> Triple(GlossTime, GlossTimeBG, GlossTimeBorder)
    }
}
@Composable
fun GlossCard(role: GlossRole,
              label: String,
              contentPadding: PaddingValues = PaddingValues(
                  horizontal = 16.dp,
                  vertical = 4.dp
              ),
              fontSize: TextUnit = 14.sp,
              borderWidth: Dp = 1.5.dp
){
    val (textColor, bgColor , borderColor) = roleToColor(role)
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .padding(contentPadding),

    ) {

        Text(text = "${role.label}: $label",
            fontSize = fontSize,
            color = textColor,
            fontWeight = FontWeight.W700
        )
    }
}