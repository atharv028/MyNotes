package com.tare.mynotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tare.mynotes.dao.NotesDao
import com.tare.mynotes.entities.Notes

@Database(
    entities = [Notes::class],
    version = 1,
    exportSchema = false
)
abstract class NotesDatabase() : RoomDatabase() {
    companion object {
        private var notesDatabase: NotesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NotesDatabase {
            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "notes.db"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return notesDatabase!!
        }
    }
    abstract fun noteDao() : NotesDao
}