package com.e.tmt.cabinet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.e.tmt.R


class ItemListFragment : Fragment() {
    var activityCabinet: cabinet? = null
    val types = arrayOf("simple User", "Admin")

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view =  inflater.inflate(R.layout.fragment_item_list, container, false)
        return view

    }
}