package com.example.pdp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.databinding.FragmentOneLessonBinding
import com.example.pdp.room.AppDataBase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class OneLessonFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private lateinit var binding: FragmentOneLessonBinding
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
        binding = FragmentOneLessonBinding.inflate(inflater, container, false)
        db = AppDataBase.AppDatabase.getInstance(requireContext())
        val lesson = db.lessonDao().getById(param1!!)
        binding.position.text = "${lesson.lesson_place.toString()}-dars"
        binding.desc.text = lesson.lesson_desc
        binding.name.text = lesson.lesson_name
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OneLessonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}