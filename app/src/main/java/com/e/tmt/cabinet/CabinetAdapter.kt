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
import kotlinx.android.synthetic.main.stuff_recycler.view.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class CabinetAdapter(inputList: MutableList<String>, var inputType: String): RecyclerView.Adapter<CabinetAdapter.StuffHolder>(){
    var stuffList = mutableListOf<Stuff>()
    var selectedList = inputList
    var selecCabiList = mutableListOf<Stuff>()

    var selectedNum: Int? = null
    var selectedOne: String = ""


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
        //return cabinetList.size
    }
    override fun onBindViewHolder(holder: StuffHolder, position: Int) {
        //var stuff = stuffList.get(holder.adapterPosition)
        val cabinet = selectedList[holder.adapterPosition]
        holder.setCabinet(cabinet)
        //holder.cabinetCheck.isChecked = false

    }

    inner class StuffHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val cabinetText: TextView = itemView.cabinetName
        //fun setStuff(stuff: Stuff){
        //    itemView.cabinetName.text = stuff.cabinetName
        //}
        fun setCabinet(cabinetText: String){
            itemView.cabinetName.text = cabinetText

            if(inputType == "item"){
                if(stuffList[layoutPosition].etc != null) {
                    itemView.etc.text = stuffList[layoutPosition].etc
                }
            }
        }

        init{

           itemView.itemCard.setOnClickListener {
               if(this@CabinetAdapter.inputType == "cabinet") {
                   selectedNum = adapterPosition
                   selectedOne = selectedList[selectedNum!!]
                   (stuffList.filter { it.cabinetName == selectedOne } as MutableList<Stuff>).also {
                       selecCabiList = it
                   }

               }else if(this@CabinetAdapter.inputType == "cell"){
                   selectedNum = adapterPosition
                   selectedOne = selectedList[selectedNum!!]
                   (stuffList.filter { it.cellName == selectedOne } as MutableList<Stuff>).also {
                       selecCabiList = it
                   }

               }
               notifyDataSetChanged()
               Toast.makeText(itemView.context, selectedOne, Toast.LENGTH_SHORT).show()

            }
        }
    }


}