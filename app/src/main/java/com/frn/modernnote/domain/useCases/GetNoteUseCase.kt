package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.Note
import com.frn.modernnote.domain.NoteRepository

class GetNoteUseCase(
    val repository: NoteRepository
) {

    operator fun invoke(id: Int): Note{
        return repository.getNote(id)
    }
}