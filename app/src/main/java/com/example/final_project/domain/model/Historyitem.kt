package com.example.final_project.domain.model

data class HistoryItem(
    val id:          Long,
    val inputText:   String,
    val dialect:     Dialect,
    val glossTokens: List<GlossToken>,
    val tokens:      List<String>,
    val videoPath:   String?,
    val createdAt:   Long,
    val isStarred:   Boolean,
)