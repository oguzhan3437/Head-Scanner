package com.oguzhancetin.goodpostureapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.data.model.Record

class RecordsAdapter(private var records:List<Record>, private val context: Context, private val onClick:(Record)->Unit) :
    BaseAdapter() {
    override fun getCount(): Int = records.size

    override fun getItem(p0: Int): Any = records[p0]

    override fun getItemId(p0: Int): Long = records[p0].id!!.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val record = (getItem(p0) as Record)
        val rowView = LayoutInflater.from(context).inflate(R.layout.card_record, p2, false)
        val rowCard = rowView.findViewById<CardView>(R.id.record_card)
        val rowText = rowView.findViewById<TextView>(R.id.textview_record_card_date)
        rowText.text = record.title
        rowCard.setOnClickListener { onClick.invoke(record) }

        return rowView
    }
    fun loadData(newRecords:List<Record>){
        records = newRecords
        notifyDataSetChanged()
    }
}