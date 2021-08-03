package com.example.pdp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdp.R
import com.example.pdp.adapters.SettingRvAdapter
import com.example.pdp.databinding.CameraBottomSheetBinding
import com.example.pdp.databinding.FragmentSettingBinding
import com.example.pdp.room.AppDataBase
import com.example.pdp.room.entity.Course
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding_setting: FragmentSettingBinding
private var signPicture = ByteArray(0)
private lateinit var db_setting: AppDataBase.AppDatabase
private lateinit var setting_rv_adapter: SettingRvAdapter

class SettingFragment : Fragment() {
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
        db_setting = AppDataBase.AppDatabase.getInstance(requireContext())
        val list = db_setting.courseDao().getAll() as ArrayList
        binding_setting = FragmentSettingBinding.inflate(inflater, container, false)
        binding_setting.image.setOnClickListener {

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
        binding_setting.btn.setOnClickListener {
            val name = binding_setting.et.text.toString()
            if (name.isNotEmpty() && signPicture.isNotEmpty()) {
                binding_setting.et.setText("")
                binding_setting.image.setImageResource(R.drawable.holder)
                list.add(Course(name, signPicture))
                db_setting.courseDao().add(Course(name, signPicture))
                setting_rv_adapter.notifyItemInserted(list.size)
                setting_rv_adapter.notifyItemRangeChanged(list.size - 1, list.size)
                signPicture = ByteArray(0)
                setting_rv_adapter.submitList(db_setting.courseDao().getAll())
                binding_setting.rv.adapter = setting_rv_adapter
            } else {
                Toast.makeText(requireContext(), "Malumotlarni kiriting", Toast.LENGTH_SHORT).show()
            }
        }
        setting_rv_adapter = SettingRvAdapter( object : SettingRvAdapter.onItemCLick {
            override fun itemEditClick(course: Course, position: Int) {
                val bundle = Bundle()
                bundle.putInt("param1", course.course_id)
                findNavController().navigate(R.id.editCourseFragment, bundle)
            }

            override fun itemClick(course: Course, position: Int) {
                val bundle = Bundle()
                bundle.putInt("param1", course.course_id)
                findNavController().navigate(R.id.addModuleFragment, bundle)
            }

            override fun itemDeleteClick(course: Course, position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setMessage(
                    "Bu kurs ichida modullar kiritilgan. Modullar bilan birgalikda o’chib ketishiga rozimisiz?"
                )

                dialog.setNegativeButton(
                    "Yo'q"
                ) { dialog, _ -> dialog?.cancel() }.setPositiveButton(
                    "Xa"
                ) { dialog, _ ->
                    db_setting.courseDao().delete(course)
                    setting_rv_adapter.submitList(db_setting.courseDao().getAll())
                    binding_setting.rv.adapter = setting_rv_adapter
                    dialog?.dismiss()
                }
                dialog.show()
                list.remove(course)


            }

        })
        setting_rv_adapter.submitList(db_setting.courseDao().getAll())
        binding_setting.rv.adapter = setting_rv_adapter
        return binding_setting.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
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
            binding_setting.image.setImageBitmap(bitmap)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            signPicture = byteArray
        } else if (requestCode == 1 && resultCode == -1) {
            val uri = data?.data!!
            var bitmap: Bitmap? = null
            bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            binding_setting.image.setImageBitmap(bitmap)
            signPicture = byteArray
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