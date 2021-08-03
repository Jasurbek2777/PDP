package com.example.pdp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.adapters.LessonsAdapter
import com.example.pdp.databinding.FragmentOneModulBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Lesson

private lateinit var binding_module: FragmentOneModulBinding
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var db: AppDataBase.AppDatabase

class OneModulFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null

    private lateinit var adapter: LessonsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = AppDataBase.AppDatabase.getInstance(requireContext())
        binding_module = FragmentOneModulBinding.inflate(inflater, container, false)
        val module = db.moduleDao().getById(param1!!)
        binding_module.moduleName.text = module.module_name
        adapter = LessonsAdapter(db.lessonDao().getAll(param1!!) as ArrayList<Lesson>,
            object : LessonsAdapter.itemClick {
                override fun itemOnClick(lesson: Lesson) {
                    val bundle = Bundle()
                    bundle.putInt("param1", lesson.lesson_id)
                    findNavController().navigate(R.id.oneLessonFragment, bundle)
                }

            })
        binding_module.rv.adapter = adapter
        return binding_module.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OneModulFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}