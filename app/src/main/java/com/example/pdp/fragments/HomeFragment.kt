package com.example.pdp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.adapters.HomeSupperRvAdapter
import com.example.pdp.databinding.FragmentHomeBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Course
import com.example.pdp.room.entity.Module

private lateinit var supeprRvAdapter: HomeSupperRvAdapter
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding_home: FragmentHomeBinding
private lateinit var db_home: AppDataBase.AppDatabase

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db_home = AppDataBase.AppDatabase.getInstance(requireContext())
        binding_home = FragmentHomeBinding.inflate(inflater, container, false)
        binding_home.settingBtn.setOnClickListener { findNavController().navigate(R.id.settingFragment) }
        supeprRvAdapter = HomeSupperRvAdapter(requireContext(),db_home.courseDao().getAll() as ArrayList<Course>,
            object : HomeSupperRvAdapter.itemOnClick {
                override fun itemALlCLick(id: Int) {
                    val bundle = Bundle()
                    bundle.putInt("param1", id)
                    findNavController().navigate(R.id.allModulsFragment,bundle)
                }

                override fun childClickCLick(module: Module) {
                    val bundle=Bundle()
                    bundle.putInt("param1",module.module_id!!)
                    findNavController().navigate(R.id.oneModulFragment,bundle)
                }

            })
        binding_home.rv.adapter = supeprRvAdapter
        return binding_home.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}