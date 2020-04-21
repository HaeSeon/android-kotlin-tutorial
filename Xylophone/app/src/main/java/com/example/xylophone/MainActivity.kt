package com.example.xylophone

import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder().setMaxStreams(8).build()
    } else {
        SoundPool(8, AudioManager.STREAM_MUSIC, 0)
    }

    //Pair : 텍스트뷰 ID, 음원리소스 ID를 연관지음
    //8개의 Pair 객체를 리스트객체 sounds 로 만든다.
    private val sounds = listOf(
        Pair(R.id.do1, R.raw.do1),
        Pair(R.id.re, R.raw.re),
        Pair(R.id.mi, R.raw.mi),
        Pair(R.id.fa, R.raw.fa),
        Pair(R.id.sol, R.raw.sol),
        Pair(R.id.la, R.raw.la),
        Pair(R.id.si, R.raw.si),
        Pair(R.id.do2, R.raw.do2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sounds.forEach{tune(it)}
    }

    private fun tune(pitch:Pair<Int,Int>){
        val soundId = soundPool.load(this, pitch.second,1)   //load()메서드로 음원의 ID 얻음
        findViewById<TextView>(pitch.first).setOnClickListener{             //findViewById()메서드로 텍스트뷰 ID에 해당하는 뷰를 얻음
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)     //텍스트뷰 클릭시 음원재생
        }
    }

    fun onDesrtoy(){
        super.onDestroy()
        soundPool.release()      //앱 종료시 반드시 release() 메소서드를 호출하여 SoundPool 객체 자원 해재
    }
}
