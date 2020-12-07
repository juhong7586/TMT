package com.e.tmt.memo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.tmt.R
import kotlinx.android.synthetic.main.fragment_edit.view.*


class EditFragment : Fragment() {
    var activityLamp: lamp? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        activityLamp?.buttonEffect(view.editSaveButton)
        activityLamp?.buttonEffect(view.editBackButton)

        view.editSaveButton.setOnClickListener{
            activityLamp?.addMT = view.editTitle.text.toString()
            activityLamp?.addME = view.editEditor.text.toString()
            activityLamp?.addMC = view.editContent.text.toString()

            activityLamp?.putMemo()
            activityLamp?.goBack()
        }

        view.editBackButton.setOnClickListener{activityLamp?.goBack()}
        val editingOne = activityLamp?.selectMemo()
        view.editEditor.setText(editingOne!!.editor)
        view.editTitle.setText(editingOne.title)
        view.editContent.setText(editingOne.content)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityLamp = context as lamp
    }

}



