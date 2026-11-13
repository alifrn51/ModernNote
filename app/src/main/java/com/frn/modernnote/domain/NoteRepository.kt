package com.frn.modernnote.domain

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(title: String,content: String, updateAt: Long,isPinned: Boolean)
    suspend fun deleteNote(noteId: Int)
    suspend fun getNote(noteId: Int): Note
    fun getNotes(): Flow<List<Note>>
    fun searchNote(query: String): Flow<List<Note>>
    suspend fun editNote(note: Note)
    suspend fun switchPinnedStatus(noteId: Int)
}