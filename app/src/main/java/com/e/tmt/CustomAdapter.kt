package com.e.tmt

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler.view.*
import java.text.SimpleDateFormat
import java.util.*


class CustomAdapter : RecyclerView.Adapter<CustomAdapter.Holder>() {


    var listData = mutableListOf<Memo>()
    var selected: Memo?= null
    var selectedNo = mutableListOf<Int>()
    var selectedIndex = mutableListOf<Int>()
    var detailNo: Int = 0
    var detailIndex: Int = 0
    var lastSelected: Int? = null
    var lastPointed: Int? = null
    var getOneTitle: String = ""
    var textNumber: Int = 0
    var oldStyle = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    var timeStamp: String = ""
    var oldDate: Date? = null
    var formattedDate: String = ""


    var activityLamp: lamp? = null






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler,
            parent,
            false
        )

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var memo = listData.get(holder.adapterPosition)

        holder.setMemo(memo)
        lastPointed = position

    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setMemo(memo: Memo) {
            itemView.checkBox.isChecked = false
            textNumber = adapterPosition + 1
            itemView.textNo.text = textNumber.toString()
            itemView.textTitle.text = memo.title
            memo.selected = false

            timeStamp = memo.datetime
            oldStyle.setTimeZone(TimeZone.getTimeZone("KST"))
            oldDate = oldStyle.parse(timeStamp)
            formattedDate = sdf.format(oldDate)
            itemView.textDate.text = formattedDate
        }


        init {


            itemView.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                //var tORf: Boolean? = null
                if (isChecked) {

                    var number = itemView.textNo.text.toString().toInt()
                    makeText(
                        itemView.context, "선택한 메모: ${itemView.textNo.text}번",
                        Toast.LENGTH_SHORT
                    ).show()

                    selectedNo.add(number)
                    //
                    //if(lastPointed  != null) {
                    listData[adapterPosition].selected = true
                    //}
                } else {
                    listData[adapterPosition].selected = false
                }
                lastSelected = lastPointed

            }

            itemView.textTitle.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    detailNo = adapterPosition
                    detailIndex = adapterPosition

                    var mMemo: Memo? = null
                    mMemo = listData[detailNo]

                    val activity = v!!.context as AppCompatActivity
                    val detailFragment = DetailFragment()
                    val transaction = activity.supportFragmentManager.beginTransaction()
                    transaction
                        .setCustomAnimations(
                            R.anim.fragment_open_enter,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fragment_fade_exit
                        )
                        .replace(R.id.fragmentLayout, detailFragment)
                        .addToBackStack("detail")
                        .commit()
                }

            })
        }
    }



}

