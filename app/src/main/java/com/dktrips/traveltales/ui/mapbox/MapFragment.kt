package com.dktrips.traveltales.ui.mapbox

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.fragment.app.Fragment
import com.dktrips.traveltales.R
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded


class MapFragment : Fragment(R.layout.map_fragment), PermissionsListener {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.mapView) as MapView?
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                enableLocationComponent(it)
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val locationComponentOptions = LocationComponentOptions.builder(requireContext())
                .pulseEnabled(true)
                .pulseColor(Color.GREEN)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .backgroundTintColor(Color.BLUE)
                .foregroundTintColor(Color.RED)
                .build()
            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(requireContext(), style)
                .locationComponentOptions(locationComponentOptions)
                .build()
            mapboxMap?.locationComponent?.apply {
                activateLocationComponent(
                    locationComponentActivationOptions
                )
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS

            }
        } else {
            var permissionsManager = PermissionsManager(this);
            permissionsManager.requestLocationPermissions(requireActivity());
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap?.getStyle(OnStyleLoaded { style -> enableLocationComponent(style) })
        } else {
            parentFragmentManager.beginTransaction().remove(this)
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

    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
    }
}