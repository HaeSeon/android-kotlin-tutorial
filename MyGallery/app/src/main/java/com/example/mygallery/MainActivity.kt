package com.example.mygallery

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE=1000    //????권한 요청에 대한 결과를 분기처리 하기 위해 쓰임

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //앱에 권한이 부여되어있는지 확인, 앱에 권한이 있으면 PERMISSION_GRANTED 가 반환, 없으면 PERMISSION_DENIED 가 반환됨
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ //권한이 없으면

            //권한이 허용되지 않음, 사용자가 이전에 권한 요청을 거부한 적이 있으면 true 를 반환한다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

                //이전에 이미 권한을 거부한 적이 있을 때에 메세지를 표시하고 다시 권한을 요청한다.
                alert ("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다","권한이 필요한 이유"){
                    yesButton {
                        //권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {  }
                }.show()
            } else {
                //권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        }else{
            //권한이 이미 허용되어 있을 때
            getAllPhotos()
        }
    }

    override fun onRequestPermissionsResult(
        //사용자가 권한을 요청하면 시스템은 onRequestPermissionsResult()매서드 호출. 권한이 부여되었는지 확인하려면 이 메서드를 오버라이드 해야함 ??
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //grandResults 배열에는 요청한 권한들의 결과가 전달됨
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE ->{
                if((grantResults.isNotEmpty()
                            &&grantResults[0]==PackageManager.PERMISSION_GRANTED)){ //하나의 권한만 요청했으므로 0번 인덱스 확인
                    //권한 허용됨
                    getAllPhotos()
                }else{
                    //권한 거부
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }

    private fun getAllPhotos(){

        //모든 사진 정보 가져오기
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_ADDED+" DESC")  //찍은 날짜 내림차순

        val fragments = ArrayList<Fragment>()
        if(cursor != null){
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while(cursor.moveToNext()){   //Cursor 객체는 내부적으로 포인터를 가지고있어서 다음정보로 이동할 수 있다. 사진이 없으면 null

                //사진 경로 url 가져오기
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )



                //사진을 Cursor 객체에서 가져올 때 마다 프래그먼트를 생성하여 fragments 리스트에 추가
                fragments.add(PhotoFragment.newInstance(contentUri))
                Log.d("MainActivity", contentUri.toString())

            }
            cursor.close()
        }


        //어댑터  ????
        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.updateFragments(fragments)
        viewPager.adapter = adapter


        //타이머 : 3초마다 자동으로 슬라이드
        timer(period = 3000){
            runOnUiThread { //UI 조작
                if(viewPager.currentItem<adapter.count-1){  //마지막 페이지가 아니면 다음페이지로
                    viewPager.currentItem=viewPager.currentItem+1
                } else{       //마지막 페이지면 첫번째 페이지로
                    viewPager.currentItem=0
                }
            }

        }
    }
}
