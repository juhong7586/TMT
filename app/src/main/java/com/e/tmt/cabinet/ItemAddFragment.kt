package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_add_memo.view.*
import kotlinx.android.synthetic.main.fragment_item_add.view.*


class ItemAddFragment : Fragment() {
    var activityCabinet: cabinet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_item_add, container, false)
        activityCabinet?.buttonEffect(view.backButton3)
        activityCabinet?.buttonEffect(view.saveStuffButton)

        view.addCabinet.text = activityCabinet?.addCA
        view.addCell.text = activityCabinet?.addCE
        view.backButton3.setOnClickListener { activityCabinet?.goBack() }
        view.saveStuffButton.setOnClickListener {
            activityCabinet?.addIT = view.addItem.text.toString()
            activityCabinet?.addETC = view.addEtc.text.toString()

            activityCabinet?.postItem()
            activityCabinet?.goBack()
        }

        return view

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }


}