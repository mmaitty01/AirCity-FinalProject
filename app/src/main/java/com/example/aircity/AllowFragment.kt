package com.example.aircity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.data.FeatureQueryResult
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllowFragment : Fragment() {
    lateinit var navController: NavController
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var nextBtn2: Button
    private lateinit var nextBtn1: Button
    private val LOCATION_PERMISSION_REQ_CODE:Int = 111

    private var longp:Double = 101.00
    private var latp:Double = 10.00



    private var lastLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_allow, container, false)

        // Inflate the layout for this fragment

        var v:View = inflater.inflate(R.layout.fragment_allow, container, false)


        nextBtn2 = v.findViewById(R.id.goToLoginBtn)
        navController = findNavController()

        val gStatus:Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)


        nextBtn2.setOnClickListener {
        if (gStatus == ConnectionResult.SUCCESS)
        {
         Toast.makeText(activity, "Available", Toast.LENGTH_LONG).show()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            manageLocation()

            val serviceFeatureTable =
                    ServiceFeatureTable(
                            "https://services1.arcgis.com/jSaRWj2TDlcN1zOC/ArcGIS/rest/services/Thailand_Province_Boundaries_2019/FeatureServer/0"
                    )

    //        fusedLocationClient?.lastLocation!!.addOnCompleteListener(activity!!) { task ->

       //         lastLocation = task.result
         //       val currentLocation101 = Point((lastLocation)!!.longitude, (lastLocation)!!.latitude,  SpatialReferences.getWgs84())


            //โลเคชั่น กรุงเทพ
            val currentLocation = Point(100.536501,  13.729852, SpatialReferences.getWgs84())
            //โลเคชั่น นอกกรุงเทพ
            val currentLocation1 = Point(98.9853,  18.7883, SpatialReferences.getWgs84())
            // query ด้วย geometry

            val query = QueryParameters().apply {
                geometry = currentLocation101(context!!)
            }
            val future: ListenableFuture<FeatureQueryResult> = serviceFeatureTable.queryFeaturesAsync(query, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL)
            // add done loading listener to fire when the selection returns
            future.addDoneListener {
                try {
                    // call get on the future to get the result
                    val results = future.get()
                    var isBkk = false
                    if (results.count() > 0) {
                        results.forEach { feature ->
                            // ได้ผลลัพธ์จากการ query มาแต่ละตัว
                            val provinceName = feature.attributes.get("NAME1")
                            // เช็คค่า attributes "NAME1"
                            isBkk = provinceName?.equals("กรุงเทพมหานคร") == true

                        }
                        if (isBkk == true){
                            navController.navigate(R.id.action_allow_to_main);
                        }else{
                            navController.navigate(R.id.action_allowFragment_to_sorryFragment);

                        }

                    }
                    else {

                        "No Results Found99".also {
                            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                            Log.d("test", it)

                        }
               }
                } catch (e: Exception) {
                    "No Results Found1".also {
                        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                        Log.d("test", it)
                    }
                }
            }
            //}
     }
        else{
            Toast.makeText(activity, "Cannot", Toast.LENGTH_LONG).show()
            navController.navigate(R.id.action_allow_to_main);
        }

        }
        nextBtn1 = v.findViewById(R.id.goToLoginBtn1)
        nextBtn1.setOnClickListener {
            navController.navigate(R.id.action_allow_to_main);
        }

        return v

    }

    @SuppressLint("MissingPermission")
    private fun currentLocation101(context: Context):Point {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null
        for (i in providers.size - 1 downTo 0) {
            location= locationManager.getLastKnownLocation(providers[i])
            if (location != null)
                break
        }
        val gps = DoubleArray(2)
        if (location != null) {
            gps[0] = location.getLatitude()
            gps[1] = location.getLongitude()

        }
        var l = Point(gps[1],gps[0], SpatialReferences.getWgs84())
        return l
    }


    @SuppressLint("MissingPermission")
    fun manageLocation(){
        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
        {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
        }
        else
        {
            var locationCb = object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    var last_location:Location? = p0?.lastLocation
                    if (last_location != null)
                    {
                    //    longp = last_location.longitude
                      //  latp = last_location.latitude


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
                   // navController.navigate(R.id.action_allowFragment_to_location_me);
                    break
                }
            }
            if (!granted){
                Toast.makeText(activity, "No permission", Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment AllowFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}