package com.example.pdp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pdp.room.dao.CourseDao
import com.example.pdp.room.dao.LessonDao
import com.example.pdp.room.dao.ModuleDao
import com.example.pdp.room.entity.Course
import com.example.pdp.room.entity.Lesson
import com.example.pdp.room.entity.Module

class AppDataBase {
     @Database(entities = [Course::class, Module::class, Lesson::class], version = 1)
         abstract class AppDatabase : RoomDatabase() {
             abstract fun moduleDao():ModuleDao
             abstract fun courseDao(): CourseDao
             abstract fun lessonDao(): LessonDao
             companion object {
                 private var instance: AppDatabase? = null

                 @Synchronized
                 fun getInstance(context: Context): AppDatabase {
                     if (instance == null)
                         instance = Room.databaseBuilder(
                             context.applicationContext,
                             AppDatabase::class.java,
                             "AppDataBase"
                         )
                             .fallbackToDestructiveMigration()
                             .allowMainThreadQueries()
                             .build()
                     return instance!!
                 }
             }
         }
}