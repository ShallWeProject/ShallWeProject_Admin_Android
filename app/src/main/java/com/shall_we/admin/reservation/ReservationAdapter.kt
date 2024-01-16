package com.shall_we.admin.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R
import com.shall_we.admin.reservation.data.ReservationData

class ReservationAdapter(private val reservationList : List<ReservationData>, private val onItemClicked: (ReservationData) -> Unit) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ReservationViewHolder(view)
    }

    inner class ReservationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.title.text = reservation.title
        holder.description.text = reservation.description

        // Set the click listener for the whole item view
        holder.itemView.setOnClickListener {
            onItemClicked(reservation)
        }
    }

    override fun getItemCount() = reservationList.size
}
