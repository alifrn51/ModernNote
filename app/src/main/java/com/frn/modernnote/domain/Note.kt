package com.frn.modernnote.domain

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val updateAt: Long,
    val isPinned: Boolean,
)
