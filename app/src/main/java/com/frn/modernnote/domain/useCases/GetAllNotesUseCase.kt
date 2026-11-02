package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(
    val repository: NoteRepository
) {

    operator fun invoke(): Flow<List<Note>>{
        return repository.getNotes()
    }

}