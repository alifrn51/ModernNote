package com.frn.modernnote.presentation.screens.notes

sealed interface NoteScreenCommand {
    data class InputSearchQuery(val query: String): NoteScreenCommand
    data class SwitchPinnedStatus(val noteId: Int): NoteScreenCommand

}