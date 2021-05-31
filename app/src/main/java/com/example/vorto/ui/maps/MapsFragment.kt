package com.example.vorto.ui.maps

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.vorto.BaseFragment
import com.example.vorto.R
import com.example.vorto.model.Businesses
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import java.text.DecimalFormat


class MapsFragment : BaseFragment() {

    private var current_lat: Double = 0.0
    private var current_long: Double = 0.0
    private lateinit var businesses: Businesses


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val currentLocation = LatLng(current_lat, current_long)
        val businessLocation =
            LatLng(businesses.coordinates.latitude, businesses.coordinates.longitude)
        googleMap.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
        googleMap.addMarker(
            MarkerOptions().position(businessLocation).title(businesses.name)
                .snippet(businesses.location.address1 + "\n" + businesses.location.city + "\n" + businesses.distance?.let {
                    metersToMiles(
                        it
                    )
                } + "miles")
        )

        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(p0: Marker): View? {
                return null
            }

            override fun getInfoContents(p0: Marker): View? {

                val info = LinearLayout(requireContext())
                info.orientation = LinearLayout.VERTICAL

                val title = TextView(requireContext())
                title.gravity = Gravity.CENTER
                title.setTypeface(null, Typeface.BOLD)
                title.setText(p0.getTitle())

                val snippet = TextView(requireContext())
                snippet.setText(p0.getSnippet())

                info.addView(title)
                info.addView(snippet)
                return info
            }

        })
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 1000, null);


    }

    override fun onBackPressed(): Boolean {
        findNavController().popBackStack()
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        handleArguments()

        mapFragment?.getMapAsync(callback)

    }

    fun metersToMiles(meters: Double): String {
        val df = DecimalFormat("0.00")

        return df.format(meters / 1609.3440057765)
    }

    private fun handleArguments() {
        if (arguments != null) {
            if (arguments!!.containsKey("BusinessItem")) {
                businesses = requireArguments().getParcelable("BusinessItem")!!

            }
            current_lat = requireArguments().getDouble("curr_lat")
            current_long = requireArguments().getDouble("curr_long")
        }

    }
}