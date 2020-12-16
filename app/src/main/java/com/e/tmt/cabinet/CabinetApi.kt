package com.e.tmt.cabinet

import com.e.tmt.memo.Memo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import java.util.*

class CabinetApi {
    companion object{
        val DOMAIN = "https://safelygogoschool.app/"
    }
}

interface CabinetService {
    //CABINET HTTP REQUESTS
    @GET("things")
    fun getStuff(): Call<List<Stuff>>
    //fun get(): Call<Thing>

    @GET("lastItem")
    fun getLastItem(): Call<List<Stuff>>



    @DELETE("cabinet/{name}")
    fun deleteCabinet(@Path("name") name: String): Call<ResponseBody>

    @DELETE("cell/{name}")
    fun deleteCell(@Path("name") name: String): Call<ResponseBody>

    @DELETE("item/{name}")
    fun deleteItem(@Path("name") name: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("item")
    fun postItem(
        @Field("cabinet") cabinet: String,
        @Field("cell") cell: String,
        @Field("item") item: String,
        @Field("etc") etc: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("item/send")
    fun sendToCabinet(
        @Field("selected") selected: List<Int>
    ): Call<ResponseBody>


    @PUT("cabinet/{name}")
    // @FormUrlEncoded
    fun putCabinet(
        @Path("name") name: String,
        @Body Ids: List<Int>
    ): Call<ResponseBody>

    @PUT("cell/{name}")
    // @FormUrlEncoded
    fun putCell(
        @Path("name") name: String,
        @Body Ids: List<Int>
    ): Call<ResponseBody>

    @PUT("item/{id}")
    // @FormUrlEncoded
    fun putItem(
        @Path("id")id: Int,
        @Body stuff: Stuff
    ): Call<ResponseBody>

}