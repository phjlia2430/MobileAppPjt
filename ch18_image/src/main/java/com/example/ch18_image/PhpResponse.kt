package com.example.ch18_image

data class HinfoData(val Age:Int?, val name: String?, val addr: String?)

data class PhpResponse(val result : ArrayList<HinfoData>)