package com.example.pdp.adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.ItemSettingRvBinding
import com.example.pdp.room.entity.Course

class SettingRvAdapter(var itemClick: onItemCLick) :
    ListAdapter<Course, SettingRvAdapter.Vh>(MyDiffUtil()) {
    inner class Vh(var item: ItemSettingRvBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(course: Course, position: Int) {
            item.itemName.text = course.name
            val image = course.image
            var bitmap = image?.size?.let { BitmapFactory.decodeByteArray(image, 0, it) }
            item.itemIamge.setImageBitmap(bitmap)
            item.edit.setOnClickListener {
                itemClick.itemEditClick(course, position)
            }
            item.delete.setOnClickListener {
                itemClick.itemDeleteClick(course, position)
            }
            item.root.setOnClickListener {
                itemClick.itemClick(course, position)
            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.course_id == newItem.course_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(ItemSettingRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }

    interface onItemCLick {
        fun itemEditClick(course: Course, position: Int)
        fun itemClick(course: Course, position: Int)
        fun itemDeleteClick(course: Course, position: Int)
    }
}