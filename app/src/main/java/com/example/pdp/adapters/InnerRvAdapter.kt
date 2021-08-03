package com.example.pdp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.InnerRvItemBinding
import com.example.pdp.room.entity.Module

class InnerRvAdapter(var list: ArrayList<Module>,var itemCLick1:itemClick) : RecyclerView.Adapter<InnerRvAdapter.Vh>() {
    inner class Vh(var item: InnerRvItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(module: Module, position: Int) {
            item.name.text = module.module_name
            item.root.setOnClickListener {
             itemCLick1.itemOnClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        InnerRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
    interface itemClick{
        fun itemOnClick(module: Module)
    }
}