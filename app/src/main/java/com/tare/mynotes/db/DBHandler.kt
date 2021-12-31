package com.tare.mynotes.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.util.Log
import com.tare.mynotes.entities.Notes

class DBHandler(private var context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        const val DB_NAME = "Notes"
        const val DB_VERSION = 1
        const val TABLE_NAME = "Notes"
        const val ID_COL = "id"
        const val TITLE_COL = "title"
        const val SUBTITLE_COL = "sub_title"
        const val DATETIME_COL = "date_time"
        const val NOTETEXT_COL = "note_text"
        const val IMGPATH_COL = "img_path"
        const val WEBLINK_COL = "web_link"
        const val COLOR_COL = "color"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1," + TITLE_COL +
                " TEXT," + SUBTITLE_COL + " TEXT," + DATETIME_COL + " TEXT," +
                NOTETEXT_COL + " TEXT," + IMGPATH_COL + " TEXT," + WEBLINK_COL +
                " TEXT," + COLOR_COL + " TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(note : Notes)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE_COL, note.title)
        values.put(SUBTITLE_COL, note.subTitle)
        values.put(DATETIME_COL, note.dateTime)
        values.put(NOTETEXT_COL, note.noteText)
        values.put(IMGPATH_COL, note.imgPath)
        values.put(WEBLINK_COL, note.webLink)
        values.put(COLOR_COL, note.color)
        val res = db.insert(TABLE_NAME, null, values)
        note.id = res
        Log.d("DB INS RES", note.toString())
    }

    fun allNotes() : MutableList<Notes>
    {
        val list : MutableList<Notes> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $ID_COL DESC"
        val res = db.rawQuery(query, null)
        if(res.moveToFirst())
        {
            do {
                val note = Notes()
                note.title = res.getString(res.getColumnIndexOrThrow(TITLE_COL))
                note.subTitle = res.getString(res.getColumnIndexOrThrow(SUBTITLE_COL))
                note.noteText = res.getString(res.getColumnIndexOrThrow(NOTETEXT_COL))
                note.dateTime = res.getString(res.getColumnIndexOrThrow(DATETIME_COL))
                note.color = res.getString(res.getColumnIndexOrThrow(COLOR_COL))
                note.id = res.getLong(res.getColumnIndexOrThrow(ID_COL))
                note.imgPath = res.getString(res.getColumnIndexOrThrow(IMGPATH_COL))
                note.webLink = res.getString(res.getColumnIndexOrThrow(WEBLINK_COL))
                list.add(note)
            }while (res.moveToNext())
        }
        res.close()
        return list
    }

    fun delete(noteId : Long)
    {
        val db = this.writableDatabase
        val res = db.delete(TABLE_NAME, "$ID_COL=$noteId", null)
        Log.d("DB DEL RES", res.toString())
        db.close()
    }

    fun getNote(noteID : Long) : Notes
    {
        val note = Notes()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID_COL = '$noteID'"
        val res = db.rawQuery(query, null)
        if(res.moveToFirst())
        {
            note.title = res.getString(res.getColumnIndexOrThrow(TITLE_COL))
            note.subTitle = res.getString(res.getColumnIndexOrThrow(SUBTITLE_COL))
            note.noteText = res.getString(res.getColumnIndexOrThrow(NOTETEXT_COL))
            note.dateTime = res.getString(res.getColumnIndexOrThrow(DATETIME_COL))
            note.color = res.getString(res.getColumnIndexOrThrow(COLOR_COL))
            note.id = res.getLong(res.getColumnIndexOrThrow(ID_COL))
            note.imgPath = res.getString(res.getColumnIndexOrThrow(IMGPATH_COL))
            note.webLink = res.getString(res.getColumnIndexOrThrow(WEBLINK_COL))
        }
        res.close()
        return note
    }

    fun updateNote(note: Notes)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE_COL, note.title)
        values.put(SUBTITLE_COL, note.subTitle)
        values.put(DATETIME_COL, note.dateTime)
        values.put(NOTETEXT_COL, note.noteText)
        values.put(IMGPATH_COL, note.imgPath)
        values.put(WEBLINK_COL, note.webLink)
        values.put(COLOR_COL, note.color)
        Log.d("ID", note.id.toString())
        val id = db.update(TABLE_NAME,values,"$ID_COL=${note.id}", null)
        Log.d("ID", id.toString())
        db.close()
    }
}