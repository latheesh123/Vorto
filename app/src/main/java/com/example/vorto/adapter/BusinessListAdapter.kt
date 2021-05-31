package com.example.vorto.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vorto.R
import com.example.vorto.model.Businesses
import com.squareup.picasso.Picasso

class BusinessListAdapter(
    private val list: ArrayList<Businesses>,
    private val selectedListItem: (Businesses) -> Unit
) :
    RecyclerView.Adapter<BusinessListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.business_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list.get(position).let { holder.bindView(it, selectedListItem, position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindView(item: Businesses, selectedListItem: (Businesses) -> Unit, position: Int) {
            itemView.apply {

                if (!item.image_url.isNullOrEmpty()) {
                    Picasso.get().load(item.image_url).fit()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(findViewById<AppCompatImageView>(R.id.business_image));
                } else {
                    findViewById<AppCompatImageView>(R.id.business_image).setImageResource(R.drawable.ic_launcher_foreground)
                }

                findViewById<AppCompatTextView>(R.id.business_rating).text =
                    "Rating : " + item.rating.toString();
                findViewById<AppCompatTextView>(R.id.business_name).text = item.name
                findViewById<AppCompatTextView>(R.id.business_open).text =
                    if (item.is_closed) "closed" else "open"
            }
            itemView.setOnClickListener { selectedListItem(item) }
        }

    }

}