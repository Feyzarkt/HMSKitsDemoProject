package com.hw.hmskitsdemoproject

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.hw.hmskitsdemoproject.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding

    // Define a fusedLocationProviderClient object.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMapView: MapView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(inflater, container, false )

        //initLocation()

        // Initialize the SDK.
        mMapView = binding.mapviewMapviewdemo
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }
        mMapView.onCreate(mapViewBundle)
        mMapView.getMapAsync(this)

        // Inflate the layout for this fragment
        return binding.root
    }
    private fun initLocation(huaweiMap: HuaweiMap?) {

        // Instantiate the fusedLocationProviderClient object.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mLocationRequest = LocationRequest()
        // Set the location update interval, in milliseconds.
        mLocationRequest.interval = 10000

        val mLocationCallback: LocationCallback
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.e("onLocationResultt", locationResult.toString())
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
            .addOnSuccessListener {
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        Toast.makeText(requireContext(), location.latitude.toString() + "\n" + location.longitude.toString(), Toast.LENGTH_LONG).show()

                        // Move camera for my location
                        val latLng = LatLng(location.latitude, location.longitude)
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        huaweiMap?.animateCamera(cameraUpdate)

                        //Adding marker
                        val options = MarkerOptions()
                            .position(latLng)
                            .title("Hello Huawei Map")
                            .snippet("I'm here!")
                        huaweiMap?.addMarker(options)

                        val latLng2 = LatLng(location.latitude+5, location.longitude+5)
                        val options2 = MarkerOptions()
                            .position(latLng2)
                            .title("Hello Huawei Map")
                            .snippet("Hello")
                        huaweiMap?.addMarker(options2)

                    }.addOnFailureListener { exception ->
                        Log.e("ERROR-e", exception.toString())
                    }
            }.addOnFailureListener { exception ->
                Log.e("ERROR-e", exception.toString())
            }
    }

    override fun onMapReady(huaweiMap: HuaweiMap?) {
        // Enable the my-location layer.
        huaweiMap?.isMyLocationEnabled = true
        // Enable the my-location icon.
        huaweiMap?.uiSettings?.isMyLocationButtonEnabled = true

        initLocation(huaweiMap)
    }
}