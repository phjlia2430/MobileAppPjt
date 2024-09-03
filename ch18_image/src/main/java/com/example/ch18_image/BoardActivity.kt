package com.example.ch18_image

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch18_image.databinding.ActivityBoardBinding
import com.google.firebase.firestore.Query
import java.io.BufferedReader
import java.io.File

class BoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainFab.setOnClickListener {
            if(MyApplication.checkAuth()){
                startActivity(Intent(this, AddActivity::class.java))
            }
            else{
                Toast.makeText(this,"인증을 먼저 진행해주세요..", Toast.LENGTH_LONG).show()
            }
        }

        //파일 읽기
        val file = File(filesDir,"test.txt")
        val readstream: BufferedReader =file.reader().buffered()
        binding.lastsaved.text = "마지막 저장시간 : "+readstream.readLine()
    }

    override fun onStart() {
        super.onStart()

        if(MyApplication.checkAuth()){
            MyApplication.db.collection("comments")
                .orderBy("date_time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemData>()
                    for( document in result ){
                        val item = document.toObject(ItemData::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.adapter = BoardAdapter(this, itemList)

                }
                .addOnFailureListener {
                    Toast.makeText(this,"서버 데이터 획득 실패",Toast.LENGTH_LONG).show()
                }
        }
    }
}