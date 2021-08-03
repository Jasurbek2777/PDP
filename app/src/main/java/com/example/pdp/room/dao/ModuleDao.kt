package com.example.pdp.room.dao

import androidx.room.*
import com.example.pdp.room.entity.Module

@Dao
interface ModuleDao {

    @Insert
    fun add(module: Module)

    @Delete
    fun delete(module: Module)

    @Update
    fun edit(module: Module)

    @Query("select * from module_table where module_course_id=:id order by module_place ")
    fun getAll(id: Int): List<Module>

    @Query("select * from module_table order by module_place ")
    fun getMOdules(): List<Module>

    @Query("select * from module_table where module_id=:id")
    fun getById(id: Int): Module
}