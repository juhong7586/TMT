package com.e.tmt.cabinet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_cabinet_list.*
import kotlinx.android.synthetic.main.fragment_cabinet_list.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*

class CabinetListFragment : Fragment() {
    var activityCabinet: cabinet? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        activityCabinet?.getStuff()
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cabinet_list, container, false)

        activityCabinet?.buttonEffect(view.addButton1)
        activityCabinet?.buttonEffect(view.backButton1)
        activityCabinet?.buttonEffect(view.deleteButton1)
        activityCabinet?.buttonEffect(view.nextButton1)
        activityCabinet?.buttonEffect(view.editCabinetButton)

        view.backButton1.setOnClickListener { activityCabinet?.goBack() }
        view.deleteButton1.setOnClickListener { activityCabinet?.deleteCabinet() }
        view.addButton1.setOnClickListener { activityCabinet?.showAddCabinet() }
        view.nextButton1.setOnClickListener {
            if(view.theCabinetName.text != ""){
                activityCabinet?.goCellList()
            }else{
                Toast.makeText(context, "서랍장을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }}

        view.editCabinetButton.setOnClickListener { activityCabinet?.showEditCabinet() }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCabinet = context as cabinet
    }


}