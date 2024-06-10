package com.example.aircity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.aircity.Adapter.OnBoardingViewAdapter
import com.example.aircity.Adapter.OnBoardingViewItem
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingFragment : Fragment() {
    lateinit var navController: NavController
    private lateinit var onBoardingViewAdapter: OnBoardingViewAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var mAuth: FirebaseAuth
    private lateinit var indicatorList: List<ImageView>
    private lateinit var goBtn: Button
    private lateinit var nextBtn: Button
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        // Create List of Page
        val onBoardingPageLists = listOf<OnBoardingViewItem>(
                OnBoardingViewItem( "เป็นคนแรกที่รู้ค่าฝุ่นล่วงหน้าและป้องกันได้ทันท่วงที", R.drawable.onboarding3_bg),
                OnBoardingViewItem("รักษาสุขภาพด้วย Tips ที่บอกความเหมาะสมในการทำกิจกรรมกลางแจ้งหรือในร่ม", R.drawable.onboarding2_bg),
                OnBoardingViewItem("สามารถเป็นส่วนหนึ่งในการรับรู้พื้นที่เกิดฝุ่น การแจ้งปัญหา และช่วยป้องกัน การเกิดเผาไหม้ในจุดต่างๆ", R.drawable.onboarding_bg)
        )

        val indicator1 = view.findViewById<ImageView>(R.id.indicator1)
        val indicator2 = view.findViewById<ImageView>(R.id.indicator2)
        val indicator3 = view.findViewById<ImageView>(R.id.indicator3)

        // handle button
        nextBtn = view.findViewById<Button>(R.id.nextBtn)
        goBtn = view.findViewById<Button>(R.id.goBtn)

        indicatorList = listOf(
                indicator1,
                indicator2,
                indicator3
        )

        // Create ViewAdapter with that list
        onBoardingViewAdapter = OnBoardingViewAdapter(onBoardingPageLists)

        // Assign Adapter to PageViewer Adapter. Done \(^_^)/
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager2)
        viewPager.adapter = onBoardingViewAdapter
        setCurrentIndicator(0)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        nextBtn.setOnClickListener{
            if (viewPager.currentItem + 1 < onBoardingViewAdapter.itemCount) {
                viewPager.currentItem += 1
            }
        }

        goBtn.setOnClickListener{
            navController.navigate(R.id.action_onboarding_to_allow);
        }
    }

    fun setCurrentIndicator(index: Int) {
        for (i in indicatorList.indices) {
            if (i == index) {
                indicatorList[i].setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.indicator_active)
                )
            } else {
                indicatorList[i].setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.indicator_inactive)
                )
            }
        }

        // check last page
        if (index == onBoardingViewAdapter.itemCount - 1) {
            nextBtn.visibility = View.GONE
            goBtn.visibility = View.VISIBLE
        } else {
            nextBtn.visibility = View.VISIBLE
            goBtn.visibility = View.GONE
        }
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnboardingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                OnboardingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}