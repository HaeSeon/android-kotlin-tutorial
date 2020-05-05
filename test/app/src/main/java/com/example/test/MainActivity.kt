package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build
import android.os.Handler
import android.widget.Toast
import com.example.test.R
import java.util.TimerTask
import java.util.Timer
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder().setMaxStreams(8).build()
    } else {
        SoundPool(8, AudioManager.STREAM_MUSIC, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val soundId = soundPool.load(this, R.raw.do1, 1)   //load()메서드로 음원의 ID 얻음
        val soundId1 = soundPool.load(this, R.raw.do_p, 1)
        val soundId2 = soundPool.load(this, R.raw.gt, 1)
        val soundId3 = MediaPlayer.create(this,R.raw.drum)



        button.setOnClickListener() {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, (Random.nextFloat()+0.5f)%2)
                }
            }, 0, 1000)


        }
        button1.setOnClickListener() {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    soundPool.play(soundId1, 0.5f, 0.5f, 0, 0, (Random.nextFloat()+0.5f)%2)
                }
            }, 0, 1000)


        }

        button2.setOnClickListener() {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    soundPool.play(soundId2, 1.0f, 1.0f, 0, 0, (Random.nextFloat()+0.5f)%2)
                }
            }, 0, 1000)


        }

        button3.setOnClickListener{ soundId3.start() }
//        findViewById<TextView>(pitch.first).setOnClickListener{             //findViewById()메서드로 텍스트뷰 ID에 해당하는 뷰를 얻음
//            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)     //텍스트뷰 클릭시 음원재생
//        }
    }


    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
