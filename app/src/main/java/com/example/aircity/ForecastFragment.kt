package com.example.aircity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aircity.Adapter.ForecastAdapter
import com.example.aircity.Adapter.ForecastOnlineViewItem
import com.example.aircity.Adapter.ForecastViewItem
import com.example.aircity.repository.FirestoreRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForecastFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForecastFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var forecastImageOnlineList: MutableList<ForecastOnlineViewItem> = mutableListOf<ForecastOnlineViewItem>()
    private var firebaseRepository = FirestoreRepository()
    private lateinit var forecastRecycleView: RecyclerView

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
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forecastRecycleView = view.findViewById<RecyclerView>(R.id.airtable);
/*        val forecastList = mutableListOf<ForecastViewItem>()
        forecastList.add(ForecastViewItem("จ.  26/07" ,"มีเมฆมาก",R.drawable.a1,"25-30","10.9"))
        forecastList.add(ForecastViewItem("อ.  27/07","มีเมฆมาก",R.drawable.a1,"25-30","10.9"))
        forecastList.add(ForecastViewItem("พ.  28/07","มีเมฆมาก",R.drawable.a1,"25-30","10.9"))
        forecastList.add(ForecastViewItem("พฤ. 29/07","มีเมฆมาก",R.drawable.a1,"25-30","10.9" ))
        forecastList.add(ForecastViewItem("ศ.  30/07","มีเมฆมาก",R.drawable.a1,"25-30","10.9" ))
        forecastList.add(ForecastViewItem("ส.  31/07","มีเมฆมาก",R.drawable.a1,"25-30","10.9" ))
        forecastList.add(ForecastViewItem("อา. 01/07","มีเมฆมาก",R.drawable.a1,"25-30","10.9" ))*/
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener {
        fetchForecastDataFromDatabase()
        }
        forecastRecycleView = view.findViewById<RecyclerView>(R.id.airtable);
        fetchForecastDataFromDatabase()
       // forecastRecycleView.adapter = ForecastAdapter(forecastList)
    }

    private fun fetchForecastDataFromDatabase() {
        firebaseRepository.getSavedCategories().get().addOnSuccessListener { documents ->
            forecastImageOnlineList.clear()
            for (document in documents) {
                forecastImageOnlineList.add(document.toObject(ForecastOnlineViewItem::class.java))
            }
            forecastRecycleView.adapter = ForecastAdapter(forecastImageOnlineList)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForecastFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForecastFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}