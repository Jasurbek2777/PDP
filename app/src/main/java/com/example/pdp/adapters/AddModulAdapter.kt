package com.example.pdp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.ModuleItemBinding
import com.example.pdp.room.entity.Course
import com.example.pdp.room.entity.Module

class AddModulAdapter( var itemClick: onItemCLick) :
    ListAdapter<Module, AddModulAdapter.Vh>(MyDiffUtil()) {
    inner class Vh(var item:  ModuleItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(module: Module, position: Int) {
            item.itemName.text = module.module_name
            item.modulePlace.text = module.module_place.toString()

            item.edit.setOnClickListener {
                itemClick.itemEditClick(module, position)
            }
            item.delete.setOnClickListener {
                itemClick.itemDeleteClick(module, position)
            }
            item.root.setOnClickListener {
                itemClick.itemClick(module, position)
            }
        }
    }


    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.module_id == newItem.module_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem == newItem
        }

    }


    interface onItemCLick {
        fun itemEditClick(module: Module, position: Int)
        fun itemClick(module: Module, position: Int)
        fun itemDeleteClick(module: Module, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddModulAdapter.Vh {
        return Vh(ModuleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }
}