package com.e.tmt.memo


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.tmt.R
import com.e.tmt.cabinet.CabinetAdapter
import com.e.tmt.cabinet.cabinet
import kotlinx.android.synthetic.main.fragment_cabinet_list.*
import kotlinx.android.synthetic.main.fragment_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class lamp : AppCompatActivity() {

    val retrofit = Retrofit.Builder()
        .baseUrl(ServerApi.DOMAIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val TmtService = retrofit.create(com.e.tmt.memo.TmtService::class.java)
    val adapter = CustomAdapter()

    var addMT: String = ""
    var addME: String = ""
    var addMC: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lamp)

        memoList.adapter = adapter
        memoList.layoutManager = LinearLayoutManager(this)

        setFragment()
        getMemo()

    }

    private fun setFragment() {
        val listFragment = ListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout, listFragment)
        transaction.commit()
    }

    //페이지 이동
    fun goDetail() {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fragment_open_enter,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.fragmentLayout, detailFragment)
            .addToBackStack("detail")
            .commit()
    }

    fun goEdit(){
        val editFragment = EditFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.fragmentLayout, editFragment)
            .addToBackStack("edit")
            .commit()
    }

    fun goAdd() {
        val addMemoFragment = AddMemoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(
                R.anim.slide_in_1,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .add(R.id.fragmentLayout, addMemoFragment)
            .addToBackStack("add")
            .commit()
    }


    fun goBack() {
        onBackPressed()
    }

    //서버와 데이터 통신
    fun postMemo() {
        //adapter.getOneTitle = addMemoTitle.text.toString()
        var addNewMemo = TmtService.postMemo(addME, addMT, addMC)
        addNewMemo.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    getLastMemo()

                    Toast.makeText(
                        baseContext,
                        "메모가 저장되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        baseContext,
                        "메모를 저장할 수 없습니다.",
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



    fun getMemo() {
        TmtService.getMemo().enqueue(object : Callback<List<Memo>> {
            override fun onResponse(call: Call<List<Memo>>, response: Response<List<Memo>>) {
                adapter.listData.addAll(response.body() as List<Memo>)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Memo>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "서버에서 데이터를 가져올 수 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                adapter.notifyDataSetChanged()
            }
        })

    }

    fun putMemo (){
        val mMemo = selectMemo()
        val mIndex = adapter.detailIndex
        var putId = mMemo!!.id
        mMemo.editor = addME
        mMemo.title = addMT
        mMemo.content = addMC
        val editMemo = TmtService.putMemo(putId, mMemo)
        editMemo.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                adapter.listData.set(mIndex, mMemo)
                adapter.notifyItemChanged(mIndex)
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    baseContext,
                    "데이터를 수정했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
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

    fun getLastMemo() {
        val total = adapter.listData.size

        TmtService.getLastMemo().enqueue(object : Callback<List<Memo>> {
            override fun onResponse(call: Call<List<Memo>>, response: Response<List<Memo>>) {
                var mMemo: List<Memo>? = null
                mMemo = response.body() as List<Memo>

                if (response.code() == 200) {
                    adapter.listData.add(mMemo[0])
                    adapter.notifyItemInserted(total)
                    adapter.notifyDataSetChanged()


                }

            }

            override fun onFailure(call: Call<List<Memo>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "문제가 생겼습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    fun sendMemo(){
        var sendNo = getSelectedList()
        if(sendNo.size > 3) {
            Toast.makeText(
                baseContext,
                "3개 초과",
                Toast.LENGTH_SHORT
            ).show()
        } else if(sendNo.isEmpty()){
            Toast.makeText(
                baseContext,
                "램프에 전송할 메모를 선택해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }else {
            val sendMemos = TmtService.sendMemo(sendNo)
            sendMemos.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(
                            baseContext,
                            "메모가 전송되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "메모가 전송되지 않았습니다.",
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



    fun deleteMemo(){
        var deletedID = getSelectedList()
        var deletedNo = getSelectedNoList()
        var lastTotal = adapter.itemCount
        val deleteMemos = TmtService.deleteMemo(deletedID)
        deleteMemos.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {

                    //for (i in 0 until adapter.selectedNo.size){
                    //    var toErase = adapter.selectedNo[i]
                    //    adapter.listData.removeAt(toErase)
                    //Toast.makeText(
                    //    baseContext,
                    //    "$adapter.listData(-1)",
                    //   Toast.LENGTH_LONG
                    //).show()


                    for (element in deletedNo) {
                        var deleteNumber = element
                        adapter.listData.removeAt(deleteNumber)
                        adapter.notifyItemRemoved(deleteNumber)
                        adapter.notifyItemChanged(deleteNumber, lastTotal - deleteNumber)
                    }

                    Toast.makeText(
                        baseContext,
                        "메모가 삭제되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    //}
                } else {
                    Toast.makeText(
                        baseContext,
                        "메모가 삭제되지 않았습니다.",
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


    //선택한 메모들을 찾아내는 코드
    fun selectMemo(): Memo? {
        var mMemo: Memo? = null
        mMemo = adapter.listData[adapter.detailNo]
        //for ( i in 0 until adapter.listData.size){
        //    mMemo = adapter.listData[i]
        //    if(adapter.detailNo == mMemo.id){
        //        adapter.selected = adapter.listData[i]
        //    }
        // }
        //return adapter.selected
        return mMemo
    }

    fun getSelectedList(): List<Int>{
        var selectedNo = mutableListOf<Int>()
        var sMemo: Memo? = null

        for (i in 0 until adapter.listData.size) {
            sMemo = adapter.listData[i]

            if (sMemo.selected) {
                var selectOne = (sMemo.id)
                selectedNo.add(selectOne)
            }
        }
        return selectedNo

    }

    fun getSelectedNoList(): List<Int>{
        var selectedNo = mutableListOf<Int>()
        var sMemo: Memo? = null

        for (i in 0 until adapter.listData.size) {
            sMemo = adapter.listData[i]

            if (sMemo.selected) {
                var selectOne = (i)
                selectedNo.add(selectOne)
            }
        }

        return selectedNo


    }

    //button effects
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

    @SuppressLint("ClickableViewAccessibility")
    fun buttonEffect2(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(buttonClick)
                    //v.alpha to 0.5
                    v.background.setColorFilter(
                        Color.parseColor("#F5F5F5"),
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




}
