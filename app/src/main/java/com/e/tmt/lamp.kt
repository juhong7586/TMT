package com.e.tmt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lamp.*
import kotlinx.android.synthetic.main.fragment_add_memo.*
import kotlinx.android.synthetic.main.fragment_list.*
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

    val TmtService = retrofit.create(TmtService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lamp)

        setFragment()
        getMemo()

    }

    fun setFragment() {
        val listFragment: ListFragment = ListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout, listFragment)
        transaction.commit()
    }

    fun goDetail() {
        val detailFragment: DetailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout, detailFragment)
        transaction.addToBackStack("detail")
        transaction.commit()
    }

    fun goAdd() {
        val addMemoFragment: AddMemoFragment = AddMemoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout, addMemoFragment)
        transaction.addToBackStack("detail")
        transaction.commit()
    }

    fun goBack() {
        onBackPressed()
    }

    fun postMemo() {
        val addNewMemo = TmtService.postMemo(addMemoEditor.text.toString(), addMemoTitle.text.toString(), addMemoContent.text.toString())
        addNewMemo.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if(response.code() ==  200){
                    Toast.makeText(
                        baseContext,
                        "성공입니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    Toast.makeText(
                        baseContext,
                        "데이터를 전송할 수 없습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "성공입니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun getMemo() {
        val adapter = CustomAdapter()
        memoList.adapter = adapter
        memoList.layoutManager = LinearLayoutManager(this)

        TmtService.getMemo().enqueue(object : Callback<List<Memo>> {
            override fun onResponse(call: Call<List<Memo>>, response: Response<List<Memo>>) {
                adapter.listData.addAll(response.body() as List<Memo>)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Memo>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "서버에서 데이터를 가져올 수 없습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    fun deleteMemo(){

    }

    fun selectMemo(){

    }
}
