package com.example.pdp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp.databinding.SupperRvItemBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Course
import com.example.pdp.room.entity.Module

class HomeSupperRvAdapter(
    var context: Context,
    var list: ArrayList<Course>,
    var itemCLick: itemOnClick
) :
    RecyclerView.Adapter<HomeSupperRvAdapter.Vh>() {
    inner class Vh(var item: SupperRvItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(course: Course) {
            item.name.text = course.name
            item.allBtn.setOnClickListener {
                itemCLick.itemALlCLick(course.course_id)
            }
            val moduls = ArrayList<Module>()
            val arrayList = AppDataBase.AppDatabase.getInstance(context).moduleDao()
                .getMOdules() as ArrayList<Module>
            for (i in arrayList) {
                if (course.course_id == i.module_course_id) {
                    moduls.add(i)
                }

            }
            var innerAdapter = InnerRvAdapter(moduls, object : InnerRvAdapter.itemClick {
                override fun itemOnClick(module: Module) {
                    itemCLick.childClickCLick(module)
                }

            })
            item.rv.adapter = innerAdapter

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        SupperRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface itemOnClick {
        fun itemALlCLick(id: Int)
        fun childClickCLick(module: Module)
    }
}