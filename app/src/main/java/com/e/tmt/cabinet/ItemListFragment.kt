package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_item_list.view.*

class ItemListFragment : Fragment() {
    private var activityCabinet: cabinet? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        activityCabinet?.buttonEffect(view.backButton5)
        activityCabinet?.buttonEffect(view.deleteButton5)
        activityCabinet?.buttonEffect(view.addButton5)
        activityCabinet?.buttonEffect(view.editItemButton)

        view.backButton5.setOnClickListener { activityCabinet?.goBack() }
        view.deleteButton5.setOnClickListener { activityCabinet?.deleteItem() }
        view.addButton5.setOnClickListener { activityCabinet?.goAddItem() }
        view.editItemButton.setOnClickListener {
            activityCabinet?.goEditItem()
           //activityCabinet?.goEditPreset()
        }

        activityCabinet?.getItems()
        view.itemList.adapter = activityCabinet?.itemAdapter
        val staggeredGridLayoutManager =  StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        view.itemList.layoutManager = staggeredGridLayoutManager


        return view

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }

}