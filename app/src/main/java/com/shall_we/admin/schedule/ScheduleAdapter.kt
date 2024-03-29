package com.shall_we.admin.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R
import com.shall_we.admin.schedule.data.ScheduleData

class ScheduleAdapter(var scheduleList: List<ScheduleData>, private val onItemClicked: (Long, String, String) -> Unit) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>()  {

    class ScheduleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
    }

    fun updateData(newData: List<ScheduleData>) {
        this.scheduleList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ScheduleViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedules,parent,false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder : ScheduleViewHolder, position:Int){
        holder.title.text = scheduleList[position].subtitle
        holder.description.text = scheduleList[position].title


        // Set the click listener for the whole item view
        holder.itemView.setOnClickListener {
            onItemClicked(scheduleList[position].experienceGiftId, scheduleList[position].subtitle, scheduleList[position].title)
        }
    }

    override fun getItemCount() = scheduleList.size
}

