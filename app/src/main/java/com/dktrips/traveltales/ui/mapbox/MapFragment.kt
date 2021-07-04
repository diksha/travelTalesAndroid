package com.dktrips.traveltales.ui.mapbox

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dktrips.traveltales.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


class MapFragment : Fragment(R.layout.map_fragment), PermissionsListener {

    private val geojsonSourceLayerId = "geojsonSourceLayerId"
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
        initSearchFab(view)
    }

    private fun initSearchFab(view: View) {
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedCarmenFeature = PlaceAutocomplete.getPlace(result.data)
                    if (mapboxMap != null) {
                        val style = mapboxMap!!.style
                        if (style != null) {
                            val source = style.getSourceAs<GeoJsonSource>(geojsonSourceLayerId)
                            source?.setGeoJson(
                                FeatureCollection.fromFeatures(
                                    arrayOf<Feature>(
                                        Feature.fromJson(selectedCarmenFeature.toJson())
                                    )
                                )
                            )
                            mapboxMap!!.animateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.Builder()
                                        .target(
                                            LatLng(
                                                (selectedCarmenFeature.geometry() as Point?)?.latitude()
                                                    ?: 0.0,
                                                (selectedCarmenFeature.geometry() as Point?)?.longitude()
                                                    ?: 0.0
                                            )
                                        )
                                        .zoom(14.0)
                                        .build()
                                ), 4000
                            )
                        }
                    }
                }

            }

        (view.findViewById(R.id.fab_location_search) as FloatingActionButton?)?.setOnClickListener(
            View.OnClickListener {
                startForResult.launch(
                    PlaceAutocomplete.IntentBuilder()
                        .accessToken(
                            (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else context?.getString(
                                R.string.mapbox_access_token
                            )) ?: ""
                        )
                        .placeOptions(
                            PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS)
                        )
                        .build(activity)
                )
            })
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