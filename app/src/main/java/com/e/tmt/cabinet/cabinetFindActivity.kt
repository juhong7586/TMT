package com.e.tmt.cabinet

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import com.e.tmt.memo.ServerApi
import kotlinx.android.synthetic.main.activity_cabinet_find.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class cabinetFindActivity : AppCompatActivity() {

    lateinit var cabinetListAdapter: SpinnerAdapter
    lateinit var cellListAdapter: SpinnerAdapter
    lateinit var stuffListAdapter: ItemAdapter

    lateinit var cabinetCardView: RecyclerView

    var allStuffs = mutableListOf<Stuff>()
    var cabinetList = mutableListOf<String>()
    var cellList = mutableListOf<String>()

    var selectedItems = mutableListOf<Stuff>()
    var selectedCabinet: String = ""
    var selectedCell: String = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(ServerApi.DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val cabinetService = retrofit.create(CabinetService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet_find)
        activateCabinet()


    }

    fun activateCabinet(){

        cabinetList.add(" -선택하세요-   ▼")

        cabinetService.getStuff().enqueue(object : Callback<List<Stuff>> {

            override fun onResponse(call: Call<List<Stuff>>, response: Response<List<Stuff>>) {
                allStuffs = response.body() as MutableList<Stuff>
                selectedItems = allStuffs
                for (i in selectedItems.indices){
                    cabinetList.add(selectedItems[i].cabinetName)
                }
                cabinetList = cabinetList.distinct() as MutableList<String>
                cabinetListAdapter = ArrayAdapter<String>(this@cabinetFindActivity, android.R.layout.simple_list_item_1, cabinetList)
                spinnerCabinet.adapter = cabinetListAdapter

                stuffListAdapter = ItemAdapter(allStuffs)
                findingItems.adapter = stuffListAdapter
                findingItems.layoutManager = LinearLayoutManager(this@cabinetFindActivity)

                cellList.add(" -선택하세요-   ▼")
                cellListAdapter = ArrayAdapter<String>(this@cabinetFindActivity, android.R.layout.simple_list_item_1, cellList)
                spinnerCell.adapter = cellListAdapter


                spinnerCabinet.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if(position != 0) {
                            selectedCabinet = cabinetList[position]
                            activateCell(selectedCabinet)
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

    fun activateCell(theCabinet: String){
        cellList.clear()
        cellList.add(" -선택하세요-   ▼")
        selectedItems = allStuffs.filter{ it.cabinetName == theCabinet} as MutableList<Stuff>
        var everyCellList = selectedItems
        for (i in 0 until selectedItems.size){
            cellList.add(selectedItems[i].cellName)
        }
        val onlyCellList = cellList.distinct()
        cellListAdapter = ArrayAdapter<String>(this@cabinetFindActivity, android.R.layout.simple_list_item_1, onlyCellList)
        spinnerCell.adapter = cellListAdapter

        stuffListAdapter.selectedList = selectedItems
        stuffListAdapter.notifyDataSetChanged()

        spinnerCell.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCell = cellList[position]
                selectedItems = everyCellList.filter{it.cellName == selectedCell} as MutableList<Stuff>
                if(position != 0) {
                    stuffListAdapter.selectedList = selectedItems
                    stuffListAdapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    fun activateStuff(){
        stuffListAdapter = ItemAdapter(allStuffs)
        findingItems.adapter = stuffListAdapter
        findingItems.layoutManager = LinearLayoutManager(this@cabinetFindActivity)
    }
}