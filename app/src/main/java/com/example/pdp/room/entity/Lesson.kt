package com.example.pdp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_table")
class Lesson {
    @PrimaryKey(autoGenerate = true)
    var lesson_id: Int = 0
    var lesson_name: String? = null
    var lesson_desc: String? = null
    var lesson_module_id: Int? = null
    var lesson_place: Int? = null

    constructor()
    constructor(
        lesson_id: Int,
        lesson_name: String?,
        lesson_desc: String?,
        lesson_module_id: Int?,
        lesson_place: Int?
    ) {
        this.lesson_id = lesson_id
        this.lesson_name = lesson_name
        this.lesson_desc = lesson_desc
        this.lesson_module_id = lesson_module_id
        this.lesson_place = lesson_place
    }

    constructor(
        lesson_name: String?,
        lesson_desc: String?,
        lesson_module_id: Int?,
        lesson_place: Int?
    ) {
        this.lesson_name = lesson_name
        this.lesson_desc = lesson_desc
        this.lesson_module_id = lesson_module_id
        this.lesson_place = lesson_place
    }

}
