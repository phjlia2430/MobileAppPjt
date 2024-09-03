package com.example.ch18_image

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=FcCvZgmYSqvi9T4wJS1PY4Ync%2Fa71kRJbwP4%2B80m5pX9bbTnBwHBUDC83ff9Acmee83yj6a55VcmRvUV2T2rHA%3D%3D&pageNo=1&numOfRows=10
interface NetworkService {
    //http://localhost/PHP_connection.php?age=22
    @GET("PHP_connection.php")
    fun getPhpList(
        @Query("age") age:String
    ) : Call<PhpResponse>

    @GET("abandonmentPublic")
    fun getXmlList(
        @Query("happenDt") date:String,
        @Query("pageNo") pageNo:Int,
        @Query("numOfRows") numOfRows:Int,
        @Query("returnType") returnType:String,
        @Query("serviceKey") apiKey:String
    ) : Call<XmlResponse>

}

