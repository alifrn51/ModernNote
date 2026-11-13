package com.frn.modernnote.domain.useCases

import com.frn.modernnote.domain.NoteRepository

class SwitchPinnedStatusUseCase(
    val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int){
        repository.switchPinnedStatus(id)
    }
}