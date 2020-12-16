package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_item_edit.view.*

class ItemEditFragment : Fragment() {
    var activityCabinet: cabinet? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_edit, container, false)

        val keys = arguments?.getStringArray("keys")
        view.editStuffCabinet.setText(keys?.get(0))
        view.editStuffCell.setText(keys?.get(1))
        view.editStuffItem.setText(keys?.get(2))
        view.editStuffEtc.setText(keys?.get(3))

        activityCabinet?.buttonEffect(view.editBackButton)
        activityCabinet?.buttonEffect(view.editStuffButton)

        view.editBackButton.setOnClickListener { activityCabinet?.goBack() }
        view.editStuffButton.setOnClickListener {
            activityCabinet?.putItem() }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }
}