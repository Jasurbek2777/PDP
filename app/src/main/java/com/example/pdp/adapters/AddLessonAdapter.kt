package com.example.pdp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.AddLessonItemBinding
import com.example.pdp.databinding.ModuleItemBinding
import com.example.pdp.room.entity.Course
import com.example.pdp.room.entity.Lesson
import com.example.pdp.room.entity.Module

class AddLessonAdapter( var itemClick: onItemCLick) :
    ListAdapter<Lesson, AddLessonAdapter.Vh>(MyDiffUtil()) {
    inner class Vh(var item: AddLessonItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(lesson: Lesson, position: Int) {
            item.itemName.text = lesson.lesson_name
            item.itemDesc.text = lesson.lesson_desc

            item.edit.setOnClickListener {
                itemClick.itemEditClick(lesson, position)
            }
            item.delete.setOnClickListener {
                itemClick.itemDeleteClick(lesson, position)
            }
            item.root.setOnClickListener {
                itemClick.itemClick(lesson, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        AddLessonItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.lesson_id == newItem.lesson_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }

    }

    interface onItemCLick {
        fun itemEditClick(lesson: Lesson, position: Int)
        fun itemClick(lesson: Lesson, position: Int)
        fun itemDeleteClick(lesson: Lesson, position: Int)
    }
}