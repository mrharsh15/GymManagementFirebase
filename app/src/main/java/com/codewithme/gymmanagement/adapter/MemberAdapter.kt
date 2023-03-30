package com.codewithme.gymmanagement.adapter

import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.codewithme.gymmanagement.R
import com.codewithme.gymmanagement.fragment.FragmentAddMember
import com.codewithme.gymmanagement.model.Member
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MemberAdapter(private var memberList: ArrayList<Member>):RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    private lateinit var mListener: onClickListener

    interface onClickListener{
        fun onClick(position: Int)
    }

    fun setOnClickListener(listener: onClickListener){
        mListener = listener
    }

    class MemberViewHolder(itemView: View, private val listener: onClickListener): RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.findViewById<TextView>(R.id.txtAdapterName)
        private var memberId: TextView = itemView.findViewById<TextView>(R.id.txtAdapterMID)
        private var mobile: TextView = itemView.findViewById<TextView>(R.id.txtAdapterMobile)
        private var expiry: TextView = itemView.findViewById<TextView>(R.id.txtExpiry)
        private var activeStat: TextView = itemView.findViewById(R.id.status)
        private var gender: TextView = itemView.findViewById(R.id.txtAdapterGender)
        private var image: ImageView = itemView.findViewById(R.id.imgAdapterPic)
        fun onBind(member: Member){
            name.text = member.name
            memberId.text = member.memberId
            mobile.text = member.mobileNum
            expiry.text = member.expiryData
            gender.text = member.gender
            if(member.gender == "Male"){
                image.setImageResource(R.drawable.boy)
            }
            else{
                image.setImageResource(R.drawable.girl)
            }
            val myFormat = "dd/MM/yyyy"
            val expDate = SimpleDateFormat(myFormat).parse(member.expiryData)!!
            if(expDate.before(Date())) {
                activeStat.text = "Inactive"
                activeStat.setTextColor(Color.RED)
            }
            else{
                activeStat.text = "Active"
                activeStat.setTextColor(Color.GREEN)
            }
            itemView.setOnClickListener(){
                listener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.member_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.onBind(memberList[position])
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}