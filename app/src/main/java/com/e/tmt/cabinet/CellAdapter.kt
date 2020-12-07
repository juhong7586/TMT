package com.e.tmt.cabinet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import kotlinx.android.synthetic.main.stuff_recycler.view.*

class CellAdapter(): RecyclerView.Adapter<CellAdapter.CellHolder>(){

    var cellList = mutableListOf<Stuff>()
    var selecCellList = mutableListOf<Stuff>()
    var cellNameList = mutableListOf<String>()
    var selectedCell: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellAdapter.CellHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stuff_recycler, parent, false)
        return CellHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cellNameList.size
    }
    override fun onBindViewHolder(holder: CellAdapter.CellHolder, position: Int) {
        var cell = cellNameList.get(holder.adapterPosition)
        holder.setCell(cell)
    }



    inner class CellHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setCell(cell: String){
            itemView.cellName.text = cell
        }
        init {
            itemView.cellCard.setOnClickListener { v ->
                selectedCell = v!!.toString()
                Toast.makeText(
                    itemView.context,
                    selectedCell,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}