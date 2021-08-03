package com.example.pdp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module_table")
class Module {
    @PrimaryKey(autoGenerate = true)
    var module_id: Int = 0
    var module_name: String? = null
    var module_course_id: Int? = null
    var module_place: Int? = null

    constructor(module_id: Int, module_name: String?, module_course_id: Int?, module_place: Int?) {
        this.module_id = module_id
        this.module_name = module_name
        this.module_course_id = module_course_id
        this.module_place = module_place
    }

    constructor(module_name: String?, module_course_id: Int?, module_place: Int?) {
        this.module_name = module_name
        this.module_course_id = module_course_id
        this.module_place = module_place
    }

    constructor()
}
