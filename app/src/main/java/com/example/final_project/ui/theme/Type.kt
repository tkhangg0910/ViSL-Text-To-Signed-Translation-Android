package com.example.final_project.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val ViSLTypography = Typography(

    headlineLarge = TextStyle(
        fontWeight    = FontWeight.Black,
        fontSize      = 22.sp,
        letterSpacing = (-0.5).sp,
    ),

    headlineMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize   = 18.sp,
    ),

    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 13.sp,
    ),

    bodyLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
    ),

    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        lineHeight = 20.sp,
    ),

    labelSmall = TextStyle(
        fontWeight    = FontWeight.Normal,
        fontSize      = 10.sp,
        letterSpacing = 0.4.sp,
    ),

    labelMedium = TextStyle(
        fontWeight    = FontWeight.ExtraBold,
        fontSize      = 10.sp,
        letterSpacing = 1.sp,
    ),
)

