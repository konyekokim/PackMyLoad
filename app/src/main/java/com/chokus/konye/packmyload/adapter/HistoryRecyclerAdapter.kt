package com.chokus.konye.packmyload.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.interfaces.CustomItemClickListener
import com.chokus.konye.packmyload.models.HistoryClass

/**
 * Created by omen on 12/04/2018.
 */
class HistoryRecyclerAdapter(var historyList : ArrayList<HistoryClass>, context : Context) : RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder>(){
    var mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history : HistoryClass = historyList[position]
        holder.historyTypeTextView!!.text = history.pmlHistoryType
        holder.historyDateTextView!!.text = history.pmlHistoryDate
        holder.setOnCustomItemClickListener(object : CustomItemClickListener{
            override fun onCustomItemClickListener(view: View, position: Int) {
                // do function here.. like create dialog to display the history details
            }
        })
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var historyTypeTextView : TextView?= null
        var historyDateTextView : TextView? = null
        var customItemClickListener : CustomItemClickListener? = null
        init {
            historyTypeTextView = itemView.findViewById(R.id.pml_type_textView)
            historyDateTextView = itemView.findViewById(R.id.pml_type_date_textView)
            itemView.setOnClickListener(this)
        }
        fun setOnCustomItemClickListener(itemClickListener : CustomItemClickListener){
            this.customItemClickListener = itemClickListener
        }
        override fun onClick(v: View?) {
            this.customItemClickListener!!.onCustomItemClickListener(v!!,adapterPosition)
        }

    }
}
