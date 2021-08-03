package com.example.pdp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.databinding.FragmentEditModuleBinding
import com.example.pdp.room.AppDataBase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditModuleFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null
    lateinit var db: AppDataBase.AppDatabase
    lateinit var binding: FragmentEditModuleBinding
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
        binding = FragmentEditModuleBinding.inflate(inflater, container, false)
        val module = db.moduleDao().getById(param1!!)
        binding.moduleName.text = module.module_name
        binding.lessonName.setText(module.module_name)
        binding.lessonPosition.setText(module.module_place.toString())
        binding.add1.setOnClickListener {
            val name = binding.lessonName.text.toString()
            val position = binding.lessonPosition.text.toString()
            if (name.isNotEmpty() && position.isNotEmpty()) {
                module.module_name = name
                module.module_place = position.toInt()
                db.moduleDao().edit(module)
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditModuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}