package com.frn.modernnote.presentation.screens

import com.frn.modernnote.domain.Note

sealed interface NoteScreenCommand {
    data class InputSearchQuery(val query: String): NoteScreenCommand
    data class SwitchPinnedStatus(val noteId: Int): NoteScreenCommand
    // Temp
    data class DeleteNote(val noteId: Int): NoteScreenCommand
    data class EditNote(val note: Note): NoteScreenCommand
}