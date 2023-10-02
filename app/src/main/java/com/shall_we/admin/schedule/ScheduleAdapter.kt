package com.shall_we.admin.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R

class ScheduleAdapter(private val scheduleList : List<ScheduleData>, private val onItemClicked: (ScheduleData) -> Unit) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ScheduleViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule,parent,false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder : ScheduleViewHolder, position:Int){
        holder.title.text = scheduleList[position].title
        holder.description.text = scheduleList[position].description

        // Set the click listener for the whole item view
        holder.itemView.setOnClickListener {
            onItemClicked(scheduleList[position])
        }
    }

    override fun getItemCount() = scheduleList.size

}
