package com.project.rekapatrol.ui.helper

import android.icu.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.rekapatrol.ui.theme.skyblue

@Composable
fun FilterIndicator(
    selectedMonth: Int?,
    selectedYear: Int?,
    onClearFilter: () -> Unit
) {
    val monthNames = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    text = "Filter: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = buildString {
                        if (selectedMonth != null) {
                            append(monthNames[selectedMonth - 1])
                        }
                        if (selectedMonth != null && selectedYear != null) {
                            append(" ")
                        }
                        if (selectedYear != null) {
                            append(selectedYear)
                        }
                    },
                    fontSize = 14.sp,
                    color = skyblue,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = onClearFilter) {
                Text(
                    text = "Hapus",
                    color = skyblue,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentMonth: Int?,
    currentYear: Int?,
    onDismiss: () -> Unit,
    onApplyFilter: (month: Int?, year: Int?) -> Unit
) {
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    var showYearDropdown by remember { mutableStateOf(false) }

    val monthNames = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    val currentCalendar = Calendar.getInstance()
    val currentRealYear = currentCalendar.get(Calendar.YEAR)
    val yearRange = (currentRealYear - 10)..(currentRealYear + 10)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filter Temuan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Dropdown untuk memilih bulan
                ExposedDropdownMenuBox(
                    expanded = showMonthDropdown,
                    onExpandedChange = { showMonthDropdown = !showMonthDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedMonth?.let { monthNames[it - 1] } ?: "Semua Bulan",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Bulan") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMonthDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showMonthDropdown,
                        onDismissRequest = { showMonthDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua Bulan") },
                            onClick = {
                                selectedMonth = null
                                showMonthDropdown = false
                            }
                        )
                        monthNames.forEachIndexed { index, month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = index + 1
                                    showMonthDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = showYearDropdown,
                    onExpandedChange = { showYearDropdown = !showYearDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedYear?.toString() ?: "Semua Tahun",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Tahun") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showYearDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showYearDropdown,
                        onDismissRequest = { showYearDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua Tahun") },
                            onClick = {
                                selectedYear = null
                                showYearDropdown = false
                            }
                        )
                        yearRange.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year.toString()) },
                                onClick = {
                                    selectedYear = year
                                    showYearDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onApplyFilter(selectedMonth, selectedYear) },
                colors = ButtonDefaults.buttonColors(containerColor = skyblue)
            ) {
                Text("Terapkan", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = skyblue)
            }
        }
    )
}