package com.e.tmt.memo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {
    var activityLamp: lamp? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        activityLamp?.buttonEffect2(view.addButton)
        activityLamp?.buttonEffect2(view.deleteButton)
        activityLamp?.buttonEffect2(view.lightOn)

        view.addButton.setOnClickListener{activityLamp?.goAdd()}
        view.deleteButton.setOnClickListener{ activityLamp?.deleteMemo()}
        view.lightOn.setOnClickListener{activityLamp?.sendMemo()}
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityLamp = context as lamp
    }
}