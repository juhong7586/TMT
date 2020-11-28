package com.e.tmt

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


class ServerApi {
    companion object{
        val DOMAIN = "http://34.64.143.75:1880/"
    }
}

interface TmtService {

    @GET("memos")
    fun getMemo(): Call<List<Memo>>


    @GET("memo/{title}")
    fun getOneMemo(@Path("title") title: String): Call<Memo>

    @GET("last")
    fun getLastMemo(): Call<List<Memo>>

    //지금
    //@Headers("accept: application/json", "Content-Type: application/json")
    @FormUrlEncoded
    @POST("memo")
    fun postMemo(
        @Field("editor") editor: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("memo/send")
    fun sendMemo(
        @Field("selected") selected: List<Int>
    ): Call<ResponseBody>

    @HTTP(path="memo", method="DELETE", hasBody = true)
    fun deleteMemo(
        @Body Map: List<Int>
    ): Call<ResponseBody>

    @PUT("memo/{id}")
   // @FormUrlEncoded
    fun putMemo(
        @Path("id") id: Int,
        @Body memo: Memo
    ): Call<ResponseBody>


}