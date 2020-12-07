package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import com.e.tmt.memo.lamp
import kotlinx.android.synthetic.main.fragment_manage_menu.view.*

class ManageMenuFragment : Fragment() {
    var activityCabinet: cabinet? = null
    var activityLamp: lamp? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_manage_menu, container, false)

        activityLamp?.buttonEffect2(view.manage1)
        activityLamp?.buttonEffect2(view.manage2)
        activityLamp?.buttonEffect2(view.manage3)


        view.manage1.setOnClickListener {
            //activityCabinet?.getCabinet()
            activityCabinet?.goCabinetList()

        }
        view.manage2.setOnClickListener { activityCabinet?.goCellList() }
        view.manage3.setOnClickListener { activityCabinet?.goAddItem() }
        return view

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }
}