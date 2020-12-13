package com.e.tmt.cabinet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import kotlinx.android.synthetic.main.item_recycler.view.*
import kotlinx.android.synthetic.main.stuff_recycler.view.*

class ItemAdapter(private val inputList: MutableList<Stuff>): RecyclerView.Adapter<ItemAdapter.ItemHolder>(){
    var stuffList = mutableListOf<Stuff>()
    var cabinetList = mutableListOf<String>()
    var selectedList = inputList
    var selecCabiList = mutableListOf<Stuff>()

    var selectedNums = mutableListOf<Int>()
    var selectedNum: Int? = null
    var selectedOne: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stuff_recycler, parent, false)
        return ItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
    override fun onBindViewHolder(holder: ItemAdapter.ItemHolder, position: Int) {
        var cell = selectedList.get(holder.adapterPosition)
        holder.setItems(cell)
    }



    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setItems(item: Stuff){
            itemView.itemName.text = item.itemName
            itemView.checkBox4.isSelected = false
        }
        init {
            itemView.checkBox4.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {

                    var name = itemView.itemName.text
                    Toast.makeText(
                        itemView.context, "선택한 물건: ${name}",
                        Toast.LENGTH_SHORT
                    ).show()

                    selectedOne = name.toString()
                    //
                    //if(lastPointed  != null) {
                    selectedList[adapterPosition].selected = 1
                    //}
                } else {
                    selectedList[adapterPosition].selected = 0
                }
            }
        }
    }
}