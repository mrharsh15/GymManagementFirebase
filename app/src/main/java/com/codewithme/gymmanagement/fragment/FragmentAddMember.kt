package com.codewithme.gymmanagement.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codewithme.gymmanagement.R
import com.codewithme.gymmanagement.databinding.FragmentAddMemberBinding
import com.codewithme.gymmanagement.model.Member
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class FragmentAddMember : Fragment() {
    private lateinit var binding: FragmentAddMemberBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fname:EditText
    private lateinit var lname:EditText
    private lateinit var memberId:EditText
    private lateinit var age:EditText
    private lateinit var mobile:EditText
    private lateinit var amount:EditText
    private lateinit var joiningDate: EditText
    private lateinit var expiryDate: EditText
    private var gender = "Male"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        fname = binding.edtFirstName
        lname = binding.edtLastName
        memberId = binding.memberId
        age = binding.edtAge
        mobile = binding.edtMobile
        amount = binding.edtAmount
        joiningDate = binding.edtJoining
        expiryDate = binding.edtExpire
        mAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("member")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Add New Member"


        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener{ view1, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.edtJoining.setText(sdf.format(cal.time))

        }

        val cal2 = Calendar.getInstance()
        val dateSetListener2 = DatePickerDialog.OnDateSetListener{ view2, year2, monthOfYear2, dayOfMonth2 ->
            cal2.set(Calendar.YEAR, year2)
            cal2.set(Calendar.MONTH, monthOfYear2)
            cal2.set(Calendar.DAY_OF_MONTH, dayOfMonth2)

            val myFormat2 = "dd/MM/yyyy"
            val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
            binding.edtExpire.setText(sdf2.format(cal2.time))
//            binding.edtExpire.setText(sdf.format(cal.time))

        }





        binding.radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.rdMale -> {
                    gender = "Male"
                }
                R.id.rdFemale -> {
                    gender = "Female"
                }
            }
        }


        binding.btnAddMemberSave.setOnClickListener {
            val member: Member = Member(memberId.text.toString().trim(), fname.text.toString().trim()+" "+lname.text.toString().trim(), gender.toString(), mobile.text.toString().trim(), joiningDate.text.toString().trim(), expiryDate.text.toString().trim())
            databaseRef.child(mAuth.currentUser!!.uid+memberId.text.toString().trim()).setValue(member).addOnCompleteListener {
                if(it.isSuccessful){
                    loadScreen(HomeFragment())
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {

        }


        binding.imgPicDate.setOnClickListener {
            activity?.let { it1 -> DatePickerDialog(
                it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(
                    Calendar.MONTH
                ), cal.get(Calendar.DAY_OF_MONTH)
            ).show() }
        }

        binding.imgPicDateExpiry.setOnClickListener {
            activity?.let { it2 -> DatePickerDialog(
                it2, dateSetListener2, cal2.get(Calendar.YEAR), cal2.get(
                    Calendar.MONTH
                ), cal2.get(Calendar.DAY_OF_MONTH)
            ).show() }
        }



    }

    private fun loadScreen(fragment: Fragment){
        requireFragmentManager().beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
    }
    private fun validate():Boolean{
        if(binding.edtFirstName.text.toString().trim().isEmpty()){
//            show toast
            return false
        }else if(binding.edtMobile.text.toString().trim().isEmpty()){
//            show toast
            return false
        }
        return true
    }



}
