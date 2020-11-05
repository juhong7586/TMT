package com.e.tmt

import retrofit2.Call
import retrofit2.http.*


class ServerApi {
    companion object{
        val DOMAIN = "https://safelygogoschool.app/"
    }
}

interface TmtService {
    @GET("memo")
    fun getMemo(): Call<List<Memo>>

    //나중
    //@GET("memo/{id}")
    //fun myObjectById(@Path("id")) id: int?, @Query("a_param") aParam: String?): Call<List<Memo>>

    //지금
    //@Headers("accept: application/json", "Content-Type: application/json")
    @FormUrlEncoded
    @POST("memo")
    fun postMemo(
        @Field("editor") editor: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Call<Result>
}
