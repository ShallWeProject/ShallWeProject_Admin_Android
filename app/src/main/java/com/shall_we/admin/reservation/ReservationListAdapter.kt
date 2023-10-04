package com.shall_we.admin.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R

class ReservationListAdapter(private val reservationList : List<ReservationListData>, private val onItemClicked: (ReservationListData) -> Unit) : RecyclerView.Adapter<ReservationListAdapter.ReservationListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationListAdapter.ReservationListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return ReservationListViewHolder(view)
    }

    inner class ReservationListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val time = itemView.findViewById<TextView>(R.id.time)
        val phonenumber=itemView.findViewById<TextView>(R.id.phonenumber)
    }

    override fun onBindViewHolder(holder: ReservationListViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.title.text = reservation.title
        holder.time.text = reservation.time
        holder.phonenumber.text=reservation.phonenumber


        // Set the click listener for the whole item view
        holder.itemView.setOnClickListener {
            onItemClicked(reservation)
        }
    }

    override fun getItemCount() = reservationList.size



}