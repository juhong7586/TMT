package com.e.tmt.cabinet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import kotlinx.android.synthetic.main.stuff_recycler.view.*
import java.util.*

class ItemAdapter(inputList: MutableList<Stuff>): RecyclerView.Adapter<ItemAdapter.ItemHolder>(),
    Filterable {
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
        val cell = selectedList[holder.adapterPosition]
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

                    val name = itemView.itemName.text
                    Toast.makeText(
                        itemView.context, "선택한 물건: $name",
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

    override fun getFilter( ): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults{
                val charSearch = constraint?.toString()?.toLowerCase()
                val filterResults = FilterResults()
                    selecCabiList.clear()
                    if(charSearch == null || charSearch.isEmpty()){
                        filterResults.values  =  stuffList
                        selecCabiList = stuffList
                    }else{
                        //for (stuff in stuffList){
                        //    var theName = stuff.itemName.toLowerCase()
                        for (theStuff in 0 until stuffList.size){
                            var sstuff = stuffList[theStuff]
                            var theName = stuffList[theStuff].itemName.toString().toLowerCase()
                            if(theName.contains(charSearch.toString(), ignoreCase = true) ){
                                selecCabiList.add(sstuff)
                            }
                        }
                        //stuffList.filter { it.itemName.contains(charSearch.toString(), ignoreCase = true) }
                    }

                //selecCabiList= stuffList.filter { it.itemName.contains(charSearch.toString(), ignoreCase = true) } as MutableList<Stuff>
                filterResults.values = selecCabiList
                //notifyDataSetChanged()
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                selectedList = selecCabiList
                notifyDataSetChanged()
                //selecCabiList.removeAll(selecCabiList)
            }
        }
    }
}