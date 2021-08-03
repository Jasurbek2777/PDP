package com.example.pdp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.InnerRvItemBinding
import com.example.pdp.databinding.LessonsItemBinding
import com.example.pdp.room.entity.Lesson
import com.example.pdp.room.entity.Module

class LessonsAdapter(var list: ArrayList<Lesson>, var itemOnClick: itemClick) :
    RecyclerView.Adapter<LessonsAdapter.Vh>() {

    inner class Vh(var item: LessonsItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(lesson: Lesson, position: Int) {
            item.btn.text = lesson.lesson_place.toString()
            item.btn.setOnClickListener {
                itemOnClick.itemOnClick(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        LessonsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
    interface itemClick {
        fun itemOnClick(lesson: Lesson)
    }
}