package com.e.tmt.cabinet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import com.e.tmt.memo.lamp
import kotlinx.android.synthetic.main.cabinet_recycler.view.*
import kotlinx.android.synthetic.main.cabinet_recycler.view.cabinetName1
import kotlinx.android.synthetic.main.cabinet_recycler.view.itemCard
import kotlinx.android.synthetic.main.cabinet_recycler2.view.*
import kotlinx.android.synthetic.main.stuff_recycler.view.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class CabinetAdapter(inputList: MutableList<String>, var inputType: String, var itemClickHandler: (Int, String) -> Unit): RecyclerView.Adapter<CabinetAdapter.StuffHolder>(){
    var stuffList = mutableListOf<Stuff>()
    var selectedList = inputList
    var selecCabiList = mutableListOf<Stuff>()

    var selectedNum: Int? = null
    var selectedOne: String = ""
    var selectedId: Int? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffHolder {
        var itemView: Any

        if (inputType == "item"){
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cabinet_recycler2, parent, false)
        }else{
            itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cabinet_recycler, parent, false)
        }
        return StuffHolder(itemView)
    }


    override fun getItemCount(): Int {
        return selectedList.size
    }
    override fun onBindViewHolder(holder: StuffHolder, position: Int) {
        var cabinet = selectedList[holder.adapterPosition]
        holder.setCabinet(cabinet)


    }

    inner class StuffHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val cabinetText: TextView = itemView.cabinetName
        //fun setStuff(stuff: Stuff){
        //    itemView.cabinetName.text = stuff.cabinetName
        //}
        fun setCabinet(cabinetText: String){


            if(inputType == "item"){
                    itemView.cabinetName.text = cabinetText
                    itemView.etc.text = stuffList[adapterPosition].etc

            }else{itemView.cabinetName1.text = cabinetText}
        }

        init{
           itemView.itemCard.setOnClickListener {
               if (inputType == "cabinet") {
                   selectedNum = adapterPosition
                   selectedOne = selectedList[selectedNum!!]
                   (stuffList.filter { it.cabinetName == selectedOne } as MutableList<Stuff>).also {
                       selecCabiList = it
                       itemClickHandler.invoke(adapterPosition, "cabinet")
                   }
               }
               else if (inputType == "cell") {
                   selectedNum = adapterPosition
                   selectedOne = selectedList[selectedNum!!]
                   (stuffList.filter { it.cellName == selectedOne } as MutableList<Stuff>).also {
                       selecCabiList = it
                       itemClickHandler.invoke(adapterPosition, "cell")
                   }
               }
               else if (inputType == "item") {
                   selectedNum = adapterPosition
                   selectedOne = selectedList[selectedNum!!]
                   (stuffList.filter { it.itemName == selectedOne } as MutableList<Stuff>).also {
                       selecCabiList = it
                       itemClickHandler.invoke(adapterPosition, "item")
                   }
               }


               notifyDataSetChanged()

            }
        }
    }


}