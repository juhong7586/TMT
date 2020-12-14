package com.e.tmt.cabinet

import com.e.tmt.memo.Memo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import java.util.*

class CabinetApi {
    companion object{
        val DOMAIN = "http://34.64.143.75:1880/"
    }
}

interface CabinetService {
    //CABINET HTTP REQUESTS
    @GET("things")
    fun getStuff(): Call<List<Stuff>>
    //fun get(): Call<Thing>

    @GET("item/{cell}")
    fun getItems1(@Path("cell") cell: String): Call <List<String>>


    @DELETE("cabinet/{name}")
    fun deleteCabinet(@Path("name") name: String): Call<ResponseBody>

    @DELETE("cell/{name}")
    fun deleteCell(@Path("name") name: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("item/send")
    fun sendToCabinet(
        @Field("selected") selected: List<Int>
    ): Call<ResponseBody>


}