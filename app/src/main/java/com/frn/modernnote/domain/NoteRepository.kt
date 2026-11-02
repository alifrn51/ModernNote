package com.frn.modernnote.domain

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun addNote(note: Note)
    fun deleteNote(noteId: Int)
    fun getNote(noteId: Int): Note
    fun getNotes(): Flow<List<Note>>
    fun searchNote(query: String): Flow<List<Note>>
    fun editNote(note: Note)
    fun switchPinnedStatus(noteId: Int)
}