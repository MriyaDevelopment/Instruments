package com.decorator1889.instruments.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appodeal.ads.Appodeal
import com.decorator1889.instruments.Adapters.SurgeryCategoryAdapter
import com.decorator1889.instruments.LocalDb.RealmDbSurgery
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.Models.CategoryModel
import com.decorator1889.instruments.Network.OpenAirQuality
import com.decorator1889.instruments.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_category.*

class SurgeryCategoryFragment : Fragment(), SurgeryCategoryAdapter.Clicker {
    var open = OpenAirQuality()
    var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    private var layoutManager: LinearLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_category, container, false)
        loadCompany()
        return view
    }


    @SuppressLint("WrongConstant")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val categories = ArrayList<CategoryModel>()
        categories.add(
            CategoryModel(R.drawable.ic_scalpel,
            "Разъединяющие",
            "surgery",
            "separation")
        )
        categories.add(
            CategoryModel(
                R.drawable.ic_needle2,
                "Соединяющие",
                "surgery",
                "connection")
        )
        categories.add(
            CategoryModel(R.drawable.ic_hook,
            "Раздвигающие, оттесняющие",
            "surgery",
                "spreading")
        )
        categories.add(
            CategoryModel(R.drawable.ic_pincet,
            "Удерживающие",
            "surgery",
                "hold")
        )
        categories.add(
            CategoryModel(R.drawable.ic_acupuncture,
            "Колющие",
            "surgery",
                "stabbing")
        )

        val adapter = SurgeryCategoryAdapter(categories, this, this.activity!!)
        layoutManager = LinearLayoutManager(activity)
        rcv?.layoutManager = layoutManager
        rcv?.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.showUpButton()
    }

    override fun onClick(company: CategoryModel) {
        val bundle = Bundle()
        bundle.putString("type", company.type)
        bundle.putString("surgeryType", company.surgeryType)
        val fragment: Fragment
        fragment = SurgeryFragment()
        fragment.arguments = bundle
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment).addToBackStack(null)
        transaction.commit()
    }
    private fun loadCompany() {

        mCompositeDisposable?.add(
            open.fetchSurgeryData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ results -> RealmDbSurgery.saveResult(results) }, this::initError)
        )

    }
    fun initError(error: Throwable){
        println(error)

    }
}