package com.e.tmt.cabinet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import kotlinx.android.synthetic.main.cabinet_recycler.view.*

class StuffAdapter(): RecyclerView.Adapter<StuffAdapter.stuffHolder>(){

    var stuffList = mutableListOf<Stuff>()
    inner class stuffHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cabinetText: TextView = itemView.cabinetName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): stuffHolder {
       val itemView = LayoutInflater.from(parent.context)
           .inflate(R.layout.cabinet_recycler, parent, false)
        return stuffHolder(itemView)
    }


    override fun getItemCount(): Int {
        return stuffList.size
    }
    override fun onBindViewHolder(holder: stuffHolder, position: Int) {
        val currentItem = stuffList[position]
        holder.cabinetText.text = currentItem.cabinetName
        //holder.cabinetCheck.isChecked = false

    }

}