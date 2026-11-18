package com.frn.modernnote.presentation.screens.editing

sealed interface EditNoteCommand {
    data class Save(
        val title: String = "",
        val content: String = ""
    ): EditNoteCommand
    data object Back: EditNoteCommand
    data object Delete: EditNoteCommand
}