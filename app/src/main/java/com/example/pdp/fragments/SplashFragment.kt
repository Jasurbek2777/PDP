package com.example.pdp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.databinding.FragmentSplashBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var binding_splash: FragmentSplashBinding
private lateinit var handler: Handler
class SplashFragment : Fragment() {
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
    ): View? {
        binding_splash = FragmentSplashBinding.inflate(inflater, container, false)
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
        }, 2000)
        return binding_splash.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SplashFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}