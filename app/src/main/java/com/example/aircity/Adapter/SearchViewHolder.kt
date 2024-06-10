package com.example.aircity.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aircity.R

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textNamebk = view.findViewById<TextView>(R.id.namebk)
    private val pmd1 = view.findViewById<TextView>(R.id.pmd1)
    private val pmd2 = view.findViewById<TextView>(R.id.pmd2)
    private val pmd3 = view.findViewById<TextView>(R.id.pmd3)
    private val pmd4 = view.findViewById<TextView>(R.id.pmd4)
    private val pmd5 = view.findViewById<TextView>(R.id.pmd5)
    private val pmd6 = view.findViewById<TextView>(R.id.pmd6)
    private val pmd7 = view.findViewById<TextView>(R.id.pmd7)
    private val pmq1 = view.findViewById<TextView>(R.id.pmq1)
    private val pmq2 = view.findViewById<TextView>(R.id.pmq2)
    private val pmq3 = view.findViewById<TextView>(R.id.pmq3)
    private val pmq4 = view.findViewById<TextView>(R.id.pmq4)
    private val pmq5 = view.findViewById<TextView>(R.id.pmq5)
    private val pmq6 = view.findViewById<TextView>(R.id.pmq6)
    private val pmq7 = view.findViewById<TextView>(R.id.pmq7)
    private val pm1 = view.findViewById<TextView>(R.id.pm1)
    private val pm2 = view.findViewById<TextView>(R.id.pm2)
    private val pm3 = view.findViewById<TextView>(R.id.pm3)
    private val pm4 = view.findViewById<TextView>(R.id.pm4)
    private val pm5 = view.findViewById<TextView>(R.id.pm5)
    private val pm6 = view.findViewById<TextView>(R.id.pm6)
    private val pm7 = view.findViewById<TextView>(R.id.pm7)
    private val p1 = view.findViewById<ImageView>(R.id.p1)
    private val p2 = view.findViewById<ImageView>(R.id.p2)
    private val p3 = view.findViewById<ImageView>(R.id.p3)
    private val p4 = view.findViewById<ImageView>(R.id.p4)
    private val p5 = view.findViewById<ImageView>(R.id.p5)
    private val p6 = view.findViewById<ImageView>(R.id.p6)
    private val p7 = view.findViewById<ImageView>(R.id.p7)

    private val context = view.context

    fun bind(SearchViewItem: SearchOnlineViewItem) {
        textNamebk.text = SearchViewItem.district
        pmd1.text = SearchViewItem.date_1
        pmd2.text = SearchViewItem.date_2
        pmd3.text = SearchViewItem.date_3
        pmd4.text = SearchViewItem.date_4
        pmd5.text = SearchViewItem.date_5
        pmd6.text = SearchViewItem.date_6
        pmd7.text = SearchViewItem.date_7
        pmq1.text = SearchViewItem.quality_1
        pmq2.text = SearchViewItem.quality_2
        pmq3.text = SearchViewItem.quality_3
        pmq4.text = SearchViewItem.quality_4
        pmq5.text = SearchViewItem.quality_5
        pmq6.text = SearchViewItem.quality_6
        pmq7.text = SearchViewItem.quality_7
        pm1.text = SearchViewItem.PM_1
        pm2.text = SearchViewItem.PM_2
        pm3.text = SearchViewItem.PM_3
        pm4.text = SearchViewItem.PM_4
        pm5.text = SearchViewItem.PM_5
        pm6.text = SearchViewItem.PM_6
        pm7.text = SearchViewItem.PM_7

        Glide.with(context).load(SearchViewItem.p1).into(p1)
        Glide.with(context).load(SearchViewItem.p2).into(p2)
        Glide.with(context).load(SearchViewItem.p3).into(p3)
        Glide.with(context).load(SearchViewItem.p4).into(p4)
        Glide.with(context).load(SearchViewItem.p5).into(p5)
        Glide.with(context).load(SearchViewItem.p6).into(p6)
        Glide.with(context).load(SearchViewItem.p7).into(p7)

    }
}