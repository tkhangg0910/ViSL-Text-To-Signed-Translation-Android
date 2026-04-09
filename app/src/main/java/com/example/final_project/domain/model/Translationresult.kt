package com.example.final_project.domain.model

data class TranslationResult(
    val id:          Long,
    val inputText:   String,
    val dialect:     Dialect,
    val glossTokens: List<GlossToken>,
    val tokens:      List<String>,
    val videoPath:   String?,
)
