package com.e.tmt.cabinet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import com.e.tmt.memo.ServerApi
import kotlinx.android.synthetic.main.activity_cabinet.*
import kotlinx.android.synthetic.main.fragment_cabinet_list.*
import kotlinx.android.synthetic.main.fragment_dialog_custom.view.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class cabinet : AppCompatActivity() {
    lateinit var cabinetAdapter: CabinetAdapter
    lateinit var cellAdapter: CabinetAdapter
    lateinit var itemAdapter: CabinetAdapter
    lateinit var cabinetCardView: RecyclerView

    private val retrofit = Retrofit.Builder()
        .baseUrl(ServerApi.DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val cabinetService = retrofit.create(CabinetService::class.java)

    lateinit var allStuffs: MutableList<Stuff>
    lateinit var theCabinetStuffs: MutableList<Stuff>
    lateinit var theCellStuffs: MutableList<Stuff>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet)
        setMenu()
    }


    private fun setMenu() {
        val cabinetMainFragment = CabinetMainFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout2, cabinetMainFragment)
        transaction.commit()
    }

    fun goFind(){
        val intent = Intent(applicationContext, cabinetFindActivity::class.java)
        startActivity(intent)
    }

    fun goCabinetList(){

        val cabinetListFragment = CabinetListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.fragmentLayout2, cabinetListFragment)
            .addToBackStack("cabinetList")
            .commit()
    }

    fun goCellList(){
        val cellListFragment = CellListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.slide_from_right,
                R.anim.fragment_fade_exit
            )
            .add(R.id.fragmentLayout2, cellListFragment)
            .addToBackStack("cellList")
            .commit()
    }

    fun goItemList(){
        val itemListFragment = ItemListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.slide_from_right,
                R.anim.fragment_fade_exit
            )
            .add(R.id.fragmentLayout2, itemListFragment)
            .addToBackStack("itemList")
            .commit()
    }


    fun goAddItem(){
        val itemAddFragment = ItemAddFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.fragmentLayout2, itemAddFragment)
            .addToBackStack("addItem")
            .commit()
    }


    fun showAddCabinet(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
       mDialogView.header.text ="서랍장 추가"
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            val new = mDialogView.stuffTitle.text.toString()
            addStuff(new)
            mAlertDialog.dismiss()
        }
    }

    fun showAddCell(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        "공간 추가".also { mDialogView.header.text = it }
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            val new = mDialogView.stuffTitle.text.toString()
            addStuff(new)
            mAlertDialog.dismiss()
        }
    }

    fun goBack() {
        onBackPressed()
    }

    //서버와 데이터 통신
    fun getStuff(){
        cabinetService.getStuff().enqueue(object : Callback<List<Stuff>> {
            override fun onResponse(call: Call<List<Stuff>>, response: Response<List<Stuff>>) {
                val theList = response.body() as MutableList<Stuff>
                allStuffs = theList
                var theList2 = mutableListOf<String>()
                for (no in 0 until theList.size) {
                    theList2.add(theList[no].cabinetName)
                }
                theList2 = theList2.distinct() as MutableList<String>
                cabinetAdapter = CabinetAdapter(theList2, "cabinet")
                cabinetAdapter.stuffList.addAll(theList)
                cabinetAdapter.notifyDataSetChanged()
                cabinetCardView = findViewById(R.id.cabinetList)

                cabinetCardView.adapter = cabinetAdapter
                val gridLayoutManager = GridLayoutManager(this@cabinet, 2, GridLayoutManager.HORIZONTAL, false)
                cabinetCardView.layoutManager = gridLayoutManager
            }
            override fun onFailure(call: Call<List<Stuff>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "문제가 발생했습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }



    fun deleteCabinet(){

        val cabinetN = cabinetAdapter.selectedOne
        val cabinetNo = cabinetAdapter.selectedNum
        val lastTotal = cabinetAdapter.itemCount
        cabinetService.deleteCabinet(cabinetN).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(
                    baseContext,
                    "사물함이 삭제되었습니다.",
                    Toast.LENGTH_LONG
                ).show()
                if (cabinetNo != null) {
                    cabinetAdapter.selectedList.removeAt(cabinetNo)
                    cabinetAdapter.notifyItemRemoved(cabinetNo)
                    cabinetAdapter.notifyItemChanged(cabinetNo, lastTotal - cabinetNo)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "문제가 생겼습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun addStuff(name: String){

        Toast.makeText(baseContext, name, Toast.LENGTH_SHORT).show()
        val total = cabinetAdapter.selectedList.size
        cabinetAdapter.selectedList.add(name)
        cabinetAdapter.notifyItemInserted(total)
        cabinetAdapter.notifyDataSetChanged()

    }

    fun getCell() {
        if(::cabinetAdapter.isInitialized) {
            val example = cabinetAdapter.selecCabiList
            val cellSize = example.size
            var example2 = mutableListOf<String>()

            for (i in 0 until cellSize) {
                example2.add(example[i].cellName)
            }

            example2 = example2.distinct() as MutableList<String>
                cellAdapter = CabinetAdapter(example2, "cell")
                theCabinetStuffs = example
                cellAdapter.stuffList.addAll(example)
                cellAdapter.notifyDataSetChanged()
        }
    }


    fun getItems(){
        val selectedCell = cellAdapter.selectedOne
        if(::cellAdapter.isInitialized) {

            theCellStuffs = theCabinetStuffs.filter { it.cellName == selectedCell } as MutableList<Stuff>
            (cabinetAdapter.selecCabiList.filter { it.cellName == selectedCell } as MutableList<Stuff>).also { theCellStuffs = it }

            val theList2 = mutableListOf<String>()
            for (element in theCellStuffs) {
                theList2.add(element.itemName)
            }

            itemAdapter = CabinetAdapter(theList2, "item")
            itemAdapter.stuffList.addAll(theCellStuffs)
            itemAdapter.notifyDataSetChanged()

        }else{
            Toast.makeText(baseContext, "에러", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun buttonEffect(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(buttonClick)
                    //v.alpha to 0.5
                    v.background.setColorFilter(
                        Color.parseColor("#D7D7D7"),
                        PorterDuff.Mode.SRC_ATOP
                    )

                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
    }

    private val buttonClick = AlphaAnimation(1f, 0.8f)



}