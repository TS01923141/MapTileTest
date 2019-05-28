package com.example.maptiletest

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.maptiletest.model.OnlineTileProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var tileProvider: TileProvider? = null
    private var tileOverlay: TileOverlay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        Completable.fromAction(Action {
//            val bitmap  : Bitmap = Glide.with(this)
//                .asBitmap()
//                .load("https://tile.openstreetmap.org/2/0/3.png")
//                .submit().get()
//        }).subscribeOn(Schedulers.newThread())
//            .subscribe()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        //hide google map layer
        mMap.mapType = GoogleMap.MAP_TYPE_NONE
        setGoogleMapStyle(R.string.google_map_style_hide)
        //set osm tile layer
        tileProvider = OnlineTileProvider(1)
        tileOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider).zIndex(0f))
    }

    //設定地圖樣式
    private fun setGoogleMapStyle(style: Int): Boolean {
        return if (mMap == null) {
            false
        } else mMap.setMapStyle(
            MapStyleOptions(
                resources
                    .getString(style)
            )
        )
    }
}
