package com.gnz.locamat.feature.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gnz.locamat.R
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.extensions.observe
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.viewModel

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    val mapViewmodel by viewModel<MapViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        observe(mapViewmodel.atmLiveData) { drawAtms(googleMap, it) }
    }

    private fun drawAtms(googleMap: GoogleMap, atms: List<LocATM>) {
        atms.forEach { atm->
            val marker = MarkerOptions()
                    .position(LatLng(atm.latitude, atm.longitude))
                    .title(getString(R.string.welcome_atm, atm.name))
            googleMap.addMarker(marker)
        }
    }
}
