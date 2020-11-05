package com.e.tmt

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler.view.*
import java.util.*

import java.text.SimpleDateFormat as SimpleDateFormat


class CustomAdapter : RecyclerView.Adapter<Holder>(){

    var listData = mutableListOf<Memo>()

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
        val memo = listData.get(position)
        holder.setMemo(memo)

    }
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setMemo(memo: Memo){
        itemView.textNo.text = "${memo.id}"
        itemView.textTitle.text = memo.title

        var timeStamp = memo.datetime
        var oldStyle = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        oldStyle.setTimeZone(TimeZone.getTimeZone("KST"))
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

        var oldDate = oldStyle.parse(timeStamp)
        var formattedDate  = sdf.format(oldDate)
        itemView.textDate.text = formattedDate
    }

    var mMemo: Memo? = null
    init {
        var listener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
            if(isChecked) {
                mMemo.forEach { element ->
                    when (button.id) {
                        R.id.checkBox -> Log.d("CheckBox", "선택완료")
                    }
                }
            }
        }
        itemView.checkBox.setOnCheckedChangeListener (listener)
    }

}
