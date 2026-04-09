package com.example.final_project.domain.model

import com.example.final_project.R

data class QuickChipItem(
    val icon: Int,
    val text: String
)

val quickChips = listOf(
    QuickChipItem(R.string.qc_bank_label, "Tôi cần đến ngân hàng."),
    QuickChipItem(R.string.qc_hospital_label, "Tôi cần đến bệnh viện."),
    QuickChipItem(R.string.qc_health_label, "Tôi cảm thấy hơi mệt."),
    QuickChipItem(R.string.qc_greeting_label, "Xin chào, bạn có khỏe không?"),
    QuickChipItem(R.string.qc_help_label, "Tôi cần giúp đỡ.")
)