package com.example.final_project.domain.model

import androidx.compose.ui.res.stringResource
import com.example.final_project.R

enum class Dialect(
    val apiValue:    String,
    val displayName: Int,
    val icon:        String,
    val subLabel:    String,
) {
    NORTH(
        apiValue    = "north",
        displayName = R.string.dialect_north,
        icon        = "🏛️",
        subLabel    = "Hà Nội",
    ),
    CENTRAL(
        apiValue    = "central",
        displayName = R.string.dialect_central,
        icon        = "⛩️",
        subLabel    = "Huế · Đà Nẵng",
    ),
    SOUTH(
        apiValue    = "south",
        displayName = R.string.dialect_south,
        icon        = "🌴",
        subLabel    = "TP.HCM",
    );

    companion object {
        fun fromApiValue(value: String): Dialect? =
            entries.find { it.apiValue == value }
    }
}
