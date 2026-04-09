package com.example.final_project.domain.model

enum class GlossRole(val label: String) {
    S("S"),
    V("V"),
    O("O"),
    TIME("TIME"),
    PLACE("PLACE"),
}

data class GlossToken(
    val role:  GlossRole,
    val label: String,
)
