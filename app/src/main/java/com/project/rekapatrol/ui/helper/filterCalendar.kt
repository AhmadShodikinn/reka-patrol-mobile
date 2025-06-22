package com.project.rekapatrol.ui.helper

import android.icu.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.rekapatrol.ui.theme.skyblue

@Composable
fun FilterIndicator(
    selectedFrom: String?,
    selectedTo: String?,
    onClearFilter: () -> Unit
) {
    if (selectedFrom.isNullOrBlank() && selectedTo.isNullOrBlank()) return

    val filterLabel = when {
        !selectedFrom.isNullOrBlank() && !selectedTo.isNullOrBlank() ->
            "Dari $selectedFrom sampai $selectedTo"
        !selectedFrom.isNullOrBlank() -> "Dari $selectedFrom"
        !selectedTo.isNullOrBlank() -> "Sampai $selectedTo"
        else -> ""
    }


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF0F0F0)
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
                    text = filterLabel,
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
    currentFrom: String?,
    currentTo: String?,
    currentStatus: String?,
    onDismiss: () -> Unit,
    onApplyFilter: (from: String?, to: String?, status: String?) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var fromDate by remember { mutableStateOf(currentFrom) }
    var toDate by remember { mutableStateOf(currentTo) }

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    val statusOptions = listOf(
        "" to "Belum Dikonfirmasi",
        "1" to "Dikonfirmasi",
        "0" to "Ditolak"
    )

    var selectedStatus by remember { mutableStateOf(currentStatus) }
    var expandedStatus by remember { mutableStateOf(false) }

    // Show DatePicker dialogs
    if (showFromPicker) {
        android.app.DatePickerDialog(
            context,
            { _, y, m, d ->
                fromDate = "%04d-%02d-%02d".format(y, m + 1, d)
                showFromPicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    if (showToPicker) {
        android.app.DatePickerDialog(
            context,
            { _, y, m, d ->
                toDate = "%04d-%02d-%02d".format(y, m + 1, d)
                showToPicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Temuan") },
        text = {
            Column {
                Text("Rentang tanggal", fontWeight = FontWeight.Bold)
                OutlinedButton(onClick = { showFromPicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = fromDate ?: "Dari Tanggal")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { showToPicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = toDate ?: "Ke Tanggal")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Status Konfirmasi", fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = statusOptions.firstOrNull { it.first == selectedStatus }?.second ?: "Semua",
                        onValueChange = {},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(50)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedStatus,
                        onDismissRequest = { expandedStatus = false }
                    ) {
                        statusOptions.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedStatus = value
                                    expandedStatus = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApplyFilter(fromDate, toDate, selectedStatus)
                    onDismiss()
                }
            ) { Text("Terapkan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}


