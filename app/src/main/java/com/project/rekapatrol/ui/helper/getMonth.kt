package com.project.rekapatrol.ui.helper

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getCurrentMonthDateRange(): Pair<String, String> {
    val now = LocalDate.now()
    val firstDay = now.withDayOfMonth(1)
    val lastDay = now.withDayOfMonth(now.lengthOfMonth())

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return Pair(firstDay.format(formatter), lastDay.format(formatter))
}
