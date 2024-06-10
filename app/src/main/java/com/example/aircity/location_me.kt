package com.example.aircity

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.data.FeatureQueryResult
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReference
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [location_me.newInstance] factory method to
 * create an instance of this fragment.
 */
class location_me : Fragment() {
    lateinit var navController: NavController
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var latTxt:EditText
    private lateinit var lonTxt:EditText
    private lateinit var btomain:Button

    private val LOCATION_PERMISSION_REQ_CODE:Int = 111

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var v:View = inflater.inflate(R.layout.location_me, container, false)
        latTxt = v.findViewById(R.id.latTxt)
        lonTxt = v.findViewById(R.id.lonTxt)
        navController = findNavController()

        val gStatus:Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if (gStatus == ConnectionResult.SUCCESS)
        {
            Toast.makeText(activity,"Available",Toast.LENGTH_LONG).show()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            manageLocation()
        }
        else{
            Toast.makeText(activity,"Cannot",Toast.LENGTH_LONG).show()
        }
        btomain = v.findViewById(R.id.btomain)
        btomain.setOnClickListener {
            navController.navigate(R.id.action_location_me_to_sorryFragment);
        }
        return v


    }
    fun manageLocation(){
        if (ContextCompat.checkSelfPermission(activity!!,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED )
        {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_PERMISSION_REQ_CODE)
        }
        else
        {
            var locationCb = object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    var last_location:Location? = p0?.lastLocation
                    if (last_location != null)
                    {
                        latTxt.setText(last_location.latitude.toString())
                        lonTxt.setText(last_location.longitude.toString())

                    }
                }
            }
            val req = com.google.android.gms.location.LocationRequest()
            req.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            req.interval = 0
            req.fastestInterval = 0
            fusedLocationClient.requestLocationUpdates(req, locationCb, null)




        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==LOCATION_PERMISSION_REQ_CODE && permissions.isNotEmpty())
        {
            var granted:Boolean = false
            for(i in permissions.indices)
            {
                if (permissions[i].equals(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    granted =true
                    manageLocation()
                    break
                }
            }
            if (!granted){
                Toast.makeText(activity,"No permission",Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment location_me.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            location_me().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}