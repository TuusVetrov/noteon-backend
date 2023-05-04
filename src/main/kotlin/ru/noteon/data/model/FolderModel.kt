package ru.noteon.data.model

import ru.noteon.data.entity.FolderEntity
import ru.noteon.data.entity.NoteEntity

data class FolderModel(
    val id: String,
    val folderName: String
) {
    companion object {
        fun fromEntity(entity: FolderEntity) = FolderModel(
            entity.id.value.toString(),
            entity.folderName,
        )
    }
}