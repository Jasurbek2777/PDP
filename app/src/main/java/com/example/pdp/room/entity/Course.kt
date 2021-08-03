package com.example.pdp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table")
class Course {
    @PrimaryKey(autoGenerate = true)
    var course_id: Int = 0
    var name: String? = null
    var image: ByteArray? = null

    constructor(course_id: Int, name: String?, image: ByteArray?) {
        this.course_id = course_id
        this.name = name
        this.image = image
    }

    constructor()
    constructor(name: String?, image: ByteArray?) {
        this.name = name
        this.image = image
    }
}
