package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import com.e.tmt.memo.lamp
import kotlinx.android.synthetic.main.activity_cabinet.view.*
import kotlinx.android.synthetic.main.fragment_cabinet_main.view.*


class CabinetMainFragment : Fragment() {
    var activityCabinet: cabinet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cabinet_main, container, false)

        activityCabinet?.buttonEffect(view.findButton)
        activityCabinet?.buttonEffect(view.manageButton)

        view.findButton.setOnClickListener{ activityCabinet?.goFind()}
        view.manageButton.setOnClickListener{ activityCabinet?.goCabinetList() }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }
}