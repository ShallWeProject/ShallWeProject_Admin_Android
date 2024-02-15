package com.shall_we.admin.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R
import com.shall_we.admin.reservation.data.ReservationListData

class ReservationListAdapter(_reservationList : List<ReservationListData>, private val onItemClicked: (ReservationListData) -> Unit) : RecyclerView.Adapter<ReservationListAdapter.ReservationListViewHolder>() {
    var reservationList : List<ReservationListData> = _reservationList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationListAdapter.ReservationListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return ReservationListViewHolder(view)
    }

    inner class ReservationListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val time = itemView.findViewById<TextView>(R.id.time)
        val payment=itemView.findViewById<TextView>(R.id.payment)
        val phonenumber=itemView.findViewById<TextView>(R.id.phonenumber)
        val person=itemView.findViewById<TextView>(R.id.person)
        val statusImageView = itemView.findViewById<ImageView>(R.id.reserved)
    }

    override fun onBindViewHolder(holder: ReservationListViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.title.text = reservation.name
        holder.time.text = reservation.time
        holder.payment.text= reservation.payment
        holder.person.text=reservation.person.toString()
        holder.phonenumber.text=reservation.phonenumber


        if (reservation.status == "WAITING") {
            holder.statusImageView.setImageResource(R.drawable.waiting) // 대기 중인 경우의 이미지
        } else {
            holder.statusImageView.setImageResource(R.drawable.reserved) // 그 외의 경우의 이미지
        }
        // Set the click listener for the whole item view
        holder.itemView.setOnClickListener {
            onItemClicked(reservation)
        }
    }

    override fun getItemCount() = reservationList.size



}