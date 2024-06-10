package com.example.aircity.Adapter


import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esri.arcgisruntime.geometry.Point

import com.example.aircity.R

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
   // private val textquality = view.findViewById<TextView>(R.id.aqiq)
    private val aqi = view.findViewById<TextView>(R.id.aqin)
    private val pm = view.findViewById<TextView>(R.id.aqipm)
    private val aqibk = view.findViewById<TextView>(R.id.aqibk)
    private val aqiimg = view.findViewById<ImageView>(R.id.aimg)
    private val aqitime = view.findViewById<TextView>(R.id.aqitime)
    private val aqimask = view.findViewById<TextView>(R.id.aqi1)
    private val aqiout = view.findViewById<TextView>(R.id.aqi2)
    private val aqiin = view.findViewById<TextView>(R.id.aqi3)
    private val qaqi = view.findViewById<TextView>(R.id.aqiq)



   private val context = view.context

    fun bind(HomeViewItem: HomeOnlineViewItem) {
        //textquality.text = HomeViewItem.nameTH
        aqi.text = HomeViewItem.AQI
        pm.text = HomeViewItem.PM25
        aqibk.text = HomeViewItem.bk
     Glide.with(context).load(HomeViewItem.img).into(aqiimg)
        aqimask.text = HomeViewItem.mask
        aqiout.text = HomeViewItem.out
        aqiin.text = HomeViewItem.inp
        aqitime.text=HomeViewItem.time
        qaqi.text=HomeViewItem.qAqi


      // genShopPoint(HomeViewItem.lat.toDouble(),HomeViewItem.long.toDouble())

        //imageView.setImageResource(item.imageId)
    }




}
