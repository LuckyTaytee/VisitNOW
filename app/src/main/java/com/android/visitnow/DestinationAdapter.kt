package com.android.visitnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.visitnow.DataModel.DatabaseModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.destination_item.view.*

class DestinationAdapter(val list: ArrayList<DatabaseModel>) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.destination_item,
            parent, false)

        return DestinationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        when (holder) {

            is DestinationViewHolder -> {
                holder.bind(list.get(position))
            }
        }
    }

    override fun getItemCount() = list.size

    class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.item_image_view
        val textView1: TextView = itemView.item_text_view_1
        val textView2: TextView = itemView.item_text_view_2

        fun bind(databaseModel: DatabaseModel) {

            textView1.setText(databaseModel.spot)
            textView2.setText(databaseModel.price)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_search)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(databaseModel.img)
                .into(imageView)
        }
    }
}