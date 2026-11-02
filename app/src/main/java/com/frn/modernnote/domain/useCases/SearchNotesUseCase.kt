package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase {
    operator fun invoke(query: String): Flow<List<Note>> {
        TODO()// TODO: impl searchNoteUseCase
    }
}