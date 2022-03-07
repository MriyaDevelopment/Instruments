package com.decorator1889.instruments.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appodeal.ads.Appodeal
import com.decorator1889.instruments.Adapters.CategoryAdapter
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.Models.CategoryModel
import com.decorator1889.instruments.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_category.*
import java.util.*
import kotlin.collections.ArrayList

class CategoryFragment : Fragment(), CategoryAdapter.Clicker {
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    @SuppressLint("WrongConstant")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val categories = ArrayList<CategoryModel>()
        categories.add(
            CategoryModel(R.drawable.ic_surgery_main,
                "Общая хирургия",
                "surgery",
                "nothing"))
        categories.add(
            CategoryModel(R.drawable.ic_pregnant,
                "Акушерство и гинекология",
                "gynecology",
                "nothing"))
        categories.add(CategoryModel(R.drawable.ic_otoscope,
                "Отоларингология",
                "lor",
                "nothing"))
        categories.add(CategoryModel(R.drawable.ic_oculist,
                "Офтальмология",
                "ophthalmology",
                "nothing"))
        categories.add(CategoryModel(R.drawable.ic_stomatology,
                "Стоматология",
                "stomatology",
                "nothing"))
        categories.add(CategoryModel(R.drawable.ic_neurology,
                "Нейрохирургия",
                "neuro",
                "nothing"))
        categories.add(CategoryModel(R.drawable.ic_urology,
            "Урология",
            "urology",
            "nothing"))
        categories.add(CategoryModel(R.drawable.ic_anestesia,
            "Анестезиология",
            "anesthesiology",
            "nothing"))

        val adapter = CategoryAdapter(categories, this, this.activity!!)
        layoutManager = LinearLayoutManager(activity)
        rcv?.layoutManager = layoutManager
        rcv?.adapter = adapter

    }


    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.hideUpButton()
    }

    override fun onClick(company: CategoryModel) {
        if (company.type == "surgery") {
            val fragment: Fragment
            fragment = SurgeryCategoryFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment).addToBackStack(null)
            transaction.commit()
        }
        else {
            val bundle = Bundle()
            bundle.putString("type", company.type)
            val fragment: Fragment
            fragment = InstrumentFragment()
            fragment.arguments = bundle
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, fragment).addToBackStack(null)
            transaction.commit()
        }

    }

    override fun onClick1(company: CategoryModel) {
        val activity = activity as MainActivity
        val rand = Random()
        val chance: Int = rand.nextInt(10)
        if (chance > 6)
        {
            Appodeal.show(activity, Appodeal.INTERSTITIAL);
        }
        val bundle = Bundle()
        bundle.putString("testType", company.type)
        bundle.putString("testName", company.name)
        val fragment: Fragment
        fragment = TestFragment()
        fragment.arguments = bundle
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment).addToBackStack(null)
        transaction.commit()
    }

}