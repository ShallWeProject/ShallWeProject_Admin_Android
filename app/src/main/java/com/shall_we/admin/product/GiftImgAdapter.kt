package com.shall_we.admin.product

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shall_we.admin.R
import com.shall_we.admin.databinding.ItemGiftImgBinding
import java.io.File

class GiftImgAdapter(private val datas: MutableList<Uri>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGiftImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = datas?.get(position)

        if (data.toString().startsWith("/")) {
            Glide.with(holder.itemView.context)
                .load(Uri.fromFile(File(data!!.path)))
                .apply(RequestOptions.placeholderOf(R.drawable.splash_icon))
                .into(holder.itemView.findViewById<ImageView>(R.id.iv_img))
        } else {
            Glide.with(holder.itemView.context)
                .load(data)
                .apply(RequestOptions.placeholderOf(R.drawable.splash_icon))
                .into(holder.itemView.findViewById<ImageView>(R.id.iv_img))
        }

        holder.itemView.findViewById<TextView>(R.id.tv_del).setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    inner class ViewHolder(binding: ItemGiftImgBinding) : RecyclerView.ViewHolder(binding.root) {
        private fun removeImage(position: Int) {
            datas?.removeAt(position)
            notifyDataSetChanged()
        }
        init {
            // itemView를 클릭했을 때 해당 항목의 ProductData를 클릭 리스너를 통해 전달
            binding.tvDel.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    datas?.apply {
                        removeAt(position)
                        Log.d("remove", "remove")
                        notifyDataSetChanged() // RecyclerView 갱신
                    }
                }
            }
        }
    }
}
