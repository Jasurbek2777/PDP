package com.example.pdp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.adapters.AllModulAdapter
import com.example.pdp.databinding.FragmentAllModulsBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Module

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding_all_module: FragmentAllModulsBinding
private lateinit var db: AppDataBase.AppDatabase
private lateinit var adapter: AllModulAdapter

class AllModulsFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        var arrayList: ArrayList<Module>
        db = AppDataBase.AppDatabase.getInstance(requireContext())
        binding_all_module = FragmentAllModulsBinding.inflate(inflater, container, false)
        binding_all_module.moduleName.text= db.courseDao().getById(param1!!).name
        arrayList = db.moduleDao().getAll(param1!!) as ArrayList<Module>
        adapter =
            AllModulAdapter(requireContext(), arrayList, object : AllModulAdapter.onItemCLick {
                override fun itemClick(module: Module, position: Int) {
                    val bundle = Bundle()
                    bundle.putInt("param1", module.module_id!!)
                    findNavController().navigate(R.id.oneModulFragment, bundle)
                }

            })
        binding_all_module.rv.adapter = adapter
        return binding_all_module.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllModulsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}