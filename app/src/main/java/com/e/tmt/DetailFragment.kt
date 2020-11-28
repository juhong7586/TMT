package com.e.tmt

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*


class DetailFragment : Fragment() {
    var activityLamp:lamp? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_detail, container, false)

        activityLamp?.buttonEffect(view.backButton)
        activityLamp?.buttonEffect(view.editButton)

        view.backButton.setOnClickListener{ activityLamp?.goBack()}
        view.editButton.setOnClickListener{activityLamp?.goEdit()}
        var mMemo = activityLamp?.selectMemo()
        view.oneTextContent.text = mMemo!!.content
        view.oneTextTitle.text = mMemo!!.title
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityLamp = context as lamp
    }

}