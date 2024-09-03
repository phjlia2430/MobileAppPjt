package com.example.ch18_image

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// http://apis.data.go.kr/B552584/UlfptcaAlarmInqireSvc/getUlfptcaAlarmInfo?year=2024&pageNo=1&numOfRows=10&returnType=xml&serviceKey=서비스키(일반 인증키(Encoding))
////https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=FcCvZgmYSqvi9T4wJS1PY4Ync%2Fa71kRJbwP4%2B80m5pX9bbTnBwHBUDC83ff9Acmee83yj6a55VcmRvUV2T2rHA%3D%3D&pageNo=1&numOfRows=10
class RetrofitConnection{

    //객체를 하나만 생성하는 싱글턴 패턴을 적용합니다.
    companion object {
        //API 서버의 주소가 BASE_URL이 됩니다.
        private const val BASE_URL = "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/"

        var xmlNetworkService : NetworkService
        val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        val xmlRetrofit : Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create(parser))
                .build()


        //http://localhost/PHP_connection.php
        private const val BASE_URL_PHP = "http://192.168.56.1/"

        var phpNetworkService : NetworkService
        val phpRetrofit:Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL_PHP)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        init{
            xmlNetworkService = xmlRetrofit.create(NetworkService::class.java)
            phpNetworkService = phpRetrofit.create(NetworkService::class.java)
        }
    }
}