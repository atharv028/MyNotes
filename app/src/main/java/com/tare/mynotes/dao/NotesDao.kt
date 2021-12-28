package com.tare.mynotes.dao

import androidx.room.*
import com.tare.mynotes.entities.Notes

@Dao
interface NotesDao {

    @get:Query("SELECT * FROM Notes ORDER BY id DESC")
    val allNotes : List<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Notes)

    @Delete
    suspend fun delete(note: Notes)
}