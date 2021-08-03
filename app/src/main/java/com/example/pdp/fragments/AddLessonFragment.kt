package com.example.pdp.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.adapters.AddLessonAdapter
import com.example.pdp.adapters.AddModulAdapter
import com.example.pdp.databinding.FragmentAddLessonBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Lesson
import com.example.pdp.room.entity.Module

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding: FragmentAddLessonBinding
private lateinit var db: AppDataBase.AppDatabase

class AddLessonFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null
    lateinit var adapter: AddLessonAdapter
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
        binding = FragmentAddLessonBinding.inflate(inflater, container, false)
        binding.moduleName.text = db.moduleDao().getById(param1!!).module_name
        val all = db.lessonDao().getAll(param1!!) as ArrayList<Lesson>
        binding.add1.setOnClickListener {
            val name = binding.lessonName.text.toString()
            val desc = binding.lessonDesc.text.toString()
            val position = binding.lessonPosition.text.toString()
            if (name.isNotEmpty() && desc.isNotEmpty() && position.isNotEmpty()) {
                binding.lessonName.setText("")
                binding.lessonDesc.setText("")
                binding.lessonPosition.setText("")
                db.lessonDao().add(Lesson(name, desc, param1, position.toInt()))
                all.add(Lesson(name, desc, param1, position.toInt()))
                adapter.submitList(db.lessonDao().getAll(param1!!))
                binding.rv.adapter = adapter
            } else {
                Toast.makeText(requireContext(), "Malumotlarni to'ldiring", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        adapter = AddLessonAdapter(object : AddLessonAdapter.onItemCLick {
            override fun itemEditClick(lesson: Lesson, position: Int) {
                val bundle = Bundle()
                bundle.putInt("param1", lesson.lesson_id)
                findNavController().navigate(R.id.editLessonFragment, bundle)
            }

            override fun itemClick(lesson: Lesson, position: Int) {

            }

            override fun itemDeleteClick(lesson: Lesson, position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setMessage(
                    "Dars o’chishiga rozimisiz?"
                )

                dialog.setNegativeButton(
                    "Yo'q"
                ) { dialog, _ -> dialog?.cancel() }.setPositiveButton(
                    "Xa"
                ) { dialog, _ ->
                    db.lessonDao().delete(lesson)
                    adapter.notifyItemRemoved(position)
                    all.remove(lesson)
                    adapter.submitList(db.lessonDao().getAll(param1!!))
                    binding.rv.adapter = adapter
                    dialog?.dismiss()
                }
                dialog.show()
            }
        })
        adapter.submitList(db.lessonDao().getAll(param1!!))
        binding.rv.adapter = adapter
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddLessonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}