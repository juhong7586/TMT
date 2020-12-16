package com.e.tmt.cabinet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import com.e.tmt.memo.ServerApi
import kotlinx.android.synthetic.main.activity_cabinet.*
import kotlinx.android.synthetic.main.fragment_cabinet_list.*
import kotlinx.android.synthetic.main.fragment_dialog_custom.view.*
import kotlinx.android.synthetic.main.fragment_item_add.*
import kotlinx.android.synthetic.main.fragment_item_edit.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.stuff_recycler.*
import okhttp3.ResponseBody
import org.w3c.dom.Text
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

    var addCA: String = ""
    var addCE: String = ""
    var addIT: String = ""
    var addETC: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet)
        setMenu()
    }

    //초기 화면 설정
    private fun setMenu() {
        val cabinetMainFragment = CabinetMainFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout2, cabinetMainFragment)
        transaction.commit()
    }

    //물건 찾기로 이동
    fun goFind(){
        val intent = Intent(applicationContext, cabinetFindActivity::class.java)
        startActivity(intent)
    }

    //서랍장 관리로 이동
    fun goCabinetList(){

        val cabinetListFragment = CabinetListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            .add(R.id.fragmentLayout2, cabinetListFragment)
            .addToBackStack("cabinetList")
            .commit()
    }

    //공간 관리로 이동
    fun goCellList(){
        if(cabinetAdapter.selectedOne != "") {
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
        }else{
            Toast.makeText(this@cabinet, "사물함을 선택해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    //물건 관리로 이동
    fun goItemList(){

        if(cellAdapter.selectedOne != "") {
            addCA = cabinetAdapter.selectedOne
            addCE = cellAdapter.selectedOne

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
        }else{
            Toast.makeText(this@cabinet, "공간을 선택해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    //물건 추가로 이동
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
            .add(R.id.fragmentLayout2, itemAddFragment)
            .addToBackStack("addItem")
            .commit()
    }

    //사물함 추가로 이동
    fun showAddCabinet(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        "서랍장 추가".also { mDialogView.header.text = it }
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            var new = mDialogView.stuffTitle.text.toString()

            //addStuff(new)
            val total  = cabinetAdapter.selectedList.size
            cabinetAdapter.selectedList.add(new)
            cabinetAdapter.notifyItemInserted(total)
            cabinetAdapter.notifyDataSetChanged()

            mAlertDialog.dismiss()
        }
    }

    //공간 추가로 이동
    fun showAddCell(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        "공간 추가".also { mDialogView.header.text = it }
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            val new = mDialogView.stuffTitle.text.toString()
            //addStuff(new)
            val total = cellAdapter.selectedList.size
            cellAdapter.selectedList.add(new)
            cellAdapter.notifyDataSetChanged()
            //Toast.makeText(this@cabinet, total.toString(), Toast.LENGTH_SHORT).show()

            mAlertDialog.dismiss()
        }
    }



    //뒤로 가기
    fun goBack() {
        val theCabinet = findViewById<TextView>(R.id.theCabinetName)
        val theCell = findViewById<TextView>(R.id.theCellName)
        theCabinet.text = ""
        theCell.text = ""
        onBackPressed()
    }
    //서버와 데이터 통신
    //물건 추가
    fun postItem(){
        var addNewItem = cabinetService.postItem(addCA, addCE, addIT, addETC)
        addNewItem.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    getLastItem()

                    Toast.makeText(
                        baseContext,
                        "물건을 넣었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        baseContext,
                        "물건을 넣을 수 없습니다.",
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


    //서랍장 가져오기(전체 가져오기)
    fun getStuff(){
        cabinetService.getStuff().enqueue(object : Callback<List<Stuff>> {
            override fun onResponse(call: Call<List<Stuff>>, response: Response<List<Stuff>>) {
                if (response.code() == 200) {
                    val theList = response.body() as MutableList<Stuff>
                    allStuffs = theList
                    var theList2 = mutableListOf<String>()
                    for (no in 0 until theList.size) {
                        theList2.add(theList[no].cabinetName)
                    }
                    theList2 = theList2.distinct() as MutableList<String>
                    cabinetAdapter =
                        CabinetAdapter(theList2, "cabinet", this@cabinet::onItemClickHandler)
                    cabinetAdapter.stuffList.addAll(theList)
                    cabinetAdapter.notifyDataSetChanged()
                    cabinetCardView = findViewById(R.id.cabinetList)

                    cabinetCardView.adapter = cabinetAdapter
                    val gridLayoutManager =
                        GridLayoutManager(this@cabinet, 2, GridLayoutManager.HORIZONTAL, false)
                    cabinetCardView.layoutManager = gridLayoutManager
                } else {
                    Toast.makeText(baseContext, "문제가 발생했습니다.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<Stuff>>, t: Throwable) {
                Toast.makeText(baseContext, "문제가 발생했습니다.", Toast.LENGTH_LONG).show()
            }
        })
    }

    //공간 가져오기
    fun getCell() {
        if(::cabinetAdapter.isInitialized) {
            theCabinetStuffs = cabinetAdapter.selecCabiList
            var example2 = mutableListOf<String>()

            for (element in theCabinetStuffs) {
                example2.add(element.cellName)
            }
            if(example2.size != 0) {
                example2 = example2.distinct() as MutableList<String>
                cellAdapter = CabinetAdapter(example2, "cell", this@cabinet::onItemClickHandler)
                cellAdapter.stuffList.addAll(theCabinetStuffs)
                cellAdapter.notifyDataSetChanged()

                //Toast.makeText(this@cabinet, cellAdapter.selectedList.toString(), Toast.LENGTH_SHORT).show()
            }else{
                cellAdapter = CabinetAdapter(example2, "cell", this@cabinet::onItemClickHandler)
            }

        }else{

            Toast.makeText(baseContext, "에러", Toast.LENGTH_SHORT).show()
        }
    }

    //물건 가져오기
    fun getItems(){
        cellAdapter.selectedOne
        if(::cellAdapter.isInitialized) {

            theCellStuffs = cellAdapter.selecCabiList

            val theList2 = mutableListOf<String>()
            for (element in theCellStuffs) {
                theList2.add(element.itemName)
            }
            if(theCellStuffs.size != 0) {
                itemAdapter = CabinetAdapter(theList2, "item", this@cabinet::onItemClickHandler)
                itemAdapter.stuffList.addAll(theCellStuffs)
                itemAdapter.notifyDataSetChanged()
            }else{
                itemAdapter = CabinetAdapter(theList2, "item", this@cabinet::onItemClickHandler)
            }
        }else{
            Toast.makeText(baseContext, "에러", Toast.LENGTH_SHORT).show()
        }
    }



    //사물함 수정
    fun showEditCabinet(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.header.text ="서랍장 수정"
        mDialogView.stuffTitle.setText(cabinetAdapter.selectedOne)
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            val editedName = mDialogView.stuffTitle.text.toString()
            if (editedName != "") {

                cabinetAdapter.selectedOne = editedName
                cabinetAdapter.selectedList.set(cabPosition!!, editedName)
                cabinetAdapter.notifyItemChanged(cabPosition!!)
                cabinetAdapter.notifyDataSetChanged()

                putCabinet(editedName)
                mAlertDialog.dismiss()
                setName(R.id.theCabinetName, "")
                //Toast.makeText(this@cabinet, cabPosition.toString(), Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@cabinet, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun putCabinet(editedName: String) {
        val editedStuffs = cabinetAdapter.selecCabiList
        val editedIds = mutableListOf<Int>()
        val beforeName = cabinetAdapter.selectedOne

        for (element in editedStuffs){
            editedIds.add(element.id)
        }
        val editCabinet = cabinetService.putCabinet(editedName, editedIds)
        editCabinet.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                cabinetAdapter.stuffList.filter{it.cabinetName == beforeName}.forEach { it.cabinetName = editedName }
                cabinetAdapter.selecCabiList.forEach { it.cabinetName = editedName }
                cabinetAdapter.notifyDataSetChanged()

                Toast.makeText(this@cabinet, "사물함 이름을 변경했습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@cabinet, "문제가 생겼습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }



    //공간 수정
    fun showEditCell(){
        val mDialogView = LayoutInflater.from(this@cabinet).inflate(R.layout.fragment_dialog_custom, null)
        val mBuilder = AlertDialog.Builder(this@cabinet)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.header.text ="공간 수정"
        mDialogView.stuffTitle.setText(cellAdapter.selectedOne)
        mDialogView.backButton3.setOnClickListener { mAlertDialog.dismiss() }
        mDialogView.saveButton2.setOnClickListener {
            val editedName = mDialogView.stuffTitle.text.toString()
            if (editedName.isNotEmpty()) {
                val editedPosition = cellAdapter.selectedNum!!

                //Toast.makeText(this@cabinet, editedPosition.toString(), Toast.LENGTH_SHORT).show()
                putCell(editedName)

                cellAdapter.selectedOne = editedName
                cellAdapter.selectedList[editedPosition] =editedName
                cellAdapter.notifyItemChanged(editedPosition)
                cellAdapter.notifyDataSetChanged()


                mAlertDialog.dismiss()
                setName(R.id.theCellName, "")


            }else{
                Toast.makeText(this@cabinet, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun putCell(editedName: String) {
        val editedStuffs = cellAdapter.selecCabiList
        val editedIds = mutableListOf<Int>()
        val beforeName = cellAdapter.selectedOne
        val cabinetName = cabinetAdapter.selectedOne

        for (element in editedStuffs){
            editedIds.add(element.id)
        }
        val editCell = cabinetService.putCell(editedName, editedIds)
        editCell.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                cabinetAdapter.stuffList.filter{it.cabinetName == cabinetName && it.cellName == beforeName}.forEach{ it.cellName = editedName }
                cabinetAdapter.selecCabiList.filter{it.cellName == beforeName}.forEach{it.cellName = editedName}

                cellAdapter.stuffList.filter{it.cellName == beforeName}.forEach { it.cellName = editedName }
                cellAdapter.selecCabiList.forEach { it.cellName = editedName }
                cabinetAdapter.notifyDataSetChanged()
                cellAdapter.notifyDataSetChanged()
                Toast.makeText(this@cabinet, "공간 이름을 변경했습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@cabinet, "문제가 생겼습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //물건 수정으로 이동
    fun goEditItem(){

        val itemEditFragment = ItemEditFragment()
        val bundle = Bundle()
        bundle.putStringArray("keys", arrayOf(cabinetAdapter.selectedOne, cellAdapter.selectedOne, itemAdapter.selectedOne, itemAdapter.selecCabiList[0].etc))

        itemEditFragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .add(R.id.fragmentLayout2, itemEditFragment)
            .addToBackStack("editCell")
            .commit()
    }
    fun putItem() {
        val editedCA = findViewById<TextView>(R.id.editStuffCabinet).text.toString()
        val editedCE = findViewById<TextView>(R.id.editStuffCell).text.toString()
        val editedIT = findViewById<TextView>(R.id.editStuffItem).text.toString()
        val editedETC = findViewById<TextView>(R.id.editStuffEtc).text.toString()

        val beforeStuff = itemAdapter.selecCabiList
        val editedId = beforeStuff[0].id
        val editedNum = itemAdapter.selectedNum
        val total = itemAdapter.selectedList.size

        val editedStuff = Stuff(0, editedId, editedCA, editedCE, editedIT, editedETC)
        val editItem = cabinetService.putItem(editedId, editedStuff)

        editItem.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                for (i in 0 until cabinetAdapter.stuffList.size){
                    val theStuff = cabinetAdapter.stuffList[i]
                        if(theStuff.id == editedId) {
                            cabinetAdapter.stuffList[i] = editedStuff
                            cabinetAdapter.notifyItemChanged(i)
                            cabinetAdapter.notifyDataSetChanged()
                        }
                }

                for (i in 0 until cabinetAdapter.selecCabiList.size) if (cabinetAdapter.selecCabiList[i].id == editedId) {
                    cabinetAdapter.selecCabiList[i] = editedStuff
                    cabinetAdapter.notifyItemChanged(i)
                    cabinetAdapter.notifyDataSetChanged()
                }

                for (i in 0 until cellAdapter.selecCabiList.size) if (cellAdapter.selecCabiList[i].id == editedId) {
                    cellAdapter.selecCabiList[i] = editedStuff
                    cellAdapter.notifyItemChanged(i)
                    cellAdapter.notifyDataSetChanged()
                }

                for (i in 0 until cellAdapter.stuffList.size) if (cellAdapter.stuffList[i].id == editedId) {
                    cellAdapter.stuffList[i] = editedStuff
                    cellAdapter.notifyItemChanged(i)
                    cellAdapter.notifyDataSetChanged()
                }
                if (editedCA == beforeStuff[0].cabinetName && editedCE == beforeStuff[0].cellName) {
                    itemAdapter.stuffList[editedNum!!] = editedStuff
                    itemAdapter.selectedList[editedNum] = editedIT
                    itemAdapter.notifyItemChanged(editedNum)
                    itemAdapter.notifyDataSetChanged()
                }else{
                    itemAdapter.selectedList.removeAt(editedNum!!)
                    itemAdapter.stuffList.removeAt(editedNum)
                    itemAdapter.notifyItemRemoved(editedNum)
                    itemAdapter.notifyItemRangeChanged(editedNum, total - editedNum)
                }
                Toast.makeText(this@cabinet, "물건 정보를 변경했습니다.", Toast.LENGTH_SHORT).show()
                setName(R.id.theItemName, "")
                goBack()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@cabinet, "문제가 생겼습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //서랍장 삭제
    fun deleteCabinet(){
        val cabinetN = cabinetAdapter.selectedOne
        val cabinetNo = cabinetAdapter.selectedNum
        val lastTotal = cabinetAdapter.itemCount
        val name = findViewById<TextView>(R.id.theCabinetName)
        if(name.text != "") {
            cabinetService.deleteCabinet(cabinetN).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Toast.makeText(
                        baseContext,
                        cabinetN + "서랍장이 삭제되었습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    if (cabinetNo != null) {
                        cabinetAdapter.selectedList.removeAt(cabinetNo)
                        cabinetAdapter.notifyItemRemoved(cabinetNo)
                        cabinetAdapter.notifyItemChanged(cabinetNo, lastTotal - cabinetNo)
                        name.text = ""
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
        }else{
            Toast.makeText(
                baseContext,
                cabinetN + "서랍장을 선택해주세요.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //공간 삭제
    fun deleteCell(){
        val cellN = cellAdapter.selectedOne
        val cellNo = cellAdapter.selectedNum
        val lastTotal = cellAdapter.itemCount
        val name = findViewById<TextView>(R.id.theCellName)
        cabinetService.deleteCell(cellN).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(baseContext, "$cellN 공간이 삭제되었습니다.", Toast.LENGTH_LONG).show()
                if (cellNo != null) {
                    cellAdapter.selectedList.removeAt(cellNo)
                    cellAdapter.notifyItemRemoved(cellNo)
                    cellAdapter.notifyItemChanged(cellNo, lastTotal - cellNo)
                    name.text = ""
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

    //물건 삭제
    fun deleteItem(){
        val itemN = itemAdapter.selectedOne
        val itemNo = itemAdapter.selectedNum
        val lastTotal = itemAdapter.itemCount
        val name = findViewById<TextView>(R.id.theItemName)
        cabinetService.deleteItem(itemN).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(baseContext, "$itemN 물건이 삭제되었습니다.", Toast.LENGTH_LONG).show()
                if (itemNo != null) {
                    itemAdapter.stuffList.removeAt(itemNo)
                    itemAdapter.selectedList.removeAt(itemNo)
                    itemAdapter.notifyItemRemoved(itemNo)
                    itemAdapter.notifyItemChanged(itemNo, lastTotal - itemNo)
                    name.text = ""
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


    fun getLastItem() {
        val total1 = cabinetAdapter.stuffList.size
        val total2 = itemAdapter.selectedList.size


        cabinetService.getLastItem().enqueue(object : Callback<List<Stuff>> {
            override fun onResponse(call: Call<List<Stuff>>, response: Response<List<Stuff>>) {
                val mStuff = response.body() as List<Stuff>
                val mStuffName = mStuff[0].itemName
                if (response.code() == 200) {
                    cabinetAdapter.stuffList.add(mStuff[0])
                    cabinetAdapter.notifyItemInserted(total1)
                    cabinetAdapter.notifyDataSetChanged()

                    itemAdapter.stuffList.add(mStuff[0])
                    itemAdapter.selectedList.add(mStuffName)
                    itemAdapter.notifyItemInserted(total2)
                    itemAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<Stuff>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "문제가 생겼습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    var cabPosition: Int? = null
    var celPosition: Int? = null
    var itemPosition: Int? = null

    internal fun onItemClickHandler(position: Int, type: String){
        when (type) {
            "cabinet" -> {
                setName(R.id.theCabinetName, cabinetAdapter.selectedOne)
                val editButton = findViewById<ImageButton>(R.id.editCabinetButton)
                editButton.visibility = View.VISIBLE
                cabPosition = position

            }
            "cell" -> {
                setName(R.id.theCellName, cellAdapter.selectedOne)
                val editButton = findViewById<ImageButton>(R.id.editCellButton)
                editButton.visibility = View.VISIBLE
                celPosition = position
            }
            "item" -> {
                setName(R.id.theItemName, itemAdapter.selectedOne)
                val editButton = findViewById<ImageButton>(R.id.editItemButton)
                editButton.visibility = View.VISIBLE
                itemPosition = position
            }
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

    fun setName (where: Int, content: String){
        val name = findViewById<TextView>(where)
        name.text = content
    }


}