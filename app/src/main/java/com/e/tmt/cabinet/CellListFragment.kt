package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_cabinet_list.view.*
import kotlinx.android.synthetic.main.fragment_cell_list.view.*
import kotlin.coroutines.coroutineContext

class CellListFragment : Fragment() {
    private var activityCabinet: cabinet? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cell_list, container, false)

        activityCabinet?.buttonEffect(view.backButton2)
        activityCabinet?.buttonEffect(view.nextButton2)
        activityCabinet?.buttonEffect(view.addButton2)
        activityCabinet?.buttonEffect(view.deleteButton2)
        activityCabinet?.buttonEffect(view.editCellButton)

        view.backButton2.setOnClickListener {
            activityCabinet?.getStuff()
            activityCabinet?.goBack() }
        view.nextButton2.setOnClickListener {
            if(view.theCellName.text != ""){
                activityCabinet?.goItemList() }else{
                Toast.makeText(context, "공간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        view.addButton2.setOnClickListener { activityCabinet?.showAddCell() }
        view.deleteButton2.setOnClickListener { activityCabinet?.deleteCell() }
        view.editCellButton.setOnClickListener {
            activityCabinet?.showEditCell()
        }


        activityCabinet?.getCell()
        view.cellList.adapter = activityCabinet?.cellAdapter
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view.cellList.layoutManager = gridLayoutManager


        return view

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }

}