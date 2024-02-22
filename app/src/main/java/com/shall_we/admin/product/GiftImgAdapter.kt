package com.shall_we.admin.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shall_we.admin.R

class GiftImgAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var datas : List<String>? = null

    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gift_img, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = datas?.get(position)

        // 나머지 아이템은 실제 데이터의 이미지 로드
        Glide.with(context)
            .load(data)
            .into(holder.itemView.findViewById<ImageView>(R.id.iv_img))

        holder.itemView.findViewById<TextView>(R.id.tv_del).setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = itemView.findViewById(R.id.iv_img)
        val delBtn: TextView = itemView.findViewById(R.id.tv_del)
        init {
            // itemView를 클릭했을 때 해당 항목의 ProductData를 클릭 리스너를 통해 전달
            delBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position)
                }
            }
        }
    }
}
