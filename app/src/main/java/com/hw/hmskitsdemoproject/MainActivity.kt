package com.hw.hmskitsdemoproject

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMapView: MapView
    private var hMap: HuaweiMap? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestPermission()

        mMapView = findViewById(R.id.mapview_mapviewdemo)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }
        mMapView.onCreate(mapViewBundle)
        mMapView.getMapAsync(this)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }
    }

    override fun onMapReady(huaweiMap: HuaweiMap?) {
        hMap = huaweiMap

        val mLocationRequest = LocationRequest()
        val mLocationCallback = object : LocationCallback() {}
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
            .addOnSuccessListener {

                fusedLocationProviderClient.lastLocation.addOnSuccessListener{ location->

                    Log.d("TAGGG", "locationLastKnow\n\nlocation: \n" + location.latitude + "\n" + location.longitude);

                    // Enable the my-location layer.
                    hMap?.isMyLocationEnabled  = true
                    // Enable the my-location icon.
                    hMap?.uiSettings?.isMyLocationButtonEnabled = true

                    val lastLocLatLng = LatLng(location.latitude, location.longitude)
                    hMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocLatLng, 15f))

                    val options = MarkerOptions()
                        .position(lastLocLatLng)
                        .title("Hello Huawei Map")
                        .snippet("This is a snippet!")

                    hMap?.addMarker(options)

                }.addOnFailureListener{ e ->
                    Log.d("TAGGG - e", "e $e");
                }
            }
            .addOnFailureListener { e ->
                Log.d("TAGGG - e", "e $e");
            }
    }

    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle: Bundle? = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
        Log.e("MAP", "onStart")
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
        Log.e("MAP", "onResume")
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
        Log.e("MAP", "onPause")
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
        Log.e("MAP", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        Log.e("MAP", "onDestroy")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
        Log.e("MAP", "onLowMemory")
    }
}