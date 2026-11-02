package com.frn.modernnote.data

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object TestNoteRepositoryImpl : NoteRepository {

    private val notesListFlow = MutableStateFlow<List<Note>>(emptyList())

    override fun addNote(note: Note) {
        notesListFlow.update {
            it + note
        }
    }

    override fun deleteNote(noteId: Int) {
        notesListFlow.update {
            it.toMutableList().apply {
                removeIf { note -> note.id == noteId }
            }
        }
    }

    override fun getNote(noteId: Int): Note {
        return notesListFlow.value.first { note -> note.id == noteId }
    }

    override fun getNotes(): Flow<List<Note>> {
        return notesListFlow.asStateFlow()
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesListFlow.map { currentList ->
            currentList.filter { it.title.contains(query) || it.content.contains(query) }
        }
    }

    override fun editNote(note: Note) {
        notesListFlow.update { oldList ->
            oldList.map {
                if (it.id == note.id) {
                    note
                } else {
                    it
                }
            }
        }
    }

    override fun switchPinnedStatus(noteId: Int) {
        notesListFlow.update { oldList ->
            oldList.map {
                if (it.id == noteId) {
                    it.copy(isPinned = !it.isPinned)
                } else {
                    it
                }
            }
        }
    }
}