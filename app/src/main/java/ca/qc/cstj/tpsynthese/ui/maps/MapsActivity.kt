package ca.qc.cstj.tpsynthese.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.ActivityMapsBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val args: MapsActivityArgs by navArgs()

    private val locationPermissionRequest by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermissionRequest.send()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // mMap.isTrafficEnabled = true

        val permissionStatus = locationPermissionRequest.checkStatus()
        if (permissionStatus.allGranted()) {
            @SuppressLint("MissingPermission")
            mMap.isMyLocationEnabled = true
        }

        val meteoMarkerOptions = MarkerOptions().position(args.coordinate)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            .title("${args.coordinate.latitude}, ${args.coordinate.longitude}")

        mMap.addMarker(meteoMarkerOptions)

        val cameraPosition = CameraPosition.Builder().target(args.coordinate).zoom(10f).build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // Add a marker in Sydney and move the camera
        // val sydney = LatLng(-34.0, 151.0)
        // mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}