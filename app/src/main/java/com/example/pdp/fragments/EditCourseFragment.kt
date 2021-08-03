package com.example.pdp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.databinding.CameraBottomSheetBinding
import com.example.pdp.databinding.FragmentEditCourseBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Module
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream

private lateinit var binding: FragmentEditCourseBinding
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var db: AppDataBase.AppDatabase
private lateinit var image:ByteArray
class EditCourseFragment : Fragment() {
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
        db = AppDataBase.AppDatabase.getInstance(requireContext())
        binding = FragmentEditCourseBinding.inflate(inflater, container, false)
        val module = db.courseDao().getById(param1!!)
        image= module.image!!
        binding.et.setText(module.name)
        binding.tv.text = module.name
        binding.image.setImageBitmap(image?.size?.let { BitmapFactory.decodeByteArray(image, 0, it) })
        binding.image.setOnClickListener {

            val bsh = CameraBottomSheetBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
            val builder = BottomSheetDialog(requireContext())
            builder.setContentView(bsh.root)

            bsh.camera.setOnClickListener {
                getPichtureFromCamera()
                builder.dismiss()
            }
            bsh.folder.setOnClickListener {
                getPictureFromGalery()
                builder.dismiss()
            }
            builder.show()
        }
        binding.add.setOnClickListener {
            val name = binding.et.text.toString()
            module.name = name
            module.image= image
            db.courseDao().edit(module)
            findNavController().popBackStack()
        }
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditCourseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun getPichtureFromCamera() {

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                @SuppressLint("ResourceType")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 0)

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()

                }
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == -1) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.image.setImageBitmap(bitmap)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            image = byteArray
        } else if (requestCode == 1 && resultCode == -1) {
            val uri = data?.data!!
            var bitmap: Bitmap? = null
            bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            binding.image.setImageBitmap(bitmap)
            image = byteArray
        }
    }

    fun getPictureFromGalery() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    val REQUEST_CODE = 1
                    startActivityForResult(intent, REQUEST_CODE)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(requireContext(), "Permisson denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }
}