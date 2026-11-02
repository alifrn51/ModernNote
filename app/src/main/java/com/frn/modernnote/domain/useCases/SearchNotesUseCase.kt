package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(
    val repository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNote(query)
    }
}