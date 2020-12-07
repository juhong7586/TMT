package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R


class ItemAddFragment : Fragment() {

    var activityCabinet: cabinet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_item_add, container, false)
        return view

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }


}