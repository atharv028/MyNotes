package com.tare.mynotes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

class Notes{

    var title : String? = null

    var subTitle : String? = null

    var dateTime : String? = null

    var noteText : String? = null

    var imgPath : String? = null

    var webLink : String? = null

    var color : String? = null

    var id : Long = 0
    override fun toString(): String {
        return "$id : $title + $dateTime"
    }
}
