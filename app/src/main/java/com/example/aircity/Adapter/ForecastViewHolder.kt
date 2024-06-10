package com.example.aircity.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aircity.R

class ForecastViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.aairp)
    private val textView1 = view.findViewById<TextView>(R.id.adate)
    private val textView2 = view.findViewById<TextView>(R.id.aair)
    private val textView3 = view.findViewById<TextView>(R.id.atemp)
    private val textView4 = view.findViewById<TextView>(R.id.arain)

    private val context = view.context

    fun bind(ForecastViewItem: ForecastOnlineViewItem) {
        Glide.with(context).load(ForecastViewItem.aairp).into(imageView)
        textView1.text = ForecastViewItem.adate
        textView2.text = ForecastViewItem.aair
        textView3.text = ForecastViewItem.atemp
        textView4.text = ForecastViewItem.arain

    }
}