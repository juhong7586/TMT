package com.e.tmt.cabinet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import com.e.tmt.memo.ServerApi
import kotlinx.android.synthetic.main.activity_cabinet.*
import kotlinx.android.synthetic.main.fragment_dialog_custom.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class cabinet : AppCompatActivity() {
    lateinit var cabinetAdapter: CabinetAdapter
    lateinit var cellAdapter: CabinetAdapter
    lateinit var cabinetCardView: RecyclerView
    lateinit var cellCardView: RecyclerView
    private lateinit var alertDialog: AlertDialog
    //var cabinetAdapter = CabinetAdapter()

    var gridLayoutManager = GridLayoutManager(
        this@cabinet,
        2,
        GridLayoutManager.HORIZONTAL,
        false
    )



    val retrofit = Retrofit.Builder()
        .baseUrl(ServerApi.DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cabinetService = retrofit.create(CabinetService::class.java)

    var headerTitle: String = ""


    var selectedCabinet: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet)
        setMenu()
    }

    override fun onStart() {
        super.onStart()
    }



    fun setMenu() {
        val cabinetMainFragment = CabinetMainFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout2, cabinetMainFragment)
        transaction.commit()
    }

    fun setMinor(){
        mainContent.setBackgroundColor(Color.parseColor("#ffffff"))
    }

    fun goManageMenu(){

        val manageMenuFragment = ManageMenuFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.slide_in_1,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .replace(R.id.fragmentLayout2, manageMenuFragment)
            .addToBackStack("manage")
            .commit()
    }

    fun goItemList(){
        val itemListFragment = ItemListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.fragmentLayout2, itemListFragment)
            .addToBackStack("itemList")
            .commit()
    }

    fun goCabinetList(){
        mainContent.setBackgroundColor(Color.parseColor("#ffffff"))
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
        mainContent.setBackgroundColor(Color.parseColor("#ffffff"))
        val cellListFragment = CellListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.slide_in_1,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.fragmentLayout2, cellListFragment)
            .addToBackStack("cellList")
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
            .addToBackStack("itemList")
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
            var new = mDialogView.stuffTitle.text.toString()
            addStuff(new)
            mAlertDialog.dismiss()
        }
    }

    fun showAddCell(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.header.text = "공간 추가"
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            var new = mDialogView.stuffTitle.text.toString()
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
                if (!::cabinetAdapter.isInitialized){
                    var theList = response.body() as MutableList<Stuff>
                    var theList2 = mutableListOf<String>()
                    for (no in 0 until theList.size) {
                        theList2.add(theList[no].cabinetName)
                    }
                    theList2 = theList2.distinct() as MutableList<String>
                    cabinetAdapter = CabinetAdapter(theList2)
                    cabinetAdapter.stuffList.addAll(theList)
                }
                cabinetCardView = findViewById(R.id.cabinetList)
                cabinetCardView.adapter = cabinetAdapter
                Toast.makeText(baseContext, cabinetAdapter.selectedList.toString(), Toast.LENGTH_SHORT).show()
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

    fun getCabinet(){
        cabinetService.getCabinet().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                cabinetCardView = findViewById(R.id.cabinetList)
                cabinetAdapter = CabinetAdapter(response.body() as MutableList<String>)
                cabinetCardView.adapter = cabinetAdapter
                cabinetCardView.layoutManager = gridLayoutManager
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "문제가 생겼습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun deleteCabinet(){

        var cabinetN = cabinetAdapter.selectedOne
        var cabinetNo = cabinetAdapter.selectedNum
        var lastTotal = cabinetAdapter.itemCount
        cabinetService.deleteCabinet(cabinetN).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(
                    baseContext,
                    "사물함이 삭제되습니다.",
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

    fun addStuff(name: String){

        Toast.makeText(baseContext, name, Toast.LENGTH_SHORT).show()
        val total = cabinetAdapter.selectedList.size
        cabinetAdapter.selectedList.add(name)
        cabinetAdapter.notifyItemInserted(total)
        cabinetAdapter.notifyDataSetChanged()

    }



    fun getCell1() {
        if(::cabinetAdapter.isInitialized) {
            var example = cabinetAdapter.selecCabiList
            var cellSize = example.size

            var example2 = mutableListOf<String>()
            for (i in 0 until cellSize) {
                example2.add(example[i].cellName)
            }

            example2 = example2.distinct() as MutableList<String>
            Toast.makeText(baseContext, example2.toString(), Toast.LENGTH_SHORT).show()

            cellCardView = findViewById(R.id.cellList)
            if(!::cellAdapter.isInitialized) {
                cellAdapter = CabinetAdapter(example2)
                cellCardView.adapter = cellAdapter
                gridLayoutManager = GridLayoutManager(
                    this@cabinet,
                    2,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
                cellCardView.layoutManager = gridLayoutManager
            }
        }
    }

    fun getCell(){
        var cabinetN = cabinetAdapter.selectedOne
        cabinetService.getCell(cabinetN).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {

                if (!::cellAdapter.isInitialized) {
                    cellAdapter = CabinetAdapter(response.body() as MutableList<String>)
                }
                cellCardView = findViewById(R.id.cellList)
                cellCardView.adapter = cellAdapter
                Toast.makeText(baseContext, cellAdapter.selectedList.toString(), Toast.LENGTH_SHORT).show()
                val gridLayoutManager = GridLayoutManager(this@cabinet, 2, GridLayoutManager.HORIZONTAL, false)
                cellCardView.layoutManager = gridLayoutManager

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "$t",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


        //var ctList = cabinetAdapter.stuffList.filter{it.cabinetName = cabinetList}
        //var ctList = cabinetAdapter.selecCabiList.distinctBy{it.cellName}.toMutableList()

        //cellAdapter.cellList = cabinetAdapter.selecCabiList.distinctBy{it.cellName}.toMutableList()
        //Toast.makeText(
        //        baseContext,
        //        cellAdapter.cellList.toString(),
        //       Toast.LENGTH_LONG).show()

        //recycView2 = findViewById(R.id.cellList)
        //recycView2.adapter = stuffAdapter
        //val gridLayoutManager = GridLayoutManager(this@cabinet, 2, GridLayoutManager.HORIZONTAL, false)
        //recycView2.layoutManager = gridLayoutManager
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