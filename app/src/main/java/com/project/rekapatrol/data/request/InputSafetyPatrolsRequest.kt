package com.project.rekapatrol.data.request

data class InputSafetyPatrolsRequest(
    val finding_paths: List<String>,
    val findings_description: String,
    val location: String,
    val category: String,
    val risk: String,
    val checkup_date: String
)
