package com.example.vorto.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vorto.BaseFragment
import com.example.vorto.R
import com.example.vorto.adapter.BusinessListAdapter
import com.example.vorto.model.Businesses
import com.example.vorto.repo.Injection
import com.example.vorto.utils.CommonViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import java.util.jar.Manifest

class SearchFragment : BaseFragment() {
    private lateinit var recyclerView: RecyclerView
    private var businessListAdapter: BusinessListAdapter? = null
    private lateinit var locationManger: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var client: FusedLocationProviderClient
    private lateinit var searchInputEditText: TextInputEditText

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val searchViewModel: SearchViewModel by activityViewModels {
        CommonViewModelFactory(Injection.getRepositoryImpl())
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        client = LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()

        } else {

            requestPermissionFor(201,this)
        }
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==201)
        {
            if (grantResults.isNotEmpty()&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.business_recycler_view)
        searchInputEditText = view.findViewById(R.id.business_search_editText)

        searchInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                searchViewModel.getBusinessList(s.toString(), latitude, longitude)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchViewModel.businesslist.observe(viewLifecycleOwner, Observer {
            businessListAdapter = BusinessListAdapter(it, ::itemclicked)
            recyclerView.adapter = businessListAdapter
        })

        searchViewModel.dialog.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        locationManger = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManger.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {

            client.lastLocation.addOnCompleteListener {

                val location: Location = it.result
                latitude = location.latitude
                longitude = location.longitude
            }

            return


        }

    }

    private fun itemclicked(businesses: Businesses) {

        findNavController().navigate(R.id.destination_maps, Bundle().apply {
            putDouble("curr_lat", latitude)
            putDouble("curr_long", longitude)
            putParcelable("BusinessItem", businesses)
        })
    }

}