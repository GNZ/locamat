package com.gnz.locamat.feature.map


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gnz.locamat.R
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.extensions.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel


class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance(): Fragment = MapFragment()
    }

    val mapViewmodel by viewModel<MapViewModel>()

    private val visibleMarkers = HashMap<Long, Marker>()

    lateinit var googleMapFragment: SupportMapFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        googleMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        googleMapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val zurich = LatLng(47.382992, 8.537409)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zurich, 12f))
        observe(mapViewmodel.atmLiveData) { drawAtms(googleMap, it) }
    }

    private fun drawAtms(googleMap: GoogleMap, atms: List<LocATM>) {
        addItemsToMap(atms, googleMap)
        googleMap.setOnCameraIdleListener {
            addItemsToMap(atms, googleMap)
        }
    }

    private fun LocATM.getLatLong(): LatLng = LatLng(latitude, longitude)

    private fun addItemsToMap(atms: List<LocATM>, map: GoogleMap) {
        //This is the current user-viewable region of the map
        val bounds = map.projection.visibleRegion.latLngBounds

        //Loop through all the items that are available to be placed on the map
        atms.forEach { atm ->
            //If the item is within the the bounds of the screen
            if (bounds.contains(atm.getLatLong())) {
                //If the item isn't already being displayed
                if (!visibleMarkers.containsKey(atm.id)) {
                    //Add the Marker to the Map and keep track of it with the HashMap
                    //getMarkerForItem just returns a MarkerOptions object
                    val marker = MarkerOptions()
                            .position(LatLng(atm.latitude, atm.longitude))
                            .title(getString(R.string.welcome_atm, atm.name))
                    visibleMarkers[atm.id!!] = map.addMarker(marker)
                }
            }
            //If the marker is off screen
            else {
                //If the course was previously on screen
                if (visibleMarkers.containsKey(atm.id)) {
                    //1. Remove the Marker from the GoogleMap
                    visibleMarkers[atm.id]?.remove()
                    //2. Remove the reference to the Marker from the HashMap
                    visibleMarkers.remove(atm.id)
                }
            }
        }
    }
}
