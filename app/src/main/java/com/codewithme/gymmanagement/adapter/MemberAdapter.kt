package com.codewithme.gymmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codewithme.gymmanagement.R
import com.codewithme.gymmanagement.model.Member

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
        var memberId: TextView = itemView.findViewById<TextView>(R.id.txtAdapterMID)
        var mobile: TextView = itemView.findViewById<TextView>(R.id.txtAdapterMobile)
        var expiry: TextView = itemView.findViewById<TextView>(R.id.txtExpiry)
        fun onBind(member: Member){
            name.text = member.name
            memberId.text = member.memberId
            mobile.text = member.mobileNum
            expiry.text = member.expiryData
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