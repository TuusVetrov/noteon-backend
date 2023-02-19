package ru.noteon.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object NoteTable: UUIDTable() {
    val author = reference("author", UserTable)
    var title = varchar("noteTitle", length = 30)
    var body = text("noteBody")
    var created = datetime("created").default(DateTime.now())
    var updated = datetime("updated").default(DateTime.now())
    var isPinned = bool("isPinned").default(false)
}