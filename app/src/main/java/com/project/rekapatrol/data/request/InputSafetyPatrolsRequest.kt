package com.project.rekapatrol.data.request

data class InputSafetyPatrolsRequest(
    val findings_path: List<String>,
    val findings_descriptions: String,
    val location: String,
    val category: String,
    val risk: String,
    val checkup_date: String
)
