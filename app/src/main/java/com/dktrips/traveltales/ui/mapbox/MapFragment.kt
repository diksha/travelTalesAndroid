package com.dktrips.traveltales.ui.mapbox

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dktrips.traveltales.R
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style


class MapFragment : Fragment(R.layout.map_fragment) {

    private var mapView: MapView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = view.findViewById(R.id.mapView) as MapView?
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            }
        }
    }
     override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

     override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

     override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

     override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

     override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }
}