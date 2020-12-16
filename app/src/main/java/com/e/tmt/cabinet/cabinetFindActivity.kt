package com.e.tmt.cabinet


import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.tmt.R
import com.e.tmt.memo.ServerApi
import kotlinx.android.synthetic.main.activity_cabinet_find.*
import kotlinx.android.synthetic.main.fragment_dialog_custom.view.*
import kotlinx.android.synthetic.main.fragment_dialog_item_send.view.*
import kotlinx.android.synthetic.main.stuff_recycler.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class cabinetFindActivity : AppCompatActivity() {

    lateinit var cabinetListAdapter: SpinnerAdapter
    lateinit var cellListAdapter: SpinnerAdapter
    lateinit var stuffListAdapter: ItemAdapter

    var allStuffs = mutableListOf<Stuff>()
    var cabinetList = mutableListOf<String>()
    var cellList = mutableListOf<String>()

    var selectedItems = mutableListOf<Stuff>()
    var sendingItems = mutableListOf<Stuff>()
    var selectedCabinet: String = ""
    var selectedCell: String = ""
    var allStuffNames = mutableListOf<String>()

    var findingOne = ""

    private lateinit var searchView: SearchView

    private val retrofit = Retrofit.Builder()
        .baseUrl(ServerApi.DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val cabinetService = retrofit.create(CabinetService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet_find)
        activateCabinet()
        buttonEffect(checkList)
        checkList.setOnClickListener {
            prepareItems()
        }
        searchView = findViewById(R.id.searchStuff)
        letsSearch()

    }

    fun letsSearch(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                //stuffListAdapter.selectedList = selectedItems
                //stuffListAdapter.notifyDataSetChanged()
                findingOne = query!!
                //stuffListAdapter.filter.filter(findingOne)
                stuffListAdapter.filter.filter(findingOne)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                findingOne = newText!!

                return true
            }
        })
    }


    fun activateCabinet() {

        cabinetList.add(" -선택하세요-   ▼")

        cabinetService.getStuff().enqueue(object : Callback<List<Stuff>> {

            override fun onResponse(call: Call<List<Stuff>>, response: Response<List<Stuff>>) {
                this@cabinetFindActivity.allStuffs = response.body() as MutableList<Stuff>

                selectedItems = allStuffs
                for (i in selectedItems.indices) {
                    cabinetList.add(selectedItems[i].cabinetName)
                    allStuffNames.add(selectedItems[i].itemName)
                }
                cabinetList = cabinetList.distinct() as MutableList<String>
                ArrayAdapter(
                    this@cabinetFindActivity,
                    android.R.layout.simple_list_item_1,
                    cabinetList
                ).also { cabinetListAdapter = it }

                spinnerCabinet.adapter = cabinetListAdapter

                stuffListAdapter = ItemAdapter(allStuffs)

                findingItems.adapter = stuffListAdapter
                stuffListAdapter.stuffList = allStuffs
                LinearLayoutManager(this@cabinetFindActivity).also {
                    findingItems.layoutManager = it
                }

                cellList.add(" -선택하세요-   ▼")
                ArrayAdapter(
                    this@cabinetFindActivity,
                    android.R.layout.simple_list_item_1,
                    cellList
                ).also { cellListAdapter = it }
                spinnerCell.adapter = cellListAdapter


                spinnerCabinet.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position != 0) {
                                selectedCabinet = cabinetList[position]
                                activateCell(selectedCabinet)
                            } else {
                                stuffListAdapter.selectedList = allStuffs
                                stuffListAdapter.notifyDataSetChanged()
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

            }

            override fun onFailure(call: Call<List<Stuff>>, t: Throwable) {
                Toast.makeText(baseContext, "문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        )
    }

    fun activateCell(theCabinet: String) {
        cellList.clear()
        cellList.add(" -선택하세요-   ▼")
        selectedItems = allStuffs.filter { it.cabinetName == theCabinet } as MutableList<Stuff>
        val everyCellList = selectedItems
        for (i in 0 until selectedItems.size) {
            cellList.add(selectedItems[i].cellName)
        }
        val onlyCellList = cellList.distinct()
        ArrayAdapter(
            this@cabinetFindActivity,
            android.R.layout.simple_list_item_1,
            onlyCellList
        ).also { cellListAdapter = it }
        spinnerCell.adapter = cellListAdapter

        stuffListAdapter.selectedList = selectedItems
        stuffListAdapter.notifyDataSetChanged()

        spinnerCell.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCell = cellList[position]
                selectedItems =
                    everyCellList.filter { it.cellName == selectedCell } as MutableList<Stuff>
                if (position != 0) {
                    stuffListAdapter.selectedList = selectedItems
                    stuffListAdapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    fun prepareItems(){
        val mDialogView = LayoutInflater.from(this@cabinetFindActivity).inflate(R.layout.fragment_dialog_item_send, null)
        val mBuilder = AlertDialog.Builder(this@cabinetFindActivity)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        var theNames = getSelectedNames().toString()
        theNames = theNames.substring(1, theNames.length-1) as String
        mDialogView.sendingItemList.text = theNames
        mDialogView.backButton6.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.toCabinet.setOnClickListener {
            sendToCabinet()
            mAlertDialog.dismiss()
        }
    }

    private var buttonClick = AlphaAnimation(1f, 0.4f)
    private var buttonBack = AlphaAnimation(0.4f, 1f)

    @SuppressLint("ClickableViewAccessibility")
    fun buttonEffect(button: View) {
        buttonClick.duration = 200
        buttonBack.duration = 200
        button.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(buttonClick)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.startAnimation(buttonBack)
                    v.invalidate()
                }
            }
            false
        }
    }

    private fun sendToCabinet() {
        val sendNo = getSelectedList()
        if (sendNo.isEmpty()) {
            Toast.makeText(
                baseContext,
                "표시할 물건을 선택해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this@cabinetFindActivity, sendNo.toString(), Toast.LENGTH_SHORT).show()
            val sendMemos = cabinetService.sendToCabinet(sendNo)
            sendMemos.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(
                            baseContext,
                            "빛이 나는 곳을 확인하세요!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "물건이 전송되지 않았습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "문제가 생겼습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }


    private fun getSelectedList(): MutableList<Int> {
        val selected = mutableListOf<Int>()
        sendingItems = stuffListAdapter.selectedList
        for (i in 0 until sendingItems.size) if (sendingItems[i].selected == 1) {
            selected.add(sendingItems[i].id)
        }
        return selected
    }

    private fun getSelectedNames(): MutableList<String>{
        val selected = mutableListOf<String>()
        sendingItems = stuffListAdapter.selectedList
        for (i in 0 until sendingItems.size) if (sendingItems[i].selected == 1) {
            selected.add(sendingItems[i].itemName)
        }
        return selected
    }

}