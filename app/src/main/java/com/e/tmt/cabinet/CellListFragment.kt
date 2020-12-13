package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_cell_list.view.*

class CellListFragment : Fragment() {
    private var activityCabinet: cabinet? = null

    override fun onResume() {
        //activityCabinet?.getCell()
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cell_list, container, false)

        activityCabinet?.buttonEffect(view.backButton2)
        activityCabinet?.buttonEffect(view.nextButton2)
        activityCabinet?.buttonEffect(view.addButton2)

        view.backButton2.setOnClickListener {
            activityCabinet?.getStuff()
            activityCabinet?.goBack() }
        view.nextButton2.setOnClickListener {
            activityCabinet?.goItemList()
        }
        view.addButton2.setOnClickListener { activityCabinet?.showAddCell() }


        activityCabinet?.getCell()
        view.cellList.adapter = activityCabinet?.cellAdapter
        var gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view.cellList.layoutManager = gridLayoutManager


        return view

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }

}