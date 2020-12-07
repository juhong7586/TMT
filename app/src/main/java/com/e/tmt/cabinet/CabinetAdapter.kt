package com.e.tmt.cabinet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import kotlinx.android.synthetic.main.cabinet_recycler.view.*

class CabinetAdapter(private val inputList: MutableList<String>): RecyclerView.Adapter<CabinetAdapter.StuffHolder>(){
    var stuffList = mutableListOf<Stuff>()
    var cabinetList = mutableListOf<String>()
    var selectedList = inputList
    var selecCabiList = mutableListOf<Stuff>()

    var selectedNums = mutableListOf<Int>()
    var selectedNum: Int? = null
    var selectedOne: String = ""

    var activityCabinet: cabinet? = null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cabinet_recycler, parent, false)
        return StuffHolder(itemView)
    }


    override fun getItemCount(): Int {
        return selectedList.size
        //return cabinetList.size
    }
    override fun onBindViewHolder(holder: StuffHolder, position: Int) {
        //var stuff = stuffList.get(holder.adapterPosition)
        var cabinet = selectedList.get(holder.adapterPosition)
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
        }

        init{

            itemView.cabinetCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked) {
                    stuffList[adapterPosition].selected = 1
                } else {
                    stuffList[adapterPosition].selected = 0
                }
            }

           itemView.itemCard.setOnClickListener { v ->

                selectedNum = adapterPosition
                selectedOne = selectedList[selectedNum!!]
                selecCabiList = stuffList.filter { it.cabinetName == selectedOne } as MutableList<Stuff>
                //v.itemCard.setCardBackgroundColor(0xFF7903DAC6.toInt())
                //Thread.sleep(1_000)
               //v.itemCard.setCardBackgroundColor(Color.parseColor("#ffffff"))

                //selecCabiList = stuffList.filter { it.cabinetName == selectedCabinet } as MutableList<Stuff>
               // Toast.makeText(itemView.context, selectedCabinet, Toast.LENGTH_SHORT).show()

            }
        }
    }


}