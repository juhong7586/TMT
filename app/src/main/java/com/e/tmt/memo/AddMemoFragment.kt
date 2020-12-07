package com.e.tmt.memo

import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_add_memo.view.*


class AddMemoFragment : Fragment() {
    var activityLamp: lamp? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_memo, container, false)
        activityLamp?.buttonEffect(view.backButton)
        activityLamp?.buttonEffect(view.saveButton)

        view.backButton.setOnClickListener{
            activityLamp?.goBack()

        }
        view.saveButton.setOnClickListener{
            activityLamp?.addMT = view.addMemoTitle.text.toString()
            activityLamp?.addME = view.addMemoEditor.text.toString()
            activityLamp?.addMC = view.addMemoContent.text.toString()

            activityLamp?.postMemo()
            activityLamp?.goBack()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityLamp = context as lamp
    }


}