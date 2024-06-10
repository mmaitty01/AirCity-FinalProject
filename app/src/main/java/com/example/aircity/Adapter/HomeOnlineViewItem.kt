package com.example.aircity.Adapter
import android.location.Location
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.google.firebase.firestore.GeoPoint


data class HomeOnlineViewItem (val bk: String= "",
                               val PM25 : String="",
                               val AQI: String ="",
                               val img: String ="",
                               val long: String ="",
                               val  lat : String ="",
                               val mask :String ="",
                               val out:String ="",
                               val inp :String ="",
                               val time:String ="",
                               val qAqi:String=""
                               )

//data class HomeOnlineViewItem (val nameTH: String= "", val PM25 : String="",val AQI: String ="",val location:GeoPoint )