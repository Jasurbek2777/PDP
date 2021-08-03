package com.example.pdp.room.dao

import androidx.room.*
import com.example.pdp.room.entity.Lesson

@Dao
interface LessonDao {

    @Insert
    fun add(lesson: Lesson)

    @Delete
    fun delete(lesson: Lesson)

    @Update
    fun edit(lesson: Lesson)

    @Query("select * from lesson_table  where lesson_module_id=:id order by lesson_place")
    fun getAll(id: Int): List<Lesson>

    @Query("select * from lesson_table where lesson_id=:id")
    fun getById(id: Int): Lesson
}