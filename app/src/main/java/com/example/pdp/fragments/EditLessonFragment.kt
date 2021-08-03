package com.example.pdp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.databinding.FragmentEditLessonBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Lesson

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditLessonFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null
    lateinit var binding: FragmentEditLessonBinding
    lateinit var db: AppDataBase.AppDatabase
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
    ): View? {
        db = AppDataBase.AppDatabase.getInstance(requireContext())
        binding = FragmentEditLessonBinding.inflate(inflater, container, false)
        val lesson = db.lessonDao().getById(param1!!)
        val module = db.moduleDao().getById(lesson.lesson_module_id!!)
        binding.moduleName.text = module.module_name
        binding.lessonName.setText(lesson.lesson_name)
        binding.lessonDesc.setText(lesson.lesson_desc)
        binding.lessonPosition.setText(lesson.lesson_place.toString())
        binding.add1.setOnClickListener {
            val name = binding.lessonName.text.toString()
            val desc = binding.lessonDesc.text.toString()
            val pos = binding.lessonPosition.text.toString()
            if (name.isNotEmpty() && pos.isNotEmpty() && desc.isNotEmpty()) {
                lesson.lesson_name = name
                lesson.lesson_place = pos.toInt()
                lesson.lesson_desc = desc
                db.lessonDao().edit(lesson)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireActivity(), "Malumotlar to'ldirilmagan", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditLessonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}