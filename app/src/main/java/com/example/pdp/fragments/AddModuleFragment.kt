package com.example.pdp.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.adapters.AddModulAdapter
import com.example.pdp.databinding.FragmentAddModuleBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Module

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding_add_module: FragmentAddModuleBinding
private lateinit var db_add: AppDataBase.AppDatabase
private lateinit var list: ArrayList<Module>
private lateinit var adapter: AddModulAdapter

class AddModuleFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null

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
        list = ArrayList()
        db_add = AppDataBase.AppDatabase.getInstance(requireContext())
        binding_add_module = FragmentAddModuleBinding.inflate(inflater, container, false)
        binding_add_module.add.setOnClickListener {
            val name = binding_add_module.name.text.toString()
            val place = binding_add_module.place.text.toString()
            if (name.isNotEmpty() && place.isNotEmpty()) {
                binding_add_module.name.setText("")
                binding_add_module.place.setText("")
                db_add.moduleDao().add(Module(name, param1, place.toInt()))
                list.add(Module(name, param1, place.toInt()))
                adapter.submitList(db_add.moduleDao().getAll(param1!!))
                binding_add_module.rv.adapter = adapter
            } else {
                Toast.makeText(requireContext(), "Malumotlarni kiriting", Toast.LENGTH_SHORT).show()
            }
        }
        binding_add_module.tv11.text = db_add.courseDao().getById(param1!!).name
        list = db_add.moduleDao().getAll(param1!!) as ArrayList<Module>

        adapter = AddModulAdapter(object : AddModulAdapter.onItemCLick {
            override fun itemEditClick(module: Module, position: Int) {
                val bundle = Bundle()
                bundle.putInt("param1", module.module_id)
                findNavController().navigate(R.id.editModuleFragment, bundle)
            }

            override fun itemClick(module: Module, position: Int) {
                val bundle = Bundle()
                bundle.putInt("param1", module.module_id)
                findNavController().navigate(R.id.addLessonFragment, bundle)
            }

            override fun itemDeleteClick(module: Module, position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setMessage(
                    "Bu modul ichida darslar kiritilgan. Darslar bilan birgalikda o’chib ketishiga rozimisiz?"
                )

                dialog.setNegativeButton(
                    "Yo'q"
                ) { dialog, _ -> dialog?.cancel() }.setPositiveButton(
                    "Xa"
                ) { dialog, _ ->
                    db_add.moduleDao().delete(module)
                    list.remove(module)
                    adapter.submitList(db_add.moduleDao().getAll(param1!!))
                    binding_add_module.rv.adapter = adapter

                    dialog?.dismiss()
                }
                dialog.show()

            }

        })
        adapter.submitList(db_add.moduleDao().getAll(param1!!))
        binding_add_module.rv.adapter = adapter

        return binding_add_module.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddModuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}