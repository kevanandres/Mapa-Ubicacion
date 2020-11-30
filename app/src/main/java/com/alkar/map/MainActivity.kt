package com.alkar.map

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import mumayank.com.airlocationlibrary.AirLocation

class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    override fun onClick(v: View?) {
        airLoc = AirLocation(this, true,true, object : AirLocation.Callbacks{
            override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                Toast.makeText(this@MainActivity,"No se pudo obtener la posición actual", Toast.LENGTH_SHORT).show()
                editText.setText("No se pudo obtener la posición actual")
            }

            override fun onSuccess(location: Location) {
                val ll=LatLng(location.latitude,location.longitude)
                gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16.0f))
                editText.setText("Posicion: Latitud=${location.latitude}, " + "Longitud=${location.longitude}")
            }
        })
    }

    override fun onMapReady(p0: GoogleMap?) {
        gMap = p0
        if (gMap!=null){
            airLoc = AirLocation(this, true,true, object : AirLocation.Callbacks{
                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                    Toast.makeText(this@MainActivity,"No se pudo obtener la posición actual", Toast.LENGTH_SHORT).show()
                    editText.setText("No se pudo obtener la posición actual")
                }

                override fun onSuccess(location: Location) {
                    val ll=LatLng(location.latitude,location.longitude)
                    gMap!!.addMarker(MarkerOptions().position(ll).title("Posicion"))
                    gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16.0f))
                    editText.setText("Posicion: Latitud=${location.latitude}, " + "Longitud=${location.longitude}")
                }
            })
        }
    }

    var airLoc: AirLocation? = null
    var gMap : GoogleMap? = null
    lateinit var mapFragment : SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fab.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLoc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        airLoc?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}