package com.example.aircity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.data.FeatureQueryResult
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.portal.Portal
import com.esri.arcgisruntime.portal.PortalItem
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.example.aircity.Adapter.*

import com.example.aircity.repository.FirestoreRepository_aqi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"

    /**
     * A simple [Fragment] subclass.
     * Use the [HomeFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    class HomeFragment : Fragment() {
        private lateinit var mMapView: MapView
        private lateinit var graphicLayer: GraphicsOverlay

        private lateinit var homeAdapter: HomeAdapter
        private lateinit var homeViewPager2: ViewPager2


        private var HomePageLists: MutableList<HomeOnlineViewItem> = mutableListOf<HomeOnlineViewItem>()
        private var firebaseRepository = FirestoreRepository_aqi()
        private lateinit var forecastRecycleView: RecyclerView
        val listOfPoint = mutableListOf<Point>()

        private var isFirst: Boolean = true



        private var lastLocation: Location? = null

        private lateinit var fusedLocationClient: FusedLocationProviderClient

        // TODO: Rename and change types of parameters
        private var param1: String? = null
        private var param2: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }
            ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud3065311013,none,PM0RJAY3FL9H9KB10185")

            // add some buoy positions to the graphics overlay

        }


        private val locationDisplay: LocationDisplay by lazy { mMapView.locationDisplay }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_home2, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            mMapView = view.findViewById<MapView>(R.id.mapView);
          //  val latitude = 13.700547
           // val longitude = 100.535619
          //  val levelOfDetail = 9
          //  val map = ArcGISMap(Basemap.Type.STREETS_VECTOR, latitude, longitude, levelOfDetail)
    //        mMapView.map = map
           mMapView.isAttributionTextVisible = false

            // set the viewpoint, Viewpoint(latitude, longitude, scale)
         //  mMapView.setViewpoint(Viewpoint(13.700547, 100.535619, 200000.0))

            val portal = Portal("https://www.arcgis.com", false)

           // val itemId = "fecc8485537144b9a64ea8084e65385e"
            val itemId = "0ff5957d617b4071801baf6c509fb5ef"
            val portalItem = PortalItem(portal, itemId)
            val map = ArcGISMap(portalItem)


            mMapView.map = map

            // create the service feature table
           /* val serviceFeatureTable =
                    FeatureLayer(ServiceFeatureTable("https://services8.arcgis.com/M7A4yaF6jGbUWuTJ/ArcGIS/rest/services/survey123_184e9a0bdf2a4f1198c707a071f4a927_stakeholder/FeatureServer/0"))

            // create the feature layer using the service feature table
            //   val featureLayer = FeatureLayer(serviceFeatureTable)


            map.operationalLayers.add(serviceFeatureTable)*/


            var viewAllCategoryText = view.findViewById<ImageView>(R.id.iv_info);
            viewAllCategoryText.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fl_container, HomeViewFragment())
                            .addToBackStack("HomeFragment")
                        .commit()
                }
            }

            addGraphics()



            //------------สำหรับสร้าง viewpager2-----------

           // homeAdapter = HomeAdapter(homeList)
            homeViewPager2 = view.findViewById<ViewPager2>(R.id.homeViewPager2);

            fetchCategoriesDataFromDatabase()


        }
        private fun requestPermissions(dataSourceStatusChangedEvent: LocationDisplay.DataSourceStatusChangedEvent) {
            val requestCode = 2
            val reqPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
       /*     // fine location permission
            val permissionCheckFineLocation =
                ContextCompat.checkSelfPermission(activity!!, reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED
            // coarse location permission
            val permissionCheckCoarseLocation =
                ContextCompat.checkSelfPermission(activity!!, reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED
            if (!(permissionCheckFineLocation && permissionCheckCoarseLocation)) { // if permissions are not already granted, request permission from the user
                ActivityCompat.requestPermissions(activity!!, reqPermissions, requestCode)
            } else {
                // report other unknown failure types to the user - for example, location services may not
                // be enabled on the device.
                val message = String.format(
                    "Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                        .source.locationDataSource.error.message
                )
                Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
            }*/
        }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            // if request is cancelled, the results array is empty
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationDisplay.startAsync()


            }

            else {
                Toast.makeText(
                    activity!!,
                    resources.getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        private fun addCurrentLocation(extent: Envelope) {
            val currentLocationPoint = Point(100.536501,  13.729852, SpatialReferences.getWgs84())
            // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
            val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 10f)
            val blueOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2f)
            simpleMarkerSymbol.outline = blueOutlineSymbol
            // create a graphic with the point geometry and symbol
            val pointGraphic = Graphic(currentLocationPoint, simpleMarkerSymbol)
            // add the point graphic to the graphics overlay
            graphicLayer.graphics.add(pointGraphic)
            // zoom to current location point
            mMapView.setViewpointAsync(Viewpoint(extent));


        }

        private fun addGraphics() {
            graphicLayer = GraphicsOverlay()
            mMapView.graphicsOverlays.add(graphicLayer)
        }


        private fun genShopPoint(x: String, y: String ,addToMap: Boolean = false):Point{
            val loca_long = x.toDouble()
            val loca_lat = y.toDouble()
            val point = Point(loca_long, loca_lat, SpatialReferences.getWgs84())
            if (addToMap) {
                // code for  get image in drawable folder
                val pictureMarkerSymbol = PictureMarkerSymbol.createAsync(
                        ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bk
                        ) as BitmapDrawable
                ).get()

                // set width, height, z from ground
                pictureMarkerSymbol.height = 52f
                pictureMarkerSymbol.width = 52f
                pictureMarkerSymbol.offsetY = 11f
                // create a graphic with the point geometry and symbol
                val pointGraphic = Graphic(point, pictureMarkerSymbol)

                // add the point graphic to the graphics overlay
               // graphicLayer.graphics.add(pointGraphic)
            }
           return point

        }


        @SuppressLint("UseRequireInsteadOfGet")
        private fun fetchCategoriesDataFromDatabase() {
            firebaseRepository.getSavedAQI().get().addOnSuccessListener { documents ->
                HomePageLists.clear()
                for (document in documents) {

                    HomePageLists.add(document.toObject(HomeOnlineViewItem::class.java))

                }

                if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    // Requesting the permission
                    // ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
                    createBuffer7km1()
                } else {
                   // Toast.makeText(activity, "Permission already granted", Toast.LENGTH_SHORT).show()
                    createBuffer7km()
                }
                homeViewPager2.adapter = HomeAdapter(HomePageLists)
                           //val listOfPoint = mutableListOf<Point>()

                // find Extent of all point
                HomePageLists.forEachIndexed { key, value ->
                    val lo_long = value.long
                    val lo_lat = value.lat

                    for (i in 1..1){
                        if(key < 65){
                            val location = genShopPoint(lo_long,lo_lat)
                            listOfPoint.add(location)
                        }

                    }
                }
                var mCompleteExtent: Envelope = GeometryEngine.combineExtents(listOfPoint)
                var newX1 = mCompleteExtent.xMin - mCompleteExtent.xMin*0.0001
                var newY1 = mCompleteExtent.yMin - mCompleteExtent.yMin*0.0001
                var newX2 = mCompleteExtent.xMax + mCompleteExtent.xMax*0.0001
                var newY2 = mCompleteExtent.yMax + mCompleteExtent.yMax*0.0001
                var mExtent2 = Envelope(newX1, newY1, newX2, newY2, mCompleteExtent.spatialReference)

                homeViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (isFirst) {
                            isFirst = false;
                            addCurrentLocation1(mExtent2)
                        } else
                            mMapView.setViewpointCenterAsync( genShopPoint(HomePageLists[position].long ,HomePageLists[position].lat), 20000.0)
                      }
                })

            }
        }

        private fun addCurrentLocation1(mExtent2: Envelope) {

                locationDisplay.addDataSourceStatusChangedListener {
                    // if LocationDisplay isn't started or has an error
                    if (!it.isStarted && it.error != null) {
                        // check permissions to see if failure may be due to lack of permissions
                        requestPermissions(it)
                    }
                }

                locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER)
                locationDisplay.startAsync()
                //mMapView.setViewpointAsync(Viewpoint(mExtent2))

        }

        @SuppressLint("MissingPermission")
        fun getLastKnownLocation(context: Context):Point{
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


        @SuppressLint("MissingPermission", "UseRequireInsteadOfGet")
        private fun createBuffer7km() { 
        /*    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(activity!!) { task ->
                //   var lastLocation:Location?= task.result
                lastLocation = task.result

            var    currentLocation101 = Point(
                    (lastLocation)!!.longitude,
                    (lastLocation)!!.latitude,
                    SpatialReferences.getWgs84()
                )*/
              //    var currentLocation101 = Point(100.536501,  13.729852, SpatialReferences.getWgs84())
                // create buffer polygon
                // create buffer geometry 100 meter
                val geometryBuffer = GeometryEngine.bufferGeodetic(getLastKnownLocation(context!!), 3.0,
                    LinearUnit(LinearUnitId.KILOMETERS), Double.NaN, GeodeticCurveType.GEODESIC
                )

                // create symbol for buffer geometry
                val geodesicOutlineSymbol =
                    SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2F)
                // 0x4D00FF00 = Green Color with 25% Opacity (4D = 25%)
                val geodesicBufferFillSymbol = SimpleFillSymbol(
                    SimpleFillSymbol.Style.SOLID,
                    0x4D00FF00.toInt(), geodesicOutlineSymbol
                )

                // new graphic
                val graphicBuffer = Graphic(geometryBuffer, geodesicBufferFillSymbol)
             //   graphicLayer.graphics.add(graphicBuffer)

                // filter 7km shop
                HomePageLists = HomePageLists.filter {
                    var isInside =
                        GeometryEngine.intersects(geometryBuffer, genShopPoint(it.long, it.lat))
                    isInside
                }.toMutableList()
                // add all filtered shop to map
                HomePageLists.forEach {
                    genShopPoint(it.long, it.lat, true)
                }
       //    }


        }

        private fun createBuffer7km1() {

               var currentLocation101 = Point(100.536501,  13.729852, SpatialReferences.getWgs84())

                // create buffer polygon
                // create buffer geometry 100 meter
                val geometryBuffer = GeometryEngine.bufferGeodetic(
                    currentLocation101, 3.0,
                    LinearUnit(LinearUnitId.KILOMETERS), Double.NaN, GeodeticCurveType.GEODESIC
                )

                // create symbol for buffer geometry
                val geodesicOutlineSymbol =
                    SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2F)
                // 0x4D00FF00 = Green Color with 25% Opacity (4D = 25%)
                val geodesicBufferFillSymbol = SimpleFillSymbol(
                    SimpleFillSymbol.Style.SOLID,
                    0x4D00FF00.toInt(), geodesicOutlineSymbol
                )

                // new graphic
                val graphicBuffer = Graphic(geometryBuffer, geodesicBufferFillSymbol)
             //   graphicLayer.graphics.add(graphicBuffer)

                // filter 7km shop
               var HomePageLists = HomePageLists.filter {
                    var isInside =
                        GeometryEngine.intersects(geometryBuffer, genShopPoint(it.long, it.lat))
                    isInside
                }.toMutableList()
                // add all filtered shop to map
                HomePageLists.forEach {
                    genShopPoint(it.long, it.lat, true)
                }


        }


        override fun onPause() {
            super.onPause()
            mMapView.pause()
        }

        override fun onResume() {
            super.onResume()
            mMapView.resume()
        }

        override fun onDestroy() {
            super.onDestroy()
            mMapView.dispose()
        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


