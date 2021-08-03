package com.example.pdp.room.dao

import androidx.room.*
import com.example.pdp.room.entity.Course

@Dao
interface CourseDao {

    @Insert
    fun add(course: Course)

    @Delete
    fun delete(course: Course)

    @Update
    fun edit(course: Course)

    @Query("select * from course_table")
    fun getAll(): List<Course>

    @Query("select * from course_table where course_id=:id")
    fun getById(id: Int): Course
}