package com.example.pdp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.ModuleRvItemBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Module

private lateinit var db: AppDataBase.AppDatabase

class AllModulAdapter(
    var context: Context,
    var list: ArrayList<Module>,
    var itemClick: onItemCLick
) : RecyclerView.Adapter<AllModulAdapter.Vh>() {
    inner class Vh(var item: ModuleRvItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(module: Module, position: Int) {
            item.itemName.text = module.module_name
            db = AppDataBase.AppDatabase.getInstance(context)
            item.itemCourseName.text = db.courseDao().getById(module.module_course_id!!).name
            item.root.setOnClickListener {
                itemClick.itemClick(module, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ModuleRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
    interface onItemCLick {
        fun itemClick(module: Module, position: Int)
    }
}