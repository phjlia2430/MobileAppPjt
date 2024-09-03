package com.example.ch18_image

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.ch18_image.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.io.BufferedReader
import java.io.File

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    // DrawerLayout Toggle
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var headerView : View
    lateinit var sharedPreference : SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 0
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //알림 notification
        /*돌아왔을때*/
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() ) {
            if (it.all { permission -> permission.value == true }) {
                noti()
            }
            else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }



        // DrawerLayout Toggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // Drawer 메뉴
        binding.mainDrawerView.setNavigationItemSelectedListener(this)

        headerView=binding.mainDrawerView.getHeaderView(0)
        val button =  headerView.findViewById<Button>(R.id.btnAuth)
        button.setOnClickListener {
            Log.d("mobileapp","button.setOnClickListener")
            val intent = Intent(this,AuthActivity::class.java)
            if(button.text.equals("로그인")){
                intent.putExtra("status","logout")
            }
            else if(button.text.equals("로그아웃")){
                intent.putExtra("status","login")
            }
            startActivity(intent)
            binding.drawer.closeDrawers()
        }


        //val tv = headerView.findViewById<TextView>(R.id.tvID)
        val ex = headerView.findViewById<TextView>(R.id.extra)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val color = sharedPreference.getString("color","#2C80CF")
        button.setBackgroundColor(Color.parseColor(color))


        val idStr = sharedPreference.getString("id","소개글")
        ex.text = idStr


        val style = sharedPreference.getString("font","bold")
        val typeface = when (style) {
            "bold" -> Typeface.BOLD
            "italic" -> Typeface.ITALIC
            "bold_italic" -> Typeface.BOLD_ITALIC
            else -> Typeface.NORMAL
        }
        ex.setTypeface(null, typeface)


        val size = sharedPreference.getString("size","16.0f")
        ex.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size!!.toFloat())



        binding.btnSearch.setOnClickListener{
            if(MyApplication.checkAuth()){
                val happenDt = binding.happenDt.text.toString()
                Log.d("mobileapp", happenDt )

                val call: Call<XmlResponse> = RetrofitConnection.xmlNetworkService.getXmlList(
                    happenDt,
                    1,
                    30,
                    "xml",
                    "FcCvZgmYSqvi9T4wJS1PY4Ync/a71kRJbwP4+80m5pX9bbTnBwHBUDC83ff9Acmee83yj6a55VcmRvUV2T2rHA==" // 일반인증키(Decoding)
                )

                call?.enqueue(object : Callback<XmlResponse> {
                    override fun onResponse(call: Call<XmlResponse>, response: Response<XmlResponse>) {
                        if(response.isSuccessful){
                            Log.d("mobileApp", "$response")
                            Log.d("mobileApp", "${response.body()}")
                            binding.xmlRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                            binding.xmlRecyclerView.adapter = XmlAdapter(response.body()!!.body!!.items!!.item)
                            binding.xmlRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
                        }
                    }

                    override fun onFailure(call: Call<XmlResponse>, t: Throwable) {
                        Log.d("mobileApp", "onFailure ${call.request()}")
                    }
                })

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this,"android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED) {
                        noti()
                    }
                    else {
                        permissionLauncher.launch( arrayOf( "android.permission.POST_NOTIFICATIONS"  ) )
                    }
                }
                else {
                    noti()
                }
            }
            else{
                Toast.makeText(this,"인증을 먼저 진행해주세요..", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun noti(){
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){     // 26 버전 이상
            val channelId="one-channel"
            val channelName="My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {   // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)  // 앱 런처 아이콘 상단에 숫자 배지를 표시할지 여부를 지정
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)
            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(this, channelId)
        }
        else {  // 26 버전 이하
            builder = NotificationCompat.Builder(this)
        }

        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.drawable.small)
            setWhen(System.currentTimeMillis())
            setContentTitle("알림 발생")
            setContentText("검색 완료")
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
        }

        manager.notify(11, builder.build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // DrawerLayout Toggle
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        if(item.itemId===R.id.menu_main_setting){
            val intent=Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val ex = headerView.findViewById<TextView>(R.id.extra)
        val button =  headerView.findViewById<Button>(R.id.btnAuth)

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val color = sharedPreference.getString("color","#2C80CF")
        button.setBackgroundColor(Color.parseColor(color))

        val idStr = sharedPreference.getString("id","소개글")
        ex.text = idStr


        val style = sharedPreference.getString("font","regular")
        val typeface = when (style) {
            "regular"->Typeface.NORMAL
            "bold" -> Typeface.BOLD
            "italic" -> Typeface.ITALIC
            "bold_italic" -> Typeface.BOLD_ITALIC
            else -> Typeface.NORMAL
        }
        ex.setTypeface(null, typeface)


        val size = sharedPreference.getString("size","16.0f")
        ex.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size!!.toFloat())
    }

    // Drawer 메뉴
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_php -> {
                Log.d("mobileapp", "친구목록 메뉴")
                val intent = Intent(this,PhpActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                true
            }
            R.id.item_board -> {
                Log.d("mobileapp", "게시판 메뉴")
                val intent = Intent(this,BoardActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                true
            }
            R.id.item_setting -> {
                Log.d("mobileapp", "안내 메뉴")
                val intent = Intent(this,NoticeActivity::class.java)
                startActivity(intent)

                binding.drawer.closeDrawers()
                true
            }
        }
        return false
    }

    override fun onStart(){
        super.onStart()

        val button = headerView.findViewById<Button>(R.id.btnAuth)
        val tv = headerView.findViewById<TextView>(R.id.tvID)
        
        if(MyApplication.checkAuth()){
            button.text = "로그아웃"
            tv.text = "${MyApplication.email}님 \n 반갑습니다."
        }
        else{
            button.text = "로그인"
            tv.text = "안녕하세요."
        }
    }


}