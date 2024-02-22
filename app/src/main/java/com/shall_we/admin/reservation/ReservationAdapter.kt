package com.shall_we.admin.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R
import com.shall_we.admin.reservation.data.ReservationData

class ReservationAdapter(var reservationList : List<ReservationData>,private val onItemClicked: (Long, String, String)-> Unit) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
    }

    fun updateData(newData: List<ReservationData>) {
        this.reservationList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ReservationViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedules,parent,false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder : ReservationViewHolder, position:Int){
        holder.title.text = reservationList[position].description
        holder.description.text = reservationList[position].title

        // Set the click listener for the whole item view
        holder.itemView.setOnClickListener {
            onItemClicked(reservationList[position].experienceGiftId, reservationList[position].description, reservationList[position].title)
        }
    }

    override fun getItemCount() = reservationList.size
}
