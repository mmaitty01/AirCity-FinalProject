package com.example.aircity.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aircity.R


class OnBoardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textDescr = view.findViewById<TextView>(R.id.tvOnboardingDescr)
    private val imageView = view.findViewById<ImageView>(R.id.ivOnboarding)

    fun bind(onBoardingViewItem: OnBoardingViewItem) {
        textDescr.text = onBoardingViewItem.description
        imageView.setImageResource(onBoardingViewItem.imageId)
    }
}