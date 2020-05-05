package com.example.gpsmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //위치정보를 주기적으로 얻는데 필요한 객체들
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)
        // SupportMapFragment를 가져와서 지도가 준비되면 알림을 받습니다.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()
    }

    //위치정보를 얻기 위한 각종 초기화
    private fun locationInit(){
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest= LocationRequest()

        //GPS 우선
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY   //priority : 정확도를 나타냄
        //업데이트 인터벌
        locationRequest.interval = 10000    //위치를 갱신하는데 필요한 시간을 밀리초 단위로 입력
        locationRequest.fastestInterval = 5000      //다른 앱에서 위치를 갱신했을 때 그 정보를 가장 빠른 간격(밀리초 단위)로 입력

        //GPS 를 사용하여 가장 정확한 위치를 요구하면서 10초마다 위치정도 갱신, 그 사이 다른 앱에서 위치를 갱신햇다면 5초마다 확인하여 그 값을 활용(베터리 절약)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //위치요청은 액티비티가 활성화되는 onResume()매서드에서 수행
    override  fun onResume(){
        super.onResume()
        addLocationListner()
    }

    private  fun addLocationListner(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null)

    }

    inner class MyLocationCallBack : LocationCallback(){
        override fun onLocationResult(locaationResult: LocationResult?) {
            super.onLocationResult(locaationResult)

            var location = LocationResult?.lastLocation
            location?. = LatLng(latitude, longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        }
    }
}
