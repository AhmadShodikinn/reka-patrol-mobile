package com.project.rekapatrol.ui.helper

enum class EntryStatus {
    BELUM_KONFIRMASI,
    TERKONFIRMASI,
    DITOLAK,
    SUDAH_TINDAK_LANJUT
}

enum class Action {
    EDIT,
    KONFIRMASI,
    TOLAK_KONFIRMASI,
    TINDAK_LANJUT,
    EVALUASI,
    HAPUS
}

fun resolveStatus(isValidEntry: Boolean?, isSolved: Boolean): EntryStatus {
    return if (isSolved) EntryStatus.SUDAH_TINDAK_LANJUT
    else when (isValidEntry) {
        null -> EntryStatus.BELUM_KONFIRMASI
        true -> EntryStatus.TERKONFIRMASI
        false -> EntryStatus.DITOLAK
    }
}

fun getAllowedActions(role: String, status: EntryStatus): List<Action> {
    if (status == EntryStatus.SUDAH_TINDAK_LANJUT) return emptyList()

    val base = mutableSetOf<Action>()

    when (status) {
        EntryStatus.BELUM_KONFIRMASI -> {
            base.addAll(setOf(Action.EDIT, Action.KONFIRMASI, Action.TOLAK_KONFIRMASI, Action.EVALUASI, Action.HAPUS))
        }
        EntryStatus.TERKONFIRMASI -> {
            base.addAll(setOf(Action.EDIT, Action.TINDAK_LANJUT, Action.TOLAK_KONFIRMASI, Action.EVALUASI, Action.HAPUS))
        }
        EntryStatus.DITOLAK -> {
            base.addAll(setOf(Action.EDIT, Action.KONFIRMASI, Action.EVALUASI, Action.HAPUS))
        }
        else -> {}
    }

    return when (role) {
        "SHE", "5R" -> base.toList()
        "PIC" -> base.filterNot { it == Action.EDIT || it == Action.HAPUS }
        "Manajemen" -> listOf(Action.EVALUASI)
        else -> emptyList()
    }
}