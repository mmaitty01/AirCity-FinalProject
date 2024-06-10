package com.example.aircity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import androidx.viewpager2.widget.ViewPager2
import com.esri.arcgisruntime.mapping.view.MapView
import com.example.aircity.Adapter.*
import com.example.aircity.repository.FirestoreRepository
import com.example.aircity.repository.FirestoreRepository_D
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [searchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class searchFragment : Fragment() {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var SearchAdapter: SearchAdapter
    private lateinit var ViewPager2: ViewPager2
    private var SearchPageLists: MutableList<SearchOnlineViewItem> = mutableListOf<SearchOnlineViewItem>()
    private var firebaseRepository = FirestoreRepository_D()
    private lateinit var forecastRecycleView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    



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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeHomeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener {
            fetchCategoriesDataFromDatabase()
        }


        // Create List of Page
        /*val SearchPageLists = listOf<SearchViewItem>(
                SearchViewItem("เขตบางขุนเทียน", "จ. 27/08", "ปานกลาง", "38", "อ. 28/08", "ดี", "18"),
                SearchViewItem("เขตบางนา", "จ. 27/08", "ดีมาก", "10", "อ. 28/08", "ดี", "15"),
                SearchViewItem("เขตพระนคร", "จ. 27/08", "ดี", "28", "อ. 28/08", "ดี", "10")
        )*/

     //  SearchAdapter = SearchAdapter(SearchPageLists)
        ViewPager2 = view.findViewById<ViewPager2>(R.id.tpm);
        fetchCategoriesDataFromDatabase()
      // ViewPager2.adapter = SearchAdapter;


        var etSearchInput = view.findViewById<EditText>(R.id.etSearchInput);
        etSearchInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (etSearchInput.text.length > 0) {
                    var filteredItem = SearchPageLists.filter {
                        it.district.toLowerCase().contains(etSearchInput.text.toString().toLowerCase())
                    }
                    ViewPager2.adapter = SearchAdapter(filteredItem)
                } else {
                    ViewPager2.adapter = SearchAdapter(SearchPageLists)
                }
            }
        })


            }
    private fun fetchCategoriesDataFromDatabase() {
        firebaseRepository.getSavedDiscover().get().addOnSuccessListener { documents ->
            SearchPageLists.clear()
            for (document in documents) {
                SearchPageLists.add(document.toObject(SearchOnlineViewItem::class.java))
            }
            ViewPager2.adapter = SearchAdapter(SearchPageLists)
            // hide pull loading
            swipeRefreshLayout.isRefreshing = false



        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment searchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            searchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}