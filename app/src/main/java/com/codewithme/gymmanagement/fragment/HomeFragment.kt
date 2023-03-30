package com.codewithme.gymmanagement.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codewithme.gymmanagement.R
import com.codewithme.gymmanagement.adapter.MemberAdapter
import com.codewithme.gymmanagement.model.Member
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var memberList: ArrayList<Member>
    private lateinit var radioGroup: RadioGroup
    private lateinit var search: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var fab: ImageView
    private val bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("user").child(mAuth.currentUser!!.uid).child("member")
        memberList = ArrayList()

        var count = 0
        val totalMembers = view.findViewById<TextView>(R.id.memberCount)

        radioGroup = view.findViewById(R.id.rdGroupMember)
        fab = view.findViewById(R.id.imgAddMember)
        fab.setOnClickListener{
            bundle.putBoolean("renew", false)
            makeScreen(FragmentAddMember())
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch)
        recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewSearch.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        val currentRad = radioGroup.checkedRadioButtonId
        if(currentRad == R.id.rdActiveMember){
            databaseReference.addValueEventListener(object: ValueEventListener{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    memberList.clear()
                    for(postSnapshot in snapshot.children){
                        count++
                        try{
                            val member = postSnapshot.getValue(Member::class.java)!!
                            val myFormat = "dd/MM/yyyy"
                            val expDate = SimpleDateFormat(myFormat).parse(member.expiryData)!!
                            if(!expDate.before(Date())) {
                                memberList.add(member)
                                Comparator<Member>{lhs, rhs -> (rhs.memberId.toInt()).compareTo(lhs.memberId.toInt())}
                                val adapter = MemberAdapter(memberList)
                                recyclerView.adapter = adapter
                                adapter.setOnClickListener(object: MemberAdapter.onClickListener{
                                    override fun onClick(position: Int) {
                                        bundle.putBoolean("renew", true)
                                        bundle.putSerializable("member", memberList[position])
                                        makeScreen(FragmentAddMember())
                                    }

                                })
                            }
                        }
                        catch (exp: DateTimeException){
                            Log.d("Error", exp.message.toString())
                        }
                    }
                    totalMembers.text = "Total Members: $count"
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rdActiveMember -> {
                    databaseReference.addValueEventListener(object: ValueEventListener{
                        @SuppressLint("SimpleDateFormat")
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onDataChange(snapshot: DataSnapshot) {
                            memberList.clear()
                            for(postSnapshot in snapshot.children){
                                try{
                                    val member = postSnapshot.getValue(Member::class.java)!!
                                    val myFormat = "dd/MM/yyyy"
                                    val expDate = SimpleDateFormat(myFormat).parse(member.expiryData)!!
                                    if(!expDate.before(Date())) {
                                        memberList.add(member)
                                        Comparator<Member>{lhs, rhs -> (rhs.memberId.toInt()).compareTo(lhs.memberId.toInt())}
                                        val adapter = MemberAdapter(memberList)
                                        recyclerView.adapter = adapter
                                        adapter.setOnClickListener(object: MemberAdapter.onClickListener{
                                            override fun onClick(position: Int) {
                                                bundle.putBoolean("renew", true)
                                                bundle.putSerializable("member", memberList[position])
                                                makeScreen(FragmentAddMember())
                                            }

                                        })
                                    }
                                }
                                catch (exp: DateTimeException){
                                    Log.d("Error", exp.message.toString())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                else -> {
                    databaseReference.addValueEventListener(object: ValueEventListener{
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onDataChange(snapshot: DataSnapshot) {
                            memberList.clear()
                            for(postSnapshot in snapshot.children){
                                try{
                                    val member = postSnapshot.getValue(Member::class.java)!!
                                    val myFormat = "dd/MM/yyyy"
                                    val expDate = SimpleDateFormat(myFormat).parse(member.expiryData)!!
                                    if(expDate.before(Date())) {
                                        memberList.add(member)
                                        Comparator<Member>{lhs, rhs -> (rhs.memberId.toInt()).compareTo(lhs.memberId.toInt())}
                                        val adapter = MemberAdapter(memberList)
                                        recyclerView.adapter = adapter
                                        adapter.setOnClickListener(object: MemberAdapter.onClickListener{
                                            override fun onClick(position: Int) {
                                                bundle.putBoolean("renew", true)
                                                bundle.putSerializable("member", memberList[position])
                                                makeScreen(FragmentAddMember())
                                            }

                                        })
                                    }
                                }
                                catch (exp: DateTimeException){
                                    Log.d("Error", exp.message.toString())
                                }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
        }

        search = view.findViewById(R.id.search)

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null && newText.isNotEmpty()){
                    searchMember(newText)
                }
                else{
                    recyclerView.visibility = View.VISIBLE
                    recyclerViewSearch.visibility = View.GONE
                }
                return false
            }

        })

        search.setOnCloseListener {
            recyclerViewSearch.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            false
        }


        return view
    }

    private fun searchMember(query: String){
        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                memberList.clear()
                for(postSnapshot in snapshot.children){
                    val member = postSnapshot.getValue(Member::class.java)!!
                    if(member.memberId.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) || member.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) || member.mobileNum.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))){
                        recyclerViewSearch.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        memberList.add(member)
                        val adapter = MemberAdapter(memberList)
                        recyclerViewSearch.adapter = adapter
                        adapter.setOnClickListener(object : MemberAdapter.onClickListener {
                            override fun onClick(position: Int) {
                                bundle.putSerializable("member", memberList[position])
                                bundle.putBoolean("renew", true)
                                makeScreen(FragmentAddMember())
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun makeScreen(fragment: Fragment){
        requireFragmentManager().beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            fragment.arguments = bundle
            addToBackStack("none")
            commit()
        }
    }
}